package com.dreamchain.skeleton.service;


import com.dreamchain.skeleton.model.UserAllocation;

import java.text.ParseException;
import java.util.List;
import java.util.Map;



public interface UserAllocationService {

    UserAllocation get(Long id);
    Map<String,Object> save(Map<String, Object> userAllocationObj) throws ParseException;
    Map<String,Object> update(Map<String, Object> userAllocationObj) throws ParseException;
    String delete(Long userAllocationId);
    List<UserAllocation> findAll();
}
