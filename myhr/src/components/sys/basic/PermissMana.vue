<template>
    <div class="demo-input-size">
        <el-input v-model="role.name" placeholder="请输入角色英文名" size="large" >
            <template #prepend>ROLE_</template>
        </el-input>
        <el-input placeholder="请输入相应的中文名" v-model="role.nameZh" size="large" style="width:220px" @keydown.enter.native="addRole"></el-input>
        <el-button type="primary" size="large" @click="addRole" :icon="Plus" >添加角色</el-button>
    </div>
    <div class="demo-collapse" style="margin-top: 10px;max-width: 800px;">
        <el-collapse accordion @change="change" v-model="activeName">
            <el-collapse-item :title="role.nameZh" :name="role.id" v-for="(role,index) in roles">
                <el-card class="box-card">
                    <template #header>
                    <div class="card-header">
                        <span>可访问资源展示</span>
                        <el-button class="button" style="float:right;color:red " :icon="Delete" @click="deleteRole(role)"></el-button>
                    </div>
                    </template>
                    <div>
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
                            <el-button type="primary" @click="doUpdate(role.id,index)">确认修改</el-button>
                        </div>
                    </div>
                </el-card>
            </el-collapse-item>
        </el-collapse>
    </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import {Plus , Delete} from '@element-plus/icons-vue'
import { deleteRequest, getRequest, postRequest, putRequest } from '@/utils/api';
import { ElMessage, ElTree,ElMessageBox } from 'element-plus';
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
const selectMenus=ref([])//用于显示原本选中哪些项
const menuDefaultProps={
    children:'children',
    label:'name'
}//用于映射后端的各项。如children属性对应返回的children。label对应返回的name字段
const deleteRole=(role)=>{

    ElMessageBox.confirm(
    '您真的要删除此「角色」吗？此操作不可撤回，请谨慎操作',
    `要删除「${role.name}」吗`,
    {
      confirmButtonText: '必须删了',
      cancelButtonText: '算了吧',
      type: 'warning',
    }
  )
    .then(() => {
        deleteRequest(`/system/basic/permiss/${role.id}`,'').then(resp=>{
            if(resp){
                ElMessage.success("删除成功");
                initRoles();
            }else{
                ElMessage.error("删除失败");
            }
        })
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '操作取消了',
      })
    })

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
const addRole=()=>{
    if(role.name && role.nameZh){
        postRequest("/system/basic/permiss/role",role).then(resp=>{
            if(resp){
                initRoles();
            }
        })
    }else{
        ElMessage.error("未完全填入新角色的信息")
    }
}
const doUpdate=(rid,index)=>{
    //console.log("所选内容:",treeRef.value[index]!.getCheckedKeys(true) );
    let selectKey=treeRef.value[index]!.getCheckedKeys(true);//若参数为false则包含了一级的菜单（包含了枝干），而数据库实际上只和二级的绑定。
    let url=`/system/basic/permiss/?rid=${rid}`;
    selectKey.forEach(key => {
        url+=`&mids=${key}`;
    });
    console.log("url:",url)
    putRequest(url,'').then(resp=>{
        if(resp){
            //initRoles();
            activeName.value='';
        }
    })

}//role对可操纵menu进行修改
const activeName=ref('2');//默认打开的是哪个，值应为name字段
</script>


<style scoped>
.demo-input-size{
    display: flex;
    justify-content: flex-start;
}
.demo-input-size .el-input{
    width:300px;
    margin-right: 15px;
}
</style>