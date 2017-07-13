package com.dreamchain.skeleton.dao.impl;


import com.dreamchain.skeleton.dao.ProductDao;
import com.dreamchain.skeleton.model.Product;
import com.dreamchain.skeleton.model.User;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@PropertySource("classpath:config.properties")
public class ProductDaoImpl implements ProductDao {
    @Autowired
    Environment environment;
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public Product get(Long id) {
        return hibernateTemplate.get(Product.class, id);
    }

    @Override
    public Long save(Product product) {
        return (Long) hibernateTemplate.save(product);
    }

    @Override
    public void update(Product product) {
        hibernateTemplate.merge(product);

    }

    @Override
    public void delete(Product product) {
        hibernateTemplate.delete(product);
    }

    @Override
    public List<Product> findAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user=(User)auth.getPrincipal();
        DetachedCriteria dcr= DetachedCriteria.forClass(Product.class);
        Criterion cr = Restrictions.eq("clientId", user.getClientId());
        if (! environment.getProperty("company.vendor.id").equals(user.getClientId())) dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        return createProductList(lst);
    }

    @Override
    public List<Product> findByCompanyName(long companyId) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Product.class);
        Criterion cr = Restrictions.eq("company.id", companyId);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        return createProductList(lst);
    }

    @Override
    public Product findByProductName(String productName, long companyId) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Product.class);
        Criterion cr = Restrictions.eq("name", productName.trim()).ignoreCase();
        Criterion cr1 = Restrictions.eq("company.id", companyId);
        dcr.add(cr);
        dcr.add(cr1);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new Product();
        return (Product)lst.get(0);
    }


    @Override
    public Product findByNewName(String CurrentName, String newName, Long companyId) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Product.class);
        Criterion cr = Restrictions.eq("name", newName.trim()).ignoreCase();
        Criterion cr1 = Restrictions.ne("name", CurrentName.trim()).ignoreCase();
        Criterion cr2 = Restrictions.eq("company.id", companyId);
        dcr.add(cr);
        dcr.add(cr1);
        dcr.add(cr2);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new Product();
        return (Product)lst.get(0);
    }

    private List<Product> createProductList(List<Object> productList){
        List<Product> list = new ArrayList<>();
        for(final Object o : productList) {
            list.add((Product)o);
        }
        return list;
    }
}
