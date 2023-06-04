<template>
  <div id="uesrtext">
    <textarea placeholder="按 Ctrl + Enter 发送" v-model="content" v-on:keyup="addMessage"></textarea>
  </div>
</template>

<script setup lang="ts">
import { usechatstore } from '@/store';
import { ref } from 'vue';
const content=ref('')
const chatStore=usechatstore()
const addMessage=(e:KeyboardEvent)=>{
  if(e.ctrlKey && e.keyCode==13 &&content.value.length){
    console.log('触发发送信息')
    var obj=new Object();
    obj.to=chatStore.currentSession.username;
    obj.content=content.value;
    console.log(JSON.stringify(obj))
    chatStore.stomp.send('/ws/chat',{},JSON.stringify(obj));
    chatStore.addMessage(obj);
    content.value=''
  }
}

</script>

<style lang="scss" scoped>
#uesrtext {
    position: absolute;
  bottom: 0;
  right: 0;
  width: 100%;
  height: 30%;
  border-top: solid 2px black;
  > textarea {
      padding: 10px;
      width: 590px;
      height: 172px;
      border: none;
      outline: none;
  }
}
</style>