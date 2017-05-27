package com.dreamchain.skeleton.dao;

import com.dreamchain.skeleton.model.ProductSubCategory;

import java.util.List;

public interface ProductSubCategoryDao {
    ProductSubCategory get(Long id);
    Long save(ProductSubCategory productSubCategory);
    void update(ProductSubCategory productSubCategory);
    void delete(ProductSubCategory productSubCategory);
    List<ProductSubCategory> findAll();
    ProductSubCategory findByProductSubCategoryName(String productSubCategoryName);
}
