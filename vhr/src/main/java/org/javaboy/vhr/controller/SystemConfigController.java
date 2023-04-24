package org.javaboy.vhr.controller;

import org.javaboy.vhr.model.Menu;
import org.javaboy.vhr.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//系统动态配置功能
@RestController//相当于@ResponseBody和@Controller的结合
@RequestMapping("/system/config")//接受所有类型请求
public class SystemConfigController {
    @Autowired
    MenuService menuService;
    @GetMapping("/menu")//get类型请求
    public List<Menu> getMenuById(){
        System.out.println("前台请求了一次菜单");
        return menuService.getMenusByHrId();
    }
}
