package com.dreamchain.skeleton.service;


import com.dreamchain.skeleton.model.Designation;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface DesignationService {
    Designation get(Long id);
    Map<String,Object> save(String designationName) throws Exception;
    Map<String,Object> update(Map<String, String> companyObj) throws ParseException;
    String delete(Long designationId);
    List<Designation> findAll();

}
