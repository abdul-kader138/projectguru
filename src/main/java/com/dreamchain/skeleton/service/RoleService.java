package com.dreamchain.skeleton.service;

import com.dreamchain.skeleton.model.Role;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


public interface RoleService {
    Role get(Long id);
    Map<String,Object> save(Map<String,String> roleObj) throws Exception;
    Map<String,Object> update(Map<String, String>  roleObj) throws ParseException;
    String delete(Long roleId);
    List<Role> findAll();
}
