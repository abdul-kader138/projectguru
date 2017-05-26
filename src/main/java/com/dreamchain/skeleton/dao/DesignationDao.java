package com.dreamchain.skeleton.dao;

import com.dreamchain.skeleton.model.Designation;

import java.util.List;


public interface DesignationDao {
    Designation get(Long id);
    Long save(Designation designation);
    void update(Designation designation);
    void delete(Designation designation);
    List<Designation> findAll();
    Designation findByDesignationName(String designationName);
    List<Object> countOfDesignation(long designationID);
}
