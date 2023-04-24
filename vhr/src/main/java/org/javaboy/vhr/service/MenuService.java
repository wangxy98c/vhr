package org.javaboy.vhr.service;


import org.javaboy.vhr.mapper.MenuMapper;
import org.javaboy.vhr.model.Hr;
import org.javaboy.vhr.model.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class MenuService {
    @Autowired
    MenuMapper menuMapper;
    public List<Menu> getMenusByHrId(){
        //((Hr)SecurityContextHolder.getContext().getAuthentication().getPrincipal()) 拿到当前角色的信息（前端不可信）并强制转化
        return menuMapper.getMenusByHrId( ((Hr)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId() );
    }

    //@Cacheable,用Redis进行缓存减少查询次数。暂时不开启
    public List<Menu> getALlMenuWithRole(){
        return menuMapper.getAllMenusWithRole();
    }
}
