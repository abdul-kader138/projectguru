package com.dreamchain.skeleton.service;


import com.dreamchain.skeleton.model.Product;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ProductService {
    Product get(Long id);
    Map<String,Object> save(Map<String, Object> productObj) throws Exception;
    Map<String,Object> update(Map<String, Object> productObj) throws ParseException;
    String delete(Long departmentId);
    List<Product> findAll();
    List<Product> findByCompanyName(long companyId);
}
