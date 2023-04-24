<template>
    <div>
        <div>
            <el-input v-model="pos.name" clearable  @keydown.enter.native="addPosition" placeholder="添加职位..." :prefix-icon="Plus" style="max-width: 450px;">
                <!-- @keydown.enter.native=“” 按回车触发，element参考文档中无 -->
                <template #append>
                    <!-- 用template包裹且append，不然不显示 -->
                    <el-button type="primary"  :icon="Plus" @click="addPosition">添加</el-button>
                </template>
            </el-input>
            <el-table :data="posData" stripe style="width: 100%">
                <el-table-column type="selection" width="55"/>
                <el-table-column prop="id" label="编号" width="180" />
                <el-table-column prop="name" label="职位名称" width="180" />
                <el-table-column prop="createDate" label="创建时间" />
                <el-table-column label="操作">
                    <template #default="scope">
                        <el-button size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                        <el-button
                        size="small"
                        type="danger"
                        @click="handleDelete(scope.$index, scope.row)"
                        >删除</el-button
                        >
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <el-dialog v-model="dialogFormVisible" title="职位编辑名称">
            <el-form :model="posData">
                <el-form-item label="职务名称">
                    <el-input v-model="editPos.name"/>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取消编辑</el-button>
                    <el-button type="primary" @click="doEditPos()">确认修改</el-button>
                </span>
            </template>
        </el-dialog>
    </div>
</template>
<!-- template内部只能有一个div -->
<script setup lang="ts">
import { deleteRequest, getRequest, postRequest, putRequest } from '@/utils/api';
import {Plus, Search} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus';
import { reactive } from 'vue';
import { ref,onMounted } from 'vue';
import { ElMessageBox } from 'element-plus';
onMounted(()=>{
    initPositions();
})
const pos=reactive({
    name:'',
})
const editPos=reactive({
    name:'',
    id:Number
})

let dialogFormVisible = ref(false)
var posData = ref([]);
const initPositions=()=>{
    getRequest("/system/basic/pos/",'').then(resp=>{
        console.log("response:",resp);
        if(resp){
            posData.value=resp;//vue3 使用proxy，对于对象和数组都《不能直接整个赋值》监测不到.
            //ref 搭配.value使用 。。 但table中:data=“”不带.value
        }
    })
}
const addPosition=()=>{
    if(pos.name){
        postRequest("/system/basic/pos/",pos).then(resp=>{//注意此处是Json，应为pos，而不是pos.name
            if(resp){// 有后端消息回来，不一定是成功。但会有操作信息。就可以更新下视图
                initPositions();
                pos.name=''
                //成功消息会从api中弹出（msg来自后端respBean）
            }
        })
    }else{
        ElMessage.error("没有填入职务哦，亲");
    }
}
const handleDelete = (index,row) => {
  ElMessageBox.confirm(
    '您真的要删除此条信息吗？此操作不可撤回，请谨慎操作',
    `要删除「${row.name}」吗`,
    {
      confirmButtonText: '必须删了',
      cancelButtonText: '算了吧',
      type: 'warning',
    }
  )
    .then(() => {
        deleteRequest("/system/basic/pos/"+row.id,'').then(resp=>{
            if(resp){
                initPositions();
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

const handleEdit=(index,row)=>{
    dialogFormVisible.value=true//又要注意了，不用.value相当于指向了新数据。就失去了绑定。但他可在标签的@click中直接写不用.value
    console.log("row",row);
    editPos.name=row.name//reactive类型的可以直接赋
    editPos.id=row.id;
}
const doEditPos=()=>{
    putRequest("/system/basic/pos/",editPos).then(()=>{
        dialogFormVisible.value=false;
        initPositions()
    })
}

//const tableData=reactive({})
</script>