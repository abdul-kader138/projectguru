package com.dreamchain.skeleton.dao.impl;

import com.dreamchain.skeleton.dao.DesignationDao;
import com.dreamchain.skeleton.model.Designation;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class DesignationDaoImpl implements DesignationDao{
    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public Designation get(Long id) {
        return hibernateTemplate.get(Designation.class,id);
    }

    @Override
    public Long save(Designation designation) {
        return (Long)  hibernateTemplate.save(designation);
    }

    @Override
    public void update(Designation designation) {
        hibernateTemplate.merge(designation);
    }

    @Override
    public void delete(Designation designation) {
        hibernateTemplate.delete(designation);
    }

    @Override
    public List<Designation> findAll() {
        return hibernateTemplate.loadAll(Designation.class);
    }

    @Override
    public Designation findByDesignationName(String designationName) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Designation.class);
        Criterion cr = Restrictions.eq("name", designationName);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new Designation();
        return (Designation)lst.get(0);
    }

    @Override
    public List<Object> countOfDesignation(long designationID) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Designation.class);//User
        Criterion cr = Restrictions.eq("id", designationID);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new ArrayList<Object>();
        return lst;
    }
}
