import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Home from '../views/Home.vue'
const router = createRouter({
  history: createWebHistory(),
  routes: [
    {
      path: '/',
      name: 'Login',
      component: Login,
      meta:{
        hidden: true,
      }
    },
    {
      path:'/home',
      name: 'Home',
      component:Home,
    },
    // {
    //   path: '/home',
    //   name: '系统管理',
    //   children:[
    //     {
    //       path: '/sys/cfg',
    //       name: '系统管理',
    //       component: ()=>import('../views/sys/SysCfg.vue')
    //     }
    //   ]
    // },//即使加上也不是home页下的sysCfg，而是一个单独的页面
  ]
})

export default router
