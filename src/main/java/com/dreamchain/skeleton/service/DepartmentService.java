package com.dreamchain.skeleton.service;

import com.dreamchain.skeleton.model.Department;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface DepartmentService {

    Department get(Long id);
    Map<String,Object> save(Map<String, Object> departmentObj) throws Exception;
    Map<String,Object> update(Map<String, Object> departmentObj) throws ParseException;
    String delete(Long departmentId);
    List<Department> findAll();
    List<Department> findByCompanyName(long companyId);
}
