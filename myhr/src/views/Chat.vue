<template>
    <div id="app">
        <div class="sidebar">
        <card></card>
        <list></list>
        </div>
        <div class="main">
        <message></message>
        <usertext></usertext>
        </div>
    </div>
</template>

<script setup lang="ts">
import card from '../components/chat/card.vue'
import list from '../components/chat/list.vue'
import message from '../components/chat/message.vue'
import usertext from '../components/chat/usertext.vue'
import { usechatstore } from '@/store'
import { onMounted } from 'vue'
const chatStore=usechatstore()

const chatstore=usechatstore()
onMounted(()=>{
  chatstore.initData()
})
</script>

<style lang="scss" scoped>
#app {
    margin: 5px 5px;
    width: 800px;
    height: 600px;
    overflow: hidden;
    border-radius: 10px;
    .sidebar, .main {
        height: 100%;
    }
    .sidebar {
        float: left;
        color: #f4f4f4;
        background-color: #2e3238;
        width: 200px;
    }
    .main {
        position: relative;
        overflow: hidden;
        background-color: #eee;
    }
}
</style>