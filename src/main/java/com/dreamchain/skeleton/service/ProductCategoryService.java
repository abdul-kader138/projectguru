package com.dreamchain.skeleton.service;


import com.dreamchain.skeleton.model.ProductCategory;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ProductCategoryService {

    ProductCategory get(Long id);
    Map<String,Object> save(String productCategoryName,String description) throws Exception;
    Map<String,Object> update(Map<String, String>  productCategoryObj) throws ParseException;
    String delete(Long productCategoryId);
    List<ProductCategory> findAll();
}
