package com.dreamchain.skeleton.service;


import com.dreamchain.skeleton.model.Category;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface CategoryService {

    Category get(Long id);
    Map<String,Object> save(Map<String, Object> categoryObj) throws ParseException;
    Map<String,Object> update(Map<String, Object> categoryObj) throws ParseException;
    String delete(Long categoryId);
    List<Category> findAll();
    List<Category> findByProductId(long productId);
}
