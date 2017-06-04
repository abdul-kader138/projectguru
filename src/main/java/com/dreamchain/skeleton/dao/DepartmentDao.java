package com.dreamchain.skeleton.dao;


import com.dreamchain.skeleton.model.Department;

import java.util.List;

public interface DepartmentDao {

    Department get(Long id);
    Long save(Department department);
    void update(Department department);
    void delete(Department department);
    List<Department> findAll();
    List<Department> findByCompanyName(long companyId);
    Department findByDepartmentName(String departmentName,long departmentID);
    List<Object> countOfDepartment(long departmentID);
    Department findByNewName(String CurrentName,String newName,Long id);
}
