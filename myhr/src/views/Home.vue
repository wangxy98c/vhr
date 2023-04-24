<script setup lang="ts">
import {
  Document,
  Menu as IconMenu,
  Location,
  Setting,
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getRequest } from '@/utils/api'
import { useRouter } from 'vue-router';
import { useStore } from '@/store';
console.log("home页的开始启用")
//import { computed } from 'vue';
const store=useStore();
//重要代码，如果不加。则会：在store初始化前渲染完成，导致首次登陆看不到目录项
//（因为initMenu完成前就展示了页面了）
const routes=store.menuRoutes;//把beforeEach强制为同步了，可用了。不需要computed了
//const routes=computed(()=>store.menuRoutes);//必须使用computed写法，不然首次进入出问题（进入时store未加载完成）
const router=useRouter();//用于页面跳转（退出登陆）
//console.log('Home页所得的router',router.getRoutes())
const user=JSON.parse(window.sessionStorage.getItem("user")||'{}');
const handleOpen = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const handleClose = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
const handleCommand = (command: string | number | object) => {
  if(command=='userinfo'){

  }else if(command=='setting'){
    
  }else if(command=='logout'){
    ElMessageBox.confirm(
      '确定退出用户？',
      'Warning',
      {
        confirmButtonText: '确定退出',
        cancelButtonText: '暂不退出',
        type: 'warning',
      }
    )
    .then(() => {
      //退出代码
      getRequest('/logout','');
      window.sessionStorage.removeItem('user');
      router.replace('/');//拦截器会提醒注销成功，不需要额外再写
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '取消退出',
      })
    })
  }
}
// const menuClick=(index:string)=>{
//   //index即标记(可以直接设置为路径)，indexpath即从父到此的路径数组
//   router.push(index);
// }
</script>

<template>
<div>
    <el-container>
      <el-header class="homeHeader">
        <div class="homeHeader title">微人事</div>
        <el-dropdown @command="handleCommand">
          <span class="el-dropdown-link">
            {{user.name}}
            <img :src="user.userface"/>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="userinfo">个人中心</el-dropdown-item>
              <el-dropdown-item command="setting">设置</el-dropdown-item>
              <el-dropdown-item command="logout" divided>注销登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>

      <el-container>
        <el-aside width="200px">
          <el-col :span="30">
            <el-menu router>
              <!-- 本来直接el-sub-menu就行，但需要把v-for放外面，故而用了template包裹一下 -->
              <!-- <template v-for="(item,index) in routes"> -->
                <el-sub-menu :index="index.toString()" v-for="(item,index) in routes">
                  <template #title>
                    {{ item.name }}
                  </template>
                  <el-menu-item :index="child.path.toString()" v-for="(child,indexj) in item.children">{{ child.name }}</el-menu-item>
                </el-sub-menu>
              <!-- </template> -->
              <!-- v-for,v-if不能同时用，本想内层v-if 发现template无法包裹？（且在template上v-if仍会有空白项）
              故而把v-for放到外层，原本的位置变v-if，问题得到解决 -->
              <!-- <el-sub-menu index="1" v-for="(item,index) in router.options.routes" v-if="!item.hidden">
                  <template #title>
                    <span>{{ item.name }}</span>
                  </template>
                  <el-menu-item :index="child.path" v-for="(child,indexj) in item.children">{{ child.name }}</el-menu-item>
              </el-sub-menu> -->
            </el-menu>
          </el-col>
        </el-aside>
        
        <el-main>
          <el-breadcrumb separator-class="el-icon-arrow-right" v-show="router.currentRoute.value.path!='/home'">
            <!-- v-if和v-show:v-if是完全不存在元素(DOM中不存在),而v-show仅仅是隐藏(DOM中仍可见) -->
            <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item >{{ router.currentRoute.value.name}}</el-breadcrumb-item>
          </el-breadcrumb>
          <div class="homeWelcomeCls" v-if="router.currentRoute.value.path=='/home'">
            欢迎来到微人事系统
          </div>
          
          <RouterView class="homeRouterViewCls"/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<style scoped>
.homeRouterViewCls{
  margin-top: 10px;
}
.homeHeader{
  background-color: #409eff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0px 15px;
  box-sizing: border-box;
}
.homeHeader.title{
  font-size: 30px;
  font-family: 华文行楷;
  /* 字体在mac上不生效 */
  color:#ffffff;
}
.el-dropdown-link img{
  margin-left: 5px;
  width: 30px;
  height: 30px;
  border-radius: 24px;
}.el-dropdown-link{
  display: flex;
  align-items: center;
}
.homeWelcomeCls{
  text-align: center;
  font-size: 30px;
  font-family: 华文行楷;
  /* 字体在mac上不生效 */
  color: #409eff;
  padding-top: 50px;
}
</style>