package com.dreamchain.skeleton.service;

import com.dreamchain.skeleton.model.Company;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.text.ParseException;
import java.util.List;
import java.util.Map;


public interface CompanyService {

    Company get(Long id);
    Map<String,Object> save(MultipartHttpServletRequest multipartHttpServletRequest) throws Exception;
    Map<String,Object> update(Map<String, String>  companyObj) throws ParseException;
    String delete(Long companyId);
    List<Company> findAll();

}
