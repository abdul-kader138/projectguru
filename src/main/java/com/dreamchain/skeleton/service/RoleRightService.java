package com.dreamchain.skeleton.service;

import com.dreamchain.skeleton.model.RoleRight;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


public interface RoleRightService {
    RoleRight get(Long id);
    Map<String,Object> save(Map<String,Object> roleObj) throws Exception;
    Map<String,Object> update(Map<String, Object>  roleObj) throws ParseException;

    String delete(Long roleId);
    List<RoleRight> findAll();
}
