package com.dreamchain.skeleton.service;

import com.dreamchain.skeleton.model.Company;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


public interface CompanyService {

    Company get(Long id);
    Map<String,Object> save(String companyName,String address) throws Exception;
    Map<String,Object> update(Map<String, String>  companyObj) throws ParseException;
    String delete(Long companyId);
    List<Company> findAll();

}
