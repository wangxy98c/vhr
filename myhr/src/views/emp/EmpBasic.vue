<template>
    <div>
        <div>
            <el-dialog
                v-model="dialogVisible"
                title="xls文件上传"
                width="30%"
            >
                <span>
                    <el-upload
                        ref="upload"
                        class="upload-demo"
                        action="/api/emp/basic/import"
                        method="put"
                        :limit="1"
                        :on-exceed="handleExceed"
                        :on-success="successHandle"
                        :on-error="errorHandle"
                        accept=".xls"
                        :auto-upload="false"
                        v-loading="loading"
                    >
                        <template #trigger>
                        <el-button type="primary">选择xls文件</el-button>
                        </template>
                        <el-button class="ml-3" type="success" @click="submitUpload" style="margin-left: 20px;">
                        点此上传
                        </el-button>
                        <template #tip>
                        <div class="el-upload__tip text-red">
                            每次仅能上传一个文件，后续文件将覆盖前一文件
                        </div>
                        </template>
                    </el-upload>
                </span>
                <template #footer>
                    <span class="dialog-footer">
                        <el-button @click="dialogVisible = false">取消上传</el-button>
                        <!-- <el-button type="primary" @click="dialogVisible = false">确认上传</el-button> -->
                    </span>
                </template>
            </el-dialog>
        </div>
        <div>
            <h1>偷懒，主要做一下《导入》《导出》功能.且导入功能未实际添加到数据库（没写添加接口）</h1>
            <span>
                <el-button :icon="Upload" style="background-color:aquamarine" @click="dialogVisible=true">导入数据</el-button>
                <el-button :icon="Download" style="background-color:aquamarine" @click="exportData">导出数据</el-button>
            </span>
        </div>
        <div>
            <el-table :data="emps"  style="width: 100%;">
                <el-table-column fixed prop="id" label="编号" width="60" />
                <el-table-column prop="name" label="姓名" width="120" />
                <el-table-column prop="gender" label="性别" width="70" />
                <el-table-column prop="birthday" label="出生日期" width="110" />
                <el-table-column prop="address" label="地址" width="200" />
                <el-table-column prop="specialty" label="学科" width="170" />
                <!-- 实际有更多字段，不想写了 -->
                <el-table-column label="操作" width="80">
                    <template #default>
                            <el-button @click="handleClick" :icon="Delete" text size="small" style="margin: 0px;padding: 5px;color: red;"></el-button>
                            <el-button :icon="Edit" @click="handleClick" text size="small" style="margin:0px;padding: 5px;color:blue"></el-button>
                    </template>
                </el-table-column>
            </el-table>
            <el-pagination layout="prev, pager, next" :total="page.total" v-model:page-size="page.size" v-model:current-page="page.cnt" @current-change="initEmps"/>
        </div>
    </div>
</template>

<script setup lang="ts">
import { getRequest } from '@/utils/api';
import {Upload,Download,Delete,Edit} from '@element-plus/icons-vue'
import { onMounted, reactive, ref } from 'vue';
import { ElMessage, ElMessageBox, genFileId } from 'element-plus';
import type { UploadInstance, UploadProps, UploadRawFile } from 'element-plus'
const dialogVisible=ref(false);
const emps=ref([]);
const page=reactive({
    total:1,
    cnt:1,
    size:10,
})
onMounted(()=>{
    initEmps();
})
const initEmps=()=>{
    getRequest(`/emp/basic/?page=${page.cnt}&size=${page.size}`,'').then(resp=>{
        if(resp){
            emps.value=resp.data;
            page.total=resp.total;
            
        }
    })
}
//导出数据下载
const exportData=()=>{
    window.open("/api/emp/basic/export/",'_parent');//第二个参数表示在哪里打开。注意要带/api 因为需要node代理转发
    console.log("exportData...")

}
const handleClick=()=>{
    ElMessage.error("偷懒，功能没做，不用试了")
}
const upload = ref<UploadInstance>()
const handleExceed: UploadProps['onExceed'] = (files) => {
  upload.value!.clearFiles()
  const file = files[0] as UploadRawFile
  file.uid = genFileId()
  upload.value!.handleStart(file)
}
const loading=ref(false)
const submitUpload = () => {
    loading.value=true;
  upload.value!.submit()
  console.log('file has submit')
}
const successHandle=()=>{
    ElMessage.success("上传成功");
    upload.value!.clearFiles()
    loading.value=false;
    dialogVisible.value=false
}
const errorHandle=()=>{
    ElMessage.error("上传失败了")
    loading.value=false;
    dialogVisible.value=false
}
</script>
<style scoped>
</style>