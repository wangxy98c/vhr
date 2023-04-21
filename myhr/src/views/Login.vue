<script setup lang="ts">
import { reactive,ref} from 'vue';
import type {FormInstance, FormRules} from 'element-plus'
import {postKeyValueRequest} from '../utils/api'
import { useRouter } from 'vue-router';
import { useRoute } from 'vue-router';
import { initMenu } from '@/utils/menu';
import { useStore } from '@/store';
const router=useRouter();
const route=useRoute();
const store=useStore()
const labelPos=ref('left')
const loginFromRef=ref<FormInstance>()//表单实例，并在标签中ref使用，且submit提交时作为参数
const userdata=reactive({
    username: 'admin',
    password: '123',
})
const loginrules=reactive<FormRules>({
    username:[
        {required:true ,message:'请输入用户名后登录',trigger:'blur'},
        {min:3 ,max:15,message:'长度必须在3-15之间',trigger:'blur'}
    ],
    password:[
        {required:true,message:'请输入密码后登录',trigger:'blur'},
        {min:3 ,max:15,message:'长度必须在3-15之间',trigger:'blur'}
    ]
})

const submitLogin=async(formEl:FormInstance|undefined)=>{
    if(!formEl) return
    await formEl.validate((valid,fields)=>{
        if(valid){
            //由于返回的respone已被处理（返回respone.data）,故而.then不用再data
            postKeyValueRequest('/doLogin',userdata).then(function(resp:any){
                if(resp){
                    //最初的responce返回体data中的.obj才是user信息。data已经在拦截器中过滤出来了
                    window.sessionStorage.setItem("user",JSON.stringify(resp.obj));
                    //console.log("LOGIN页：",window.sessionStorage.getItem("user"));
                    const path=route.query.redirect;//它不能写在上边并用computed。
                    //console.log("请求路径",path);
                    //initMenu(router,store);不宜在此处初始化菜单，因为可能使用刷新，导致home页没菜单了
                    //也不宜在home页面初始化，因为可能不在home页面刷新。   使用路由导航守卫
                    router.replace((path=='/'||path==undefined)?'/home':path);//页面跳转
                }
            })
        }else{
            console.log('登陆出错',fields)
        }
    })
}
</script>
<template>
  <div>
    <el-form :rules="loginrules" :label-position='labelPos' :model="userdata" ref="loginFromRef">
        <h1> 登录 </h1>
        <el-form-item prop="username" label="用户名">
            <!-- 上面prop必须是userdata中定义的数据,且验证规则中也是它 -->
            <el-input v-model="userdata.username" placeholder="输入用户名"/>
        </el-form-item>
        <el-form-item prop="password" label="密码">
            <el-input v-model="userdata.password" type="password"/>
        </el-form-item>
        <el-form-item>
            <el-button type='primary' width="50px" @click="submitLogin(loginFromRef)" >登录</el-button>
        </el-form-item>
      </el-form>
  </div>
</template>

<style>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>
