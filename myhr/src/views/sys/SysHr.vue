<template>
    <div>
        <div style="margin-top: 20px;display: flex;justify-content: center;">
            <el-input v-model="keywords" placeholder="通过用户名搜索用户" :prefix-icon="Search" style="width:400px; margin-right: 10px;" @keydown.enter.native="initHrs"></el-input>
            <el-button :icon="Search" type="primary" @click="initHrs">搜索</el-button>
        </div>
        <div style="display: flex;flex-wrap: wrap;">
            <el-card class="box-card" v-for="(hr,index) in hrs" :key="index">
                <template #header>
                    <div class="card-header">
                        <span>{{ hr.name }}</span>
                        <el-button text :icon="Delete" style="color: red;" @click="deleteHr(hr.id)"></el-button>
                    </div>
                </template>
                <div>
                    <div style="width: 100vw; display: flex; justify-content: center;">
                        <img :src="hr.userface" :alt="hr.name" :title="hr.name" class="userfaceImg">
                    </div>
                    <div class="userinfo" style="margin-top: 10px;">
                        <div> 用户名：{{ hr.name }} </div>
                        <div> 手机号：{{ hr.phone }} </div>
                        <div> 电话号：{{ hr.telephone }} </div>
                        <div> 地址：{{ hr.address }} </div>
                        <div> 用户状态：<el-switch v-model="hr.enabled" inactive-text="禁用" active-text="启用" @change="enableChange(hr)"/></div>
                    </div>
                    <div> 用户角色
                        <el-tag class="ml-2" type="success" v-for="(role,indexj) in hr.roles" :key="indexj" style="margin-right: 3px;">{{ role.nameZh }}</el-tag>
                        
                        <el-popover
                            placement="right"
                            title="角色列表"
                            :width="240"
                            trigger="click"
                            @show="showpop(hr.roles)"
                            @hide="hidepop(hr.id)"
                        >
                            <el-select
                                v-model="selectRoles"
                                multiple
                                placeholder="Select"
                                style="width: 220px"
                                :teleported="false"
                            >
                                <el-option
                                    v-for="(role,index) in allroles"
                                    :key="index"
                                    :label="role.nameZh"
                                    :value="role.id"
                                />
                            </el-select>

                            <template #reference>
                                <el-button :icon="More" text></el-button>
                            </template>
                        </el-popover>
                        
                        
                    </div>
                    
                </div>
            </el-card>
        </div>
    </div>
</template>
<script setup lang="ts">
import { onMounted, ref } from 'vue';
import {Search,More,Delete} from '@element-plus/icons-vue'
import { getRequest ,putRequest ,deleteRequest} from '@/utils/api';

onMounted(()=>{
    initHrs();
    initAllRoles();
})
const keywords=ref('')
const hrs=ref([])
const initHrs=()=>{
    //console.log('keywords',keywords.value)
    getRequest(`/system/hr/?keywords=${keywords.value}`,'').then(resp=>{
        if(resp){
            hrs.value=resp;
            //console.log('resp',resp)
        }
    })
}
const enableChange=(hr:any)=>{
    putRequest("/system/hr/",hr).then(resp=>{
        if(resp){
            initHrs();
        }
    })
    //console.log(hr);
}
const initAllRoles=()=>{
    getRequest("/system/hr/roles",'').then(resp=>{
        if(resp){
            allroles.value=resp;
        }
    })
}
const selectRoles = ref([])
const showpop=(hrroles)=>{
    selectRoles.value=[]
    hrroles.forEach(role => {
        selectRoles.value.push(role.id);
    });
}
const hidepop=(hrid)=>{
    let url=`/system/hr/role?hrid=${hrid}`
    selectRoles.value.forEach(sr => {
        url+=`&rids=${sr}`
    });
    //偷个懒，没有判断实际是否进行了更新。直接进行覆盖。
    putRequest(url,'').then(resp=>{
        if(resp){
            initHrs();
        }
    })
}
const deleteHr=(hrid)=>{//偷懒了，没有确认框。
    deleteRequest(`/system/hr/${hrid}`,'').then(resp=>{
        if(resp){
            initHrs();
        }
    })
}
const allroles=ref([])


</script>
<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 350px;
  margin-top: 10px;
  margin-bottom: 10px;
  margin-left: 10px;
  margin-right: 10px;
}
.userfaceImg{
    width: 72px;
    height: 72px;
    border-radius: 72px;
}
.userinfo div{
    font-size: 12px;
    color:#409eff
}
.el-button + .el-button {
  margin-left: 8px;
}
</style>