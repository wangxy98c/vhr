package org.javaboy.vhr.service;

import org.javaboy.vhr.mapper.RoleMapper;
import org.javaboy.vhr.model.Menu;
import org.javaboy.vhr.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    RoleMapper roleMapper;
    public List<Role> getAllRoles(){
        return roleMapper.getAllRoles();
    }

    public int addRole(Role role) {
        if(!role.getName().startsWith("ROLE_")){
            role.setName("ROLE_"+role.getName());
        }//需要注意，security里的role必须有ROLE_前缀
        return roleMapper.insert(role);
    }

    public int deleteRoleById(Integer rid) {
        return roleMapper.deleteByPrimaryKey(rid);
    }
}
