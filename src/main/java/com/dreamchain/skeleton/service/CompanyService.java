package com.dreamchain.skeleton.service;

import com.dreamchain.skeleton.model.Company;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


public interface CompanyService {

    Company get(Long id);
    Map<String,Object> save(MultipartHttpServletRequest multipartHttpServletRequest) throws Exception;
    Map<String,Object> update(MultipartHttpServletRequest multipartHttpServletRequest) throws Exception;
    String delete(Long companyId,HttpServletRequest request);
    List<Company> findAll();

}
