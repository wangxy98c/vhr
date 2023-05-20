package org.javaboy.vhr.controller.sys.basic;

import org.javaboy.vhr.model.Department;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/basic/department")
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;
    @GetMapping("/")
    public List<Department> getAllDepartments(){
        //递归查询全部部门，从-1（根）开始。递归逻辑在xml的ResultMapWithChildren里
        return departmentService.geAllDepartments();
    }

    @PostMapping("/")//使用了存储过程（但其实可以不用，为了练习）。本身需要有三步（修改isparent、形成depath、以及添加）
    public RespBean addDep(@RequestBody Department dep){
        departmentService.addDep(dep);
        if(dep.getResult()==1){
            return RespBean.ok("添加成功",dep);//同时返回数据
        }
        return RespBean.error("添加失败");
        //另外：children字段不希望它是null，故在model中直接new ArrayList<>()以获得空数组（默认）
    }

    @DeleteMapping("/{id}")//存储过程
    public RespBean deleteDepById(@PathVariable Integer id){
        Department dep=new Department();
        dep.setId(id);
        departmentService.deleteDepById(dep);
        if(dep.getResult()==-2){
            return RespBean.error("删除失败，有子部门");
        }else if(dep.getResult()==-1){
            return RespBean.error("删除失败，有员工所属此部门");
        }else if(dep.getResult()==1){
            return RespBean.ok("删除成功");
        }
        return RespBean.error("删除失败，未知原因");
    }
}
