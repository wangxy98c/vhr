import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import {createPinia} from 'pinia'
import { initMenu} from './utils/menu'
import { useStore } from './store'
import LoginVue from './views/Login.vue'

const app = createApp(App)
app.use(router)
app.use(ElementPlus)
app.use(createPinia())
const store=useStore();


router.beforeEach( async(to,from)=>{
    if(to.path=='/'){
        return true;
    }else{
        if(window.sessionStorage.getItem("user")){
            //用户登陆过了（为了防止没登陆就直接去其他页面）
            console.log("将要去的地址：",to.path)
            await initMenu(router,store)//需要注意initMenu的写法。async function为了实现同步
            console.log("router已经放行")
            return true;
        }
        else{
            //为了能让登陆失败的用户在登陆后直接去到刚刚想去的地址，使用重定向记录

            //return {name: 'Login'};
            return `/?redirect=${to.path}`
        }
        
    }
})
// router.beforeEach((to, from, next) => {
//     if (to.path == '/') {
//         next();
//     } else {
//         if (window.sessionStorage.getItem("user")) {
//             initMenu(router, store);
//             if(store.menuRoutes.length>0){
//                 next();
//             }else{
//                 next({...to,replace:true});//不断得回到login页，但现在好像不能用了
//             }
//         } else {
//             next('/?redirect=' + to.path);
//         }
//     }
// })
app.mount('#app')
