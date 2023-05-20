package org.javaboy.vhr.controller.emp;

import org.javaboy.vhr.model.Department;
import org.javaboy.vhr.model.Employee;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.RespPageBean;
import org.javaboy.vhr.service.EmployeeService;
import org.javaboy.vhr.utils.POIUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/emp/basic")
public class EmpBasicController {
    @Autowired
    EmployeeService employeeService;

    @GetMapping("/")
    public RespPageBean getEmployeeByPage(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer size){
        //设置默认的参数，用于没有参数的时候
        return employeeService.getEmployeByPage(page,size);
    }
    //注意：导出
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportData(){
       List<Employee> list=(List<Employee>) employeeService.getEmployeByPage(null,null).getData();
       return POIUtils.employee2Excel(list);
    }
    @PutMapping("/import")
    public RespBean importData(MultipartFile file) throws IOException {
        //file.transferTo(new File("/Library/code/vhr/importfile.xls"));//因为处理过程相对较慢，先存起来
        List<Employee> list=POIUtils.excel2Employee(file);
//        for(Employee employee:list){
//            System.out.println(employee);
//        }//打印对象
        //调用sql，此处略去(没写添加的接口且只处理了部分字段)
        return RespBean.ok("上传成功");
    }
}
