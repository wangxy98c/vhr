<template>
<el-input v-model="filterText" placeholder="请输入需要搜索的部门名称" :prefix-icon="Search" clearable/>

<el-tree
    ref="treeRef"
    class="filter-tree"
    :data="departments"
    :props="defaultProps"
    :expand-on-click-node="false"
    :filter-node-method="filterNode"
>
    <template #default="{ node, data }">
        <span class="custom-tree-node">
            <span>{{ node.label }}</span>
            <span>
                <el-button @click="append(data)" size="small"> 添加 </el-button>
                <el-button style="margin-left: 8px" @click="remove(data)" size="small" type="danger"> 删除 </el-button>
            </span>
        </span>
    </template>
</el-tree>
<!-- 添加部门的dialog -->
<el-dialog v-model="dialogFormVisible" title="添加新部门">
    <el-form :model="dep">
        <el-form-item label="上级部门" label-width="80px">{{ pdep.name }}</el-form-item>
      <el-form-item label="部门名称" label-width="80px">
        <el-input v-model="dep.name"  placeholder="请输入要添加的新部门的名称" />
      </el-form-item>
    </el-form>
    <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="doAddDep">
          确定
        </el-button>
      </span>
    </template>
  </el-dialog>
</template>

<script lang="ts" setup>
import { onMounted, ref, watch } from 'vue'
import { reactive } from 'vue'
import { ElTree } from 'element-plus'
import { getRequest ,postRequest, deleteRequest} from '@/utils/api'
import {Search} from '@element-plus/icons-vue'
import { ElMessageBox ,ElMessage} from 'element-plus'
import type Node from 'element-plus/es/components/tree/src/model/node'
onMounted(()=>{
initDeps();
})
const filterText = ref('')
const treeRef = ref<InstanceType<typeof ElTree>>()
const defaultProps = {
children: 'children',
label: 'name',
}
watch(filterText, (val) => {
treeRef.value!.filter(val)//调用 :filter-node-method="xx"。即filterNode
})
interface Tree {
id: number
label: string
children?: Tree[]
}
const filterNode = (value: string, data: Tree) => {
if (!value) return true
//return data.label.includes(value) 。。。element-plus样例代码
//data是el-tree中:data绑定的数据（自己的没有label字段，故改为name）。value是搜索的值
return data.name.includes(value)
}

const departments=ref([]);
const initDeps=()=>{
getRequest("/system/basic/department/",'').then(resp=>{
    if(resp){
        departments.value=resp;
    }  
})
}

const pdep=reactive({
    name:''//记录当前在哪个部门下添加
})
const dep=reactive({
    name:'',
    parentId: -1,
})
const append = (data: Tree) => {
    console.log("append:",data);
    pdep.name=data.name;
    dep.parentId=data.id
    dialogFormVisible.value=true;
}

const remove = ( data: Tree) => {
    console.log("remove:",data);
    if(data.isParent){
        ElMessage.error("父部门不可删除")
        return;
    }
    ElMessageBox.confirm(
    '您真的要删除此「部门」吗？此操作不可撤回，请谨慎操作',
    `要删除「${data.name}」吗`,
    {
      confirmButtonText: '必须删了',
      cancelButtonText: '算了吧',
      type: 'warning',
    }
  )
    .then(() => {
        deleteRequest(`/system/basic/department/${data.id}`,'').then(resp=>{
            if(resp){
                ElMessage.success("删除成功");
                initDeps()//偷懒,优化 可参照addDep2departments
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
const addDep2departments=(departs,obj:Object)=>{
    for(let i=0;i<departs.length;i++){
        let d=departs[i]
        if(d.id==obj.parentId){
            d.children.push(obj)
            return
        }else{
            addDep2departments(d.children,obj)
        }
    }
}
const doAddDep=()=>{
    //console.log("测：",departments.value[0].name)
    postRequest("/system/basic/department/",dep).then(resp=>{
        if(resp){
            //initDeps();
            console.log("parentId:",resp.parentId);
            addDep2departments(departments.value,resp.obj)//注意返回的resp里的对象应该是.obj
            dialogFormVisible.value=false;
            dep.name=''
            dep.parentId=-1
        }
    }) 
}
const dialogFormVisible = ref(false)

</script>

<style scoped>
.custom-tree-node {
flex: 1;
display: flex;
align-items: center;
justify-content: space-between;
font-size: 14px;
padding-right: 8px;
}

.dialog-footer button:first-child {
  margin-right: 10px;
}
</style>