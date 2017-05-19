package com.dreamchain.skeleton.service;

import com.dreamchain.skeleton.model.Company;

import java.util.List;

/**
 * Created by LAPTOP DREAM on 5/19/2017.
 */
public interface CompanyService {

    Company get(Long id);
    String save(String companyName) throws Exception;
    void update(Company company);
    void delete(long companyId);
    Company findByCompanyName(String companyName);
    List<Company> findAll();

}
