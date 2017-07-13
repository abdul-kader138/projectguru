package com.dreamchain.skeleton.dao.impl;


import com.dreamchain.skeleton.dao.DepartmentDao;
import com.dreamchain.skeleton.model.Category;
import com.dreamchain.skeleton.model.Department;
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
public class DepartmentDaoImpl implements DepartmentDao{
    @Autowired
    Environment environment;
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public Department get(Long id) {
        return hibernateTemplate.get(Department.class,id);
    }

    @Override
    public Long save(Department department) {
        return (Long)  hibernateTemplate.save(department);
    }

    @Override
    public void update(Department department) {
        hibernateTemplate.merge(department);
    }

    @Override
    public void delete(Department department) {
        hibernateTemplate.delete(department);
    }

    @Override
    public List<Department> findAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user=(User)auth.getPrincipal();
        DetachedCriteria dcr= DetachedCriteria.forClass(Department.class);
        Criterion cr = Restrictions.eq("clientId", user.getClientId());
        if (! environment.getProperty("company.vendor.id").equals(user.getClientId())) dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        return createDepartmentList(lst);
    }

    @Override
    public List<Department> findByCompanyName(long companyId) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Department.class);
        Criterion cr = Restrictions.eq("company.id", companyId);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        return createDepartmentList(lst);
    }

    @Override
    public Department findByDepartmentName(String departmentName,long companyID) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Department.class);
        Criterion cr = Restrictions.eq("name", departmentName.trim()).ignoreCase();
        Criterion cr1 = Restrictions.eq("company.id", companyID);
        dcr.add(cr);
        dcr.add(cr1);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new Department();
        return (Department)lst.get(0);
    }

    @Override
    public List<Object> countOfDepartment(long departmentID) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Category.class);
        Criterion cr = Restrictions.eq("departmentId", departmentID);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new ArrayList<Object>();
        return lst;

    }

    @Override
    public Department findByNewName(String CurrentName,String newName,Long id) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Department.class);
        Criterion cr = Restrictions.eq("name", newName.trim()).ignoreCase();
        Criterion cr1 = Restrictions.ne("name", CurrentName.trim()).ignoreCase();
        Criterion cr2 = Restrictions.eq("company.id", id);
        dcr.add(cr);
        dcr.add(cr1);
        dcr.add(cr2);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new Department();
        return (Department)lst.get(0);
    }

    private List<Department> createDepartmentList(List<Object> departmentList){
       List<Department> list = new ArrayList<>();
        for(final Object o : departmentList) {
            list.add((Department)o);
        }
        return list;
    }
}
