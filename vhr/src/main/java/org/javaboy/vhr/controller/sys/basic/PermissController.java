package org.javaboy.vhr.controller.sys.basic;

import org.javaboy.vhr.model.Menu;
import org.javaboy.vhr.model.RespBean;
import org.javaboy.vhr.model.Role;
import org.javaboy.vhr.service.MenuService;
import org.javaboy.vhr.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/basic/permiss")
public class PermissController {
    @Autowired
    RoleService roleService;
    @Autowired
    MenuService menuService;
    @GetMapping("/")
    public List<Role> getAllRoles(){
        return roleService.getAllRoles();
    }
    @GetMapping("/menus")
    public List<Menu> getAllMenus(){
        return menuService.getAllmenus();
    }
    @GetMapping("/mids/{rid}")
    public List<Integer> getMidsByRid(@PathVariable Integer rid){
        //System.out.println("/mids/rid");
        return menuService.getMidsByRid(rid);
    }
    @PutMapping("/")
    public RespBean updateMenuRole( Integer rid,Integer[] mids){
        if(menuService.updateMenuRole(rid,mids)){
            return RespBean.ok("更新成功");
        }
        return RespBean.error("更新失败");
    }
    @PostMapping("/role")
    public RespBean addRole(@RequestBody Role role){
        System.out.println("添加角色");
        if(roleService.addRole(role)==1){
            return RespBean.ok("添加成功");
        }
        return RespBean.error("添加失败");
    }
    @DeleteMapping("/{rid}")
    public RespBean deleteRoleById (@PathVariable Integer rid){
        if(roleService.deleteRoleById(rid)==1){
            return RespBean.ok("删除成功");
        }
        return RespBean.error("删除失败");
    }
}
