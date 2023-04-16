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
const store=useStore();
const routes=store.menuRoutes;
console.log("home页的store-menuRoutes",routes)
const user=JSON.parse(window.sessionStorage.getItem("user")||'{}');
const router=useRouter();
console.log("home页的router：",router);
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
                  <el-menu-item :index="child.path" v-for="(child,indexj) in item.children">{{ child.name }}</el-menu-item>
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
          <RouterView />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<style scoped>
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
</style>