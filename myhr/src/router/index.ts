import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Home from '../views/Home.vue'
import Chat from '../views/Chat.vue'
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
      children:[
        {
          path:'/chat',
          name:'在线聊天',
          component:Chat,
        },//子目录
      ]
    },
  ]
})

export default router
