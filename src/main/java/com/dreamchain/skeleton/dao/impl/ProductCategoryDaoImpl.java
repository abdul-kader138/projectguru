package com.dreamchain.skeleton.dao.impl;

import com.dreamchain.skeleton.dao.ProductCategoryDao;
import com.dreamchain.skeleton.model.Company;
import com.dreamchain.skeleton.model.ProductCategory;
import com.dreamchain.skeleton.model.ProductSubCategory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductCategoryDaoImpl implements ProductCategoryDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;


    @Override
    public ProductCategory get(Long id) {
        return hibernateTemplate.get(ProductCategory.class,id);
    }

    @Override
    public Long save(ProductCategory productCategory) {
        return (Long) hibernateTemplate.save(productCategory);
    }

    @Override
    public void update(ProductCategory productCategory) {
        hibernateTemplate.merge(productCategory);
    }

    @Override
    public void delete(ProductCategory productCategory) {
        hibernateTemplate.delete(productCategory);
    }

    @Override
    public List<ProductCategory> findAll() {
        return hibernateTemplate.loadAll(ProductCategory.class);
    }

    @Override
    public ProductCategory findByProductCategoryName(String ProductCategoryName) {
        DetachedCriteria dcr= DetachedCriteria.forClass(ProductCategory.class);
        Criterion cr = Restrictions.eq("name", ProductCategoryName);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new ProductCategory();
        return (ProductCategory)lst.get(0);
    }

    @Override
    public List<Object> countOfProductCategory(long ProductCategoryID) {
        DetachedCriteria dcr= DetachedCriteria.forClass(ProductSubCategory.class);// sub category
        Criterion cr = Restrictions.eq("product_category_id", ProductCategoryID);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new ArrayList<Object>();
        return lst;
    }
}
