package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.EmployeeMapper;
import org.javaboy.vhr.model.Employee;
import org.javaboy.vhr.model.RespPageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    EmployeeMapper employeeMapper;
    @Autowired
    RabbitTemplate rabbitTemplate;

    public final static Logger logger= LoggerFactory.getLogger(EmployeeService.class);

    public RespPageBean getEmployeByPage(Integer page, Integer size) {
        if(page!=null && size!=null){
            page=(page-1)*size;
        } 
        List<Employee> data=employeeMapper.getEmployeeByPage(page,size);
        Long total = employeeMapper.getTotal();
        RespPageBean respPageBean = new RespPageBean();
        respPageBean.setData(data);
        respPageBean.setTotal(total);
        return respPageBean;
    }

    public Integer addEmp(Employee employee) {
        int result = employeeMapper.insertSelective(employee);
        if(result==1){
            //  mapper进行了组件回填
            Employee emp = employeeMapper.getEmployeeById(employee.getId());

            logger.info(emp.toString());
            //与启动类中的List对应
            rabbitTemplate.convertAndSend("javaboy.mail.welcome",emp);
        }
        return result;

    }
}
