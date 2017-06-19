package com.dreamchain.skeleton.dao.impl;


import com.dreamchain.skeleton.dao.ChangeRequestDao;
import com.dreamchain.skeleton.model.ChangeRequest;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class ChangeRequestDaoImpl implements ChangeRequestDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;


    @Override
    public ChangeRequest get(Long id) {
        return hibernateTemplate.get(ChangeRequest.class, id);
    }

    @Override
    public Long save(ChangeRequest changeRequest) {
        return (Long) hibernateTemplate.save(changeRequest);
    }

    @Override
    public void update(ChangeRequest changeRequest) {
        hibernateTemplate.merge(changeRequest); }

    @Override
    public void delete(ChangeRequest changeRequest) {
        hibernateTemplate.delete(changeRequest);
    }

    @Override
    public List<ChangeRequest> findAll() {
        return hibernateTemplate.loadAll(ChangeRequest.class);
    }

    @Override
    public ChangeRequest findByChangeRequestName(String changeRequestName) {
        DetachedCriteria dcr= DetachedCriteria.forClass(ChangeRequest.class);
        Criterion cr = Restrictions.eq("name", changeRequestName.trim()).ignoreCase();
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new ChangeRequest();
        return (ChangeRequest)lst.get(0);
    }

    @Override
    public ChangeRequest findByNewName(String CurrentChangeRequestName, String newChangeRequestName) {
        return null;

    }

    @Override
    public List<Object> countOfCategory(long categoryId) {
        return null;
    }
}
