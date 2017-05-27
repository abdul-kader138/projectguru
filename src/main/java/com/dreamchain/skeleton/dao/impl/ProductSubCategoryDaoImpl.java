package com.dreamchain.skeleton.dao.impl;


import com.dreamchain.skeleton.model.ProductSubCategory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class ProductSubCategoryDaoImpl implements com.dreamchain.skeleton.dao.ProductSubCategoryDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public ProductSubCategory get(Long id) {
        return hibernateTemplate.get(ProductSubCategory.class, id);
    }

    @Override
    public Long save(ProductSubCategory productSubCategory) {
        return (Long) hibernateTemplate.save(productSubCategory);
    }

    @Override
    public void update(ProductSubCategory productSubCategory) {
        hibernateTemplate.merge(productSubCategory);
    }

    @Override
    public void delete(ProductSubCategory productSubCategory) {
        hibernateTemplate.delete(productSubCategory);
    }

    @Override
    public List<ProductSubCategory> findAll() {
        return hibernateTemplate.loadAll(ProductSubCategory.class);
    }

    @Override
    public ProductSubCategory findByProductSubCategoryName(String productSubCategoryName) {
        DetachedCriteria dcr = DetachedCriteria.forClass(ProductSubCategory.class);
        Criterion cr = Restrictions.eq("name", productSubCategoryName);
        dcr.add(cr);
        List<Object> lst = hibernateTemplate.findByCriteria(dcr);
        if (lst.size() == 0) return new ProductSubCategory();
        return (ProductSubCategory) lst.get(0);
    }
}
