package org.javaboy.vhr.controller;

import org.javaboy.vhr.model.RespBean;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
    @GetMapping("/login")
    //@CrossOrigin("*")//此处注解解决跨域（但是是特殊情况下产生的跨域问题，一个用户未登陆即访问其他页面
    //会产生重定向，而此重定向直接请求，不会经过node。故而在前端的配置不会生效。
    //但不推荐此方法，更好的办法是没有登陆不要重定向，而返回"没有登陆"的信息（参见实际代码）
    public RespBean login(){
        System.out.println("/login");
        return RespBean.error("尚未登陆，请登录");
    }
}
