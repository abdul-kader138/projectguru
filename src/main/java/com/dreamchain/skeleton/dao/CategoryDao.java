package com.dreamchain.skeleton.dao;


import com.dreamchain.skeleton.model.Category;

import java.util.List;

public interface CategoryDao {
    Category get(Long id);
    Long save(Category category);
    void update(Category category);
    void delete(Category category);
    List<Category> findAll();
    Category findByProductName(String categoryName, long companyId,long departmentId,long productId);
    List<Object> countOfProduct(long productID);
    Category findByNewName(String CurrentName, String newName, Long companyId,Long departmentId,long productId);
    List<Category> findByProductId(long productId);
}
