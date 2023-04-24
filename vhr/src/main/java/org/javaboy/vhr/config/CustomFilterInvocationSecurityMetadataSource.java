package org.javaboy.vhr.config;

import org.javaboy.vhr.model.Menu;
import org.javaboy.vhr.model.Role;
import org.javaboy.vhr.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

//权限访问控制：
//前端请求，根据请求地址判断所对应的菜单项，再根据菜单项找到能访问的角色。
//后段再查询用户是否具有这些角色

@Component
public class CustomFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    MenuService menuService;
    AntPathMatcher pathMatcher=new AntPathMatcher();//字符串比对工具
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        //分析请求地址需要哪些角色

        String requestUrl = ((FilterInvocation) object).getRequestUrl();//拿到请求地址
        List<Menu> menus = menuService.getALlMenuWithRole();//每次都会查询且变化不大，可以优化(在Service里)
        for (Menu menu:menus){
            //第一个参数是标准[数组]，第二个参数是要和标准对比的串。不要写反
            if(pathMatcher.match(menu.getUrl(),requestUrl)){
                List<Role> roles = menu.getRoles();
                String[] str=new String[roles.size()];
                for (int i=0;i< roles.size();i++) {
                    str[i]=roles.get(i).getName();
                }
                System.out.println("返回了所需角色");
                //注意是springSecurity中的SecurityConfig。不是自己写的。参数是一个数组,故不能直接用roles
                return SecurityConfig.createList(str);
            }
        }
        //System.out.println("没匹配上");
        //没匹配上（不知道什么路径，可能是数据库中没记录的页面），登陆即可访问
        return SecurityConfig.createList("ROLE_LOGIN");//"R_L"只是一个标记，后续进行处理
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;//true
    }
}
