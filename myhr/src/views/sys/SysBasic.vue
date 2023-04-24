<template>
    
    <el-tabs
        v-model="activeName"
        type="border-card"
        class="demo-tabs"
        @tab-click="handleClick"
    >
        <el-tab-pane label="部门管理" name="first">
            <DepMana></DepMana>
        </el-tab-pane>
        <el-tab-pane label="职位管理" name="second">
            <PosMana></PosMana>
        </el-tab-pane>
        <el-tab-pane label="职称管理" name="third">
            <JobLevelMana></JobLevelMana>
        </el-tab-pane>
        <el-tab-pane label="奖惩管理" name="fourth">
            <EcMana></EcMana>
        </el-tab-pane>
        <el-tab-pane label="权限组" name="dd">
            <PermissMana></PermissMana>
        </el-tab-pane>
    </el-tabs>
</template>
<script lang="ts" setup>
import DepMana from '@/components/sys/basic/DepMana.vue';
import EcMana from '@/components/sys/basic/EcMana.vue';
import JobLevelMana from '@/components/sys/basic/JobLevelMana.vue';
import PermissMana from '@/components/sys/basic/PermissMana.vue';
import PosMana from '@/components/sys/basic/PosMana.vue';
import { ref } from 'vue'
import type { TabsPaneContext } from 'element-plus'


const activeName = ref('first')

const handleClick = (tab: TabsPaneContext, event: Event) => {
console.log(tab, event)
}
</script>
<style>
.demo-tabs > .el-tabs__content {
padding: 32px;
color: #6b778c;
font-size: 32px;
font-weight: 600;
}
</style>
