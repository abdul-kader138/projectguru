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
    Company findByNewName(String CurrentName,String newName);
    List<Object> countOfCompany(long companyID);
    List<Object> countOfCompanyForProduct(long companyID);



}


