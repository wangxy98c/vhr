package org.javaboy.vhr.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//RestController的作用相当于Controller加ResponseBody共同作用的结果，但采用RestController请求方式一般会采用Restful风格的形式
//        Controller的作用：声明该类是Controller层的Bean，将该类声明进入Spring容器中进行管理
//        ResponseBody的作用：表明该类的所有方法的返回值都直接进行提交而不经过视图解析器，且返回值的数据自动封装为json的数据格式
public class HelloController {
    @GetMapping("/hello")
    public String Hello(){
        System.out.printf("/hello");
        return "hello  you have in";
    }

    @GetMapping("/employee/basic/hello")
    public String hello2(){
        System.out.println("basic/hello");
        return "/employee/basic/hello";
    }
    @GetMapping("/employee/advanced/hello")
    public String hello3(){
        System.out.println("advanced/hello");
        return "/employee/advanced/hello";
    }
}
