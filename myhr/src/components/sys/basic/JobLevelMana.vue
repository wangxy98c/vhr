<template>
    <div class="demo-collapse" style="margin-top: 10px;max-width: 800px;">
        <el-collapse accordion @change="change">
            <el-collapse-item :title="role.nameZh" :name="role.id" v-for="(role,index) in roles">
                <el-card class="box-card">
                        <el-tree 
                         ref="treeRef"
                         :data="allMenus" 
                         :props="menuDefaultProps" 
                         show-checkbox 
                         node-key="id" 
                         :default-checked-keys="selectMenus"
                        />
                        <div style="display: flex;justify-content: flex-end;padding-top: 5px;">
                            <el-button >取消修改</el-button>
                            <el-button type="primary" @click="doUpdate(role.id)">确认修改</el-button>
                        </div>
                </el-card>
            </el-collapse-item>
        </el-collapse>
    </div>
  </template>
  
  <script lang="ts" setup>
  
import { onMounted, reactive, ref } from 'vue';
import { getRequest } from '@/utils/api';
import { ElTree } from 'element-plus';

onMounted(()=>{
    initRoles();
})

import type Node from 'element-plus/es/components/tree/src/model/node'

const treeRef = ref<InstanceType<typeof ElTree>>()
const role=reactive({
    name:'',
    nameZh:'',
})//用于添加role
const roles=ref([]);//用于展示所有role
const initRoles=()=>{
    getRequest("/system/basic/permiss/",'').then(resp=>{
        if(resp){
            roles.value=resp;
        }
    })
}//获取角色
const allMenus=ref([])
const selectMenus=ref([])//用于选中哪些项
const menuDefaultProps={
    children:'children',
    label:'name'
}//用于映射后端的各项。如children属性对应返回的children。label对应返回的name字段
const deleteRole=()=>{

}
const change=(name)=>{
    //需要注意，当有参数时。@click=“change()”不应该带().否则就相当于传一个空的name作为参数。而不是原应的返回index
    //console.log("手风琴：",name);
    if(name){
        initMenus();//必须带括号。否则不当函数？
        initSelectMenus(name);//name其实是rid。一般组件默认叫name而已
    }
}
const initSelectMenus=(rid:Number)=>{
    getRequest(`/system/basic/permiss/mids/${rid}`,'').then(resp=>{
        if(resp){
            selectMenus.value=resp;
        }
    })
}
const initMenus=()=>{
    getRequest("/system/basic/permiss/menus",'').then(resp=>{
        allMenus.value=resp;
        //console.log("allMenus:",allMenus);
    })
}
const getCheckedKeys = () => {
    console.log(treeRef.value!.getCheckedKeys(false))
  }
const doUpdate=(rid)=>{
    console.log("所选内容:",treeRef.value[rid]!.getCheckedKeys(true) );// 注意这里，
}//role对可操纵menu进行修改
const activeName=ref('2');//默认打开的是哪个，值应为name字段
  </script>