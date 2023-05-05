package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.DepartmentMapper;
import org.javaboy.vhr.model.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    @Autowired
    DepartmentMapper departmentMapper;
    public List<Department> geAllDepartments() {
        return departmentMapper.getAllDepartmentsByParentId(-1);//-1是董事会(根结点)
        //懒加载 、 以及递归查询  代码在xml文件里。resultMapWithChildren。但需要 fetchType="lazy"
//        <collection property="children" ofType="org.javaboy.vhr.model.Department"
//        select="org.javaboy.vhr.mapper.DepartmentMapper.getAllDepartmentsByParentId" column="id" fetchType="lazy"/>
    }

    public void addDep(Department dep) {
        dep.setEnabled(true);//必须设置，dep可能没设置此字段
        departmentMapper.addDep(dep);
    }

    public void deleteDepById(Department dep) {
        departmentMapper.deleteDepById(dep);
    }
}
