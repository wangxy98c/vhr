package org.javaboy.vhr.config;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CustomUrlDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        //整个函数并不返回true、false。而是判断为拒绝访问时，抛出AccessDeniedException
        for (ConfigAttribute configAttribute : configAttributes) {
            String needRole = configAttribute.getAttribute();//拿到'请求地址'所需要的角色
            if("ROLE_LOGIN".equals(needRole)){
                if(authentication instanceof AnonymousAuthenticationToken){
                    System.out.println("--没登陆");
                    throw new AccessDeniedException("尚未登陆，请登录");
                }else{
                    System.out.println("--登陆就行");
                    return;//登陆了，不用管，访问就行
                }
            }
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();//获得'登陆用户'的角色
            for (GrantedAuthority authority : authorities) {
                //此项目设计：有其中一个角色即可。（其他项目也可能是需要有所有角色）
                if(authority.getAuthority().equals(needRole)){
                    System.out.println("--有对应角色");
                    return ;
                }
            }
        }
        System.out.println("--没权限");
        throw new AccessDeniedException("没有对应权限，请使用其他账号或联系管理员");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
