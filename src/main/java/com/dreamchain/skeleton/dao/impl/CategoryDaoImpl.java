package com.dreamchain.skeleton.dao.impl;


import com.dreamchain.skeleton.dao.CategoryDao;
import com.dreamchain.skeleton.model.Category;
import com.dreamchain.skeleton.model.Product;
import com.dreamchain.skeleton.model.User;
import org.hibernate.Criteria;
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
public class CategoryDaoImpl implements CategoryDao {

    @Autowired
    Environment environment;

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public Category get(Long id) {
        return hibernateTemplate.get(Category.class, id);
    }

    @Override
    public Long save(Category category) {
        return (Long) hibernateTemplate.save(category);
    }

    @Override
    public void update(Category category) {
        hibernateTemplate.merge(category);
    }

    @Override
    public void delete(Category category) {
        hibernateTemplate.delete(category);
    }

    @Override
    public List<Category> findAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user=(User)auth.getPrincipal();
        DetachedCriteria dcr= DetachedCriteria.forClass(Category.class);
        Criterion cr = Restrictions.eq("clientId", user.getClientId());
        if (! environment.getProperty("company.vendor.id").equals(user.getClientId())) dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        return createCategoryList(lst);
    }

    @Override
    public Category findByProductName(String categoryName, long companyId, long departmentId, long productId) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Category.class);
        Criterion cr = Restrictions.eq("name", categoryName.trim()).ignoreCase();
        Criterion cr1 = Restrictions.eq("companyId", companyId);
        Criterion cr2 = Restrictions.eq("departmentId", departmentId);
        Criterion cr3 = Restrictions.eq("productId", productId);
        dcr.add(cr);
        dcr.add(cr2);
        dcr.add(cr3);
        dcr.add(cr1);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new Category();
        return (Category)lst.get(0);
    }

    @Override
    public List<Object> countOfProduct(long productID) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Category.class);//Category
        Criterion cr = Restrictions.eq("productId", productID);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new ArrayList<Object>();
        return lst;
//        return null;
    }


    @Override
    public Category findByNewName(String CurrentName, String newName, Long companyId, Long departmentId, long productId) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Category.class);
        Criterion cr = Restrictions.eq("name", newName.trim()).ignoreCase();
        Criterion cr1 = Restrictions.ne("name", CurrentName.trim()).ignoreCase();
        Criterion cr2 = Restrictions.eq("companyId", companyId);
        Criterion cr3 = Restrictions.eq("departmentId", departmentId);
        Criterion cr4 = Restrictions.eq("productId", productId);
        dcr.add(cr);
        dcr.add(cr1);
        dcr.add(cr2);
        dcr.add(cr3);
        dcr.add(cr4);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new Category();
        return (Category)lst.get(0);
    }

    @Override
    public List<Category> findByProductId(long productId) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Category.class);
        Criterion cr = Restrictions.eq("productId", productId);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        return createCategoryList(lst);
    }


    private List<Category> createCategoryList(List<Object> categoryList){
        List<Category> list = new ArrayList<>();
        for(final Object o : categoryList) {
            list.add((Category)o);
        }
        return list;
    }
}
