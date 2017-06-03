package com.dreamchain.skeleton.dao.impl;


import com.dreamchain.skeleton.dao.DepartmentDao;
import com.dreamchain.skeleton.model.Department;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class DepartmentDaoImpl implements DepartmentDao{

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
        return hibernateTemplate.loadAll(Department.class);
    }

    @Override
    public Department findByDepartmentName(String departmentName,long companyID) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Department.class);
        Criterion cr = Restrictions.eq("name", departmentName.trim().toUpperCase());
        Criterion cr1 = Restrictions.eq("companyId", companyID);
        dcr.add(cr);
        dcr.add(cr1);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new Department();
        return (Department)lst.get(0);
    }

    @Override
    public List<Object> countOfDepartment(long departmentID) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Department.class);//Product
        Criterion cr = Restrictions.eq("id", departmentID);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new ArrayList<Object>();
        return lst;

    }

    @Override
    public Department findByNewName(String CurrentName,String newName,Long id) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Department.class);
        Criterion cr = Restrictions.eq("name", newName.trim().toUpperCase());
        Criterion cr1 = Restrictions.ne("name", CurrentName.trim().toUpperCase());
        Criterion cr2 = Restrictions.eq("companyId", id);
        dcr.add(cr);
        dcr.add(cr1);
        dcr.add(cr2);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new Department();
        return (Department)lst.get(0);
    }
}
