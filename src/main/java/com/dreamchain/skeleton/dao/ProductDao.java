package com.dreamchain.skeleton.dao;


import com.dreamchain.skeleton.model.Product;

import java.util.List;

public interface ProductDao {
    Product get(Long id);
    Long save(Product product);
    void update(Product product);
    void delete(Product product);
    List<Product> findAll();
    List<Product> findByCompanyName(long companyId);
    Product findByProductName(String productName, long companyId);
    List<Object> countOfProduct(long productID);
    Product findByNewName(String CurrentName, String newName, Long companyId);
}
