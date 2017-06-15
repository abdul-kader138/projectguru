package com.dreamchain.skeleton.dao;


import com.dreamchain.skeleton.model.RoleRight;

import java.util.List;

public interface RoleRightDao {

    RoleRight get(Long id);
    Long save(RoleRight roleRight);
    void update(RoleRight roleRight);
    void delete(RoleRight roleRight);
    List<RoleRight> findAll();
    List<Object> countOfRights(long rightsId);
    RoleRight findByRolesName(long roleId);
    RoleRight findByNewName(long currentRoleId,long newRoleId);


}

