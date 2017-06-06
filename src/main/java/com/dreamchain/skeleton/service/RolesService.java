package com.dreamchain.skeleton.service;


import com.dreamchain.skeleton.model.Roles;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface RolesService {

    Roles get(Long id);
    Map<String,Object> save(Map<String, Object>  rolesObj) throws Exception;
    Map<String,Object> update(Map<String, Object>  rolesObj) throws ParseException;
    String delete(Long rolesId);
    List<Roles> findAll();
}

