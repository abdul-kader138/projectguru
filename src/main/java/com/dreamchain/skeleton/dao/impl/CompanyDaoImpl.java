package com.dreamchain.skeleton.dao.impl;

import com.dreamchain.skeleton.dao.CompanyDao;
import com.dreamchain.skeleton.model.Company;
import com.dreamchain.skeleton.model.User;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

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
    public void save(Company company) {
      hibernateTemplate.save(company);
    }

    @Override
    public void update(Company company) {

    }

    @Override
    public void delete(long companyId) {

    }

    @Override
    public List<Company> findAll() {
        return null;
    }

    @Override
    public Company findByCompanyName(String companyName) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Company.class);
        Criterion cr = Restrictions.eq("name", companyName);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new Company();
        return (Company)lst.get(0);
    }
}
