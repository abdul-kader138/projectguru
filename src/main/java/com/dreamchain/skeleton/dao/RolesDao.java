package com.dreamchain.skeleton.dao;


import com.dreamchain.skeleton.model.Roles;

import java.util.List;

public interface RolesDao {
    Roles get(Long id);
    Long save(Roles roles);
    void update(Roles roles);
    void delete(Roles roles);
    List<Roles> findAll();
    Roles findByRolesName(String rolesName);
    List<Object> countOfRoles(long rolesId);
    Roles findByNewName(String CurrentName,String newName);

}

