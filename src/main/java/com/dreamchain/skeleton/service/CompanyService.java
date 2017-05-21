package com.dreamchain.skeleton.service;

import com.dreamchain.skeleton.model.Company;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * Created by LAPTOP DREAM on 5/19/2017.
 */
public interface CompanyService {

    Company get(Long id);
    Map<String,Object> save(String companyName,String address) throws Exception;
    Map<String,Object> update(Company company) throws ParseException;
    String delete(Long companyId);
    Company findByCompanyName(String companyName);
    List<Company> findAll();

}
