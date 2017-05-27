package com.dreamchain.skeleton.service;


import com.dreamchain.skeleton.model.ProductSubCategory;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface ProductSubCategoryService {
    ProductSubCategory get(Long id);
    Map<String,Object> save(String productSubCategoryName,String description, long productCategoryId) throws Exception;
    Map<String,Object> update(Map<String, String>  productSubCategoryObj) throws ParseException;
    String delete(Long projectId);
    List<ProductSubCategory> findAll();
}
