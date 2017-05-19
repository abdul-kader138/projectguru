package com.dreamchain.skeleton.dao;

import com.dreamchain.skeleton.model.Company;

import java.util.List;

/**
 * Created by LAPTOP DREAM on 5/19/2017.
 */
public interface CompanyDao   {

    Company get(Long id);
    void save(Company user);
    void update(Company user);
    void delete(long companyId);
    List<Company> findAll();
    Company findByCompanyName(String companyName);



}


