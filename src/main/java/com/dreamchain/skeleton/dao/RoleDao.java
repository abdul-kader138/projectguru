package com.dreamchain.skeleton.dao;


import com.dreamchain.skeleton.model.Role;

import java.util.List;

public interface RoleDao {

    Role get(Long id);
    Long save(Role role);
    void update(Role role);
    void delete(Role role);
    List<Role> findAll();
    Role findByRoleName(String roleName);
    List<Object> countOfRole(long roleID);

}
