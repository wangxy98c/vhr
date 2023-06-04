<template>
    <div id="list">
        <ul style="padding-left: 0px;">
            <li v-for="item in hrs" :class="{ active: currentSessionName?item.username == currentSessionName:false }" v-on:click="changeCurrentSession(item)"><!--   :class="[item.id === currentSessionId ? 'active':'']" -->
                <img class="avatar" :src="item.userface" :alt="item.name">
                <!-- alt是图片未加载出来时的显示 -->
                <p class="name">{{item.name}}</p>
            </li>
        </ul>
    </div>
  </template>
  
<script setup lang="ts">
import {usechatstore} from '@/store'
import { computed } from 'vue'
const chatStore=usechatstore()
const hrs=computed(()=>chatStore.hrs)
const currentSessionName=computed(()=>chatStore.currentSession.username)
const changeCurrentSession=(user)=>{
    chatStore.changeCurrentSession(user)
    
    console.log("chat修改了当前sessionName",user.username);
}

</script>
  
<style lang="scss" scoped>
#list {
    li {
        padding: 0px 15px;
        border-bottom: 1px solid #292C33;
        cursor: pointer;
        list-style: none;/*去掉小圆点*/
        
        &:hover {
            background-color: rgba(255, 255, 255, 0.03);
        }
    }
li.active {/*注意这个是.不是冒号:*/
            background-color: rgba(255, 255, 255, 0.1);
    }
    .avatar {
        border-radius: 2px;
        width: 30px;
        height: 30px;
        vertical-align: middle;
    }
    .name {
        display: inline-block;
        margin-left: 15px;
    }
}
  </style>