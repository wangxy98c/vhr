<template>
    <div>
        <div style="display: flex;margin-bottom: 15px;">
            <el-button :icon="Plus" style="width: 200px;" type="primary" @click="visible=true">添加工资帐套</el-button>
            <el-button type="primary" :icon="Refresh" @click="initData" text> </el-button>
        </div>
        <div>
            <el-table :data="tableData" style="width: 100%" class="tableCls">
                <el-table-column prop="name" label="帐套名称" width="100"/>
                <el-table-column prop="basicSalary" label="基本工资" width="100"/>
                <el-table-column prop="createDate" label="启用日期" width="150" />
                <el-table-column label="养老金">
                    <el-table-column prop="pensionBase" label="基数"/>
                    <el-table-column prop="pensionPer" label="比率"/>
                </el-table-column>

                <el-table-column fixed="right" label="Operations" width="120">
                    <template #default="scope">
                        <el-button link type="primary" size="small" @click="handleClick()">编辑</el-button>
                        <el-button link type="primary" size="small" @click="deleteClick(scope.$index,scope.row)" style="color:red">删除</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <div>
            <el-dialog v-model="visible" :show-close="true" width="60%" center>
                <div>
                    <el-steps :active="stepmsg.stepcnt" align-center finish-status="success">
                        <el-step :title="step" v-for="(step,index) in stepmsg.item" :key="index"/>
                    </el-steps>
                    <div >
                        <el-row 
                            v-for="(value,key,index) in stepmsg.salary"
                            v-show="stepmsg.tablemsgs[index].show==stepmsg.stepcnt"
                        >
                            <span>{{ stepmsg.tablemsgs[index].name }}</span>
                            <span>>>>>>测试：(name:{{ stepmsg.salary["name"] }}---动态的key:{{ stepmsg.salary[key] }}  )</span>
                            <el-input
                                v-model="stepmsg.salary[key]"
                                class="w-50 m-2"
                            ></el-input>
                        </el-row>
                    </div>
                </div>
                <template #footer>
                    <el-button @click="laststep">{{ stepmsg.stepcnt==0?'取消':'上一步' }}</el-button>
                    <el-button @click="nextstep" type="primary">{{ stepmsg.stepcnt==7?'完成':'下一步' }}</el-button>
                </template>
            </el-dialog>
        </div>
    </div>
</template>
<script setup lang="ts">
import { deleteRequest, getRequest, postRequest } from '@/utils/api';
import {Plus,Refresh} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus';
import { onMounted, reactive, ref } from 'vue';
onMounted(()=>{
    initData();
})
const stepmsg=reactive({
    item:[
        '基本工资','交通补助','午餐补助','奖金','养老金','医疗保险','公积金','套账名称'
    ],
    stepcnt:0,
    tablemsgs:[//需要与salary的数量与信息相对应，否则v-for salary时 会导致tablemsgs[index]越界
        {
            show:0,
            name:'基本工资',
        },
        {
            show:1,
            name:'交通补助',
        },
        {
            show:2,
            name:'午餐补助',
        },
        {
            show:3,
            name:'奖金',
        },
        {
            show:4,
            name:'养老金比例',
        },
        {
            show:4,
            name:'养老金基数',
        },
        {
            show:5,
            name:'医疗保险比例',
        },
        {
            show:5,
            name:'医疗保险基数',
        },
        {
            show:6,
            name:'公积金比例',
        },
        {
            show:6,
            name:'公积金基数',
        },
        {
            show:7,
            name:'套账名称',
        }
    ],
    salary:{
        basicSalary: 10000,
        trafficSalary: 300,
        lunchSalary: 800,
        bonus: 6000,
        pensionPer: 20,
        pensionBase: 3000,
        medicalPer: 30,
        medicalBase: 3000,
        accumulationFundPer: 15,
        accumulationFundBase: 6000,
        name: 'testname'
    }
})
const tableData=ref([])
const visible=ref(false)
const initData=()=>{
    getRequest("/salary/sob/",'').then(resp=>{
        if(resp){
            tableData.value=resp;
        }
    })
}
const handleClick=()=>{
    ElMessage.error("别点了，功能没做");
}
const deleteClick=(index,row)=>{
    console.log("~~~~~~~~",row.id);
    deleteRequest(`/salary/sob/${row.id}`,'').then(resp=>{
        if(resp){
            initData();
        }
    })
}

const laststep=()=>{
    if(stepmsg.stepcnt>=1){
        stepmsg.stepcnt=stepmsg.stepcnt-1
    }
}
const nextstep=()=>{
    if(stepmsg.stepcnt<7){
        stepmsg.stepcnt=stepmsg.stepcnt+1
        return
    }
    visible.value=false
    console.log(stepmsg.salary)
    postRequest("/salary/sob/",stepmsg.salary).then(resp=>{
        if(resp){
            initData();
        }
    })
}

</script>
<style scoped>
.tableCls table{
    text-align: center;
}
</style>