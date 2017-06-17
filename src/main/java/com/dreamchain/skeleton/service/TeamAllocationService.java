package com.dreamchain.skeleton.service;

import com.dreamchain.skeleton.model.TeamAllocation;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


public interface TeamAllocationService {
    TeamAllocation get(Long id);
    Map<String,Object> save(Map<String, Object> teamAllocationObj) throws ParseException;
    Map<String,Object> update(Map<String, Object> teamAllocationObj) throws ParseException;
    String delete(Long teamAllocationObjId);
    List<TeamAllocation> findAll();
}
