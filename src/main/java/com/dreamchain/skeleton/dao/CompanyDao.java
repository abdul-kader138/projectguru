package com.dreamchain.skeleton.dao;

import com.dreamchain.skeleton.model.Company;

import java.util.List;


public interface CompanyDao   {

    Company get(Long id);
    Long save(Company company);
    void update(Company company);
    void delete(Company company);
    List<Company> findAll();
    Company findByCompanyName(String companyName);
    List<Object> countOfCompany(long companyID);



}


