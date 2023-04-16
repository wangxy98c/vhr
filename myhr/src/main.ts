import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import {createPinia} from 'pinia'
import { initMenu } from './utils/menu'
import { useStore } from './store'

const app = createApp(App)
app.use(router)
app.use(ElementPlus)
app.use(createPinia())
const store=useStore();

router.beforeEach( async(to,from)=>{
    if(to.path=='/'){
        return true;
    }else{
        console.log('_初始化前的routes:',router.getRoutes());
        initMenu(router,store)//此处不同步，会先往下执行。需要额外处理
        
        console.log('_初始化后的routes:',router.getRoutes());
        return true;
    }
})
app.mount('#app')
