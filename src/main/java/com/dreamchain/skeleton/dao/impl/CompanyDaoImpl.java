package com.dreamchain.skeleton.dao.impl;

import com.dreamchain.skeleton.dao.CompanyDao;
import com.dreamchain.skeleton.model.Company;
import com.dreamchain.skeleton.model.Department;
import com.dreamchain.skeleton.model.Project;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CompanyDaoImpl implements CompanyDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public Company get(Long id) {
        return hibernateTemplate.get(Company.class,id);
    }

    @Override
    public Long save(Company company) {
        return (Long)  hibernateTemplate.save(company);
    }

    @Override
    public void update(Company company) {
        hibernateTemplate.merge(company);
    }

    @Override
    public void delete(Company company) {
       hibernateTemplate.delete(company);
    }

    @Override
    public List<Company> findAll() {
        return hibernateTemplate.loadAll(Company.class);
    }

    @Override
    public Company findByCompanyName(String companyName) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Company.class);
        Criterion cr = Restrictions.eq("name", companyName.trim().toUpperCase());
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new Company();
        return (Company)lst.get(0);
    }

    @Override
    public List<Object> countOfCompany(long companyID) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Department.class);
        Criterion cr = Restrictions.eq("companyId", companyID);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new ArrayList<Object>();
        return lst;

    }


    @Override
    public Company findByNewName(String CurrentName,String newName) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Company.class);
        Criterion cr = Restrictions.eq("name", newName.trim().toUpperCase());
        Criterion cr1 = Restrictions.ne("name", CurrentName.trim().toUpperCase());
        dcr.add(cr);
        dcr.add(cr1);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new Company();
        return (Company)lst.get(0);
    }

}
