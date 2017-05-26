package com.dreamchain.skeleton.dao;


import com.dreamchain.skeleton.model.ProductCategory;

import java.util.List;

public interface ProductCategoryDao {
    ProductCategory get(Long id);
    Long save(ProductCategory productCategory);
    void update(ProductCategory productCategory);
    void delete(ProductCategory productCategory);
    List<ProductCategory> findAll();
    ProductCategory findByProductCategoryName(String ProductCategoryName);
    List<Object> countOfProductCategory(long ProductCategoryID);

}
