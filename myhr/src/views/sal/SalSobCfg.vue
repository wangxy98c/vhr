<template>
    <div>
        <el-table :data="empsalary"  style="width: 100%;">
            <el-table-column prop="id" label="编号" width="60" />
            <el-table-column prop="name" label="姓名" width="120" />
            <el-table-column prop="gender" label="性别" width="70" />
            <el-table-column prop="birthday" label="出生日期" width="110" />
            <el-table-column label="所属工资帐套">
                <template #default="scope">
                    <!-- 带name的插槽被称为具名插槽 (named slots)。没有提供 name 的 <slot> 出口会隐式地命名为“default”。简写为 # -->
                    <el-tooltip placement="right" effect="light">
                        <template #content>  
                            基础工资:{{ scope.row.salary.basicSalary }}<br/>
                            养老金:{{ scope.row.salary.pensionBase}}<br/>
                            餐费:{{ scope.row.salary.lunchSalary}}<br/>
                            交通补贴:{{ scope.row.salary.trafficSalary}}<br/>
                            医疗保险:{{ scope.row.salary.medicalBase}}<br/>
                        </template>
                        <el-button>{{scope.row.salary.name}}</el-button>
                    </el-tooltip>
                </template>
            </el-table-column>
            <!-- 实际有更多字段，不想写了 -->
            <el-table-column label="操作" width="80">
                <template #default>
                        <el-button @click="handleClick" :icon="Delete" text size="small" style="margin: 0px;padding: 5px;color: red;"></el-button>
                        <el-button :icon="Edit" @click="handleClick" text size="small" style="margin:0px;padding: 5px;color:blue"></el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination layout="prev, pager, next" :total="page.total" v-model:page-size="page.size" v-model:current-page="page.cnt" @current-change="initEmpsalary"/>
    </div>
</template>
<script setup lang="ts">
import { getRequest } from '@/utils/api';
import { onMounted, reactive, ref } from 'vue';
import {Edit,Delete} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox, stepProps } from 'element-plus';
onMounted(()=>{
    initEmpsalary()
})
const empsalary=ref([])
const page=reactive({
    total:1,
    size:10,
    cnt:1
})
const initEmpsalary=()=>{
    getRequest(`/sob/config/?page=${page.cnt}&size=${page.size}`,'').then(resp=>{
        if(resp){
            empsalary.value=resp.data;
            page.total=resp.total;
        }
    })
}
const handleClick=()=>{
    ElMessage.error("功能没做，点了没用");
}
</script>