<template>
    <div id="message" v-scrollbottom="sessions">
        <div v-if="currentSession.username">
            <template v-for="item in sessions[user.username+'#'+currentSession.username]">
                    <p class="time">
                        <!-- entry.date不是一个Date对象，需要处理成对象后转化为字串 -->
                        <span>{{ `${new Date(item.date).toLocaleDateString()}` }}</span>
                    </p>
                    <div class="main" :class="{self:item.self}">
                        <img class="avatar" :src="item.self ? user.userface : currentSession.userface" alt="">
                        <p class="text">{{item.content}}</p>
                    </div>
            </template>
        </div>
        
    </div>
  </template>
  
<script setup lang="ts">
import {usechatstore} from '@/store'
import { computed } from 'vue'
const chatStore=usechatstore()
const img='../src/assets/images/1.jpg'
const sessions=computed(()=>chatStore.sessions)
const currentSession=computed(()=>chatStore.currentSession)
const user=JSON.parse(window.sessionStorage.getItem("user")||'{}')
//自定义指令,参见https://cn.vuejs.org/guide/reusability/custom-directives.html#introduction
//需要注意，只能有一个大写字母。不能是 vScrollBottom 否则不会生效
const vScrollbottom=(el)=>{
    console.log('自定义指令')
    setTimeout(function () {
        console.log("setTimeout")
        el.scrollTop += 9999;
    }, 10)
}
</script>
  
<style lang="scss" scoped>
#message {
    padding: 15px;
max-height: 68%;
overflow-y: scroll;
ul {
    padding-left: 0px;
    list-style-type: none;
    li {
        margin-bottom: 15px;
    }
}
.time {
    text-align: center;
    margin: 7px 0;
    > span {
        display: inline-block;
        padding: 0 18px;
        font-size: 12px;
        color: #FFF;
        background-color: #dcdcdc;
        border-radius: 2px;
    }
}
.main {
    .avatar {
        float: left;
        margin: 0 10px 0 0;
        border-radius: 3px;
        width: 30px;
        height: 30px;

    }
    .text {
        display: inline-block;
        padding: 0 10px;
        max-width: 80%;
        background-color: #fafafa;
    border-radius: 4px;
    line-height: 30px;
    }
}
.self {
    text-align: right;
    .avatar {
    float: right;
    margin: 0 0 0 10px;
    border-radius: 3px;
    width: 30px;
    height: 30px;
    }
    .text {
    display: inline-block;
    padding: 0 10px;
    max-width: 80%;
    background-color: #b2e281;
    border-radius: 4px;
    line-height: 30px;
    }
}
}
</style>