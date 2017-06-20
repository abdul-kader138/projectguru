package com.dreamchain.skeleton.dao.impl;


import com.dreamchain.skeleton.dao.ChangeRequestDao;
import com.dreamchain.skeleton.model.ChangeRequest;
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
public class ChangeRequestDaoImpl implements ChangeRequestDao {
    @Autowired
    Environment environment;

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

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user=(User)auth.getPrincipal();
        DetachedCriteria dcr= DetachedCriteria.forClass(ChangeRequest.class);
        Criterion cr = Restrictions.eq("companyId", user.getCompanyId());
        if (environment.getProperty("user.type.client").equals(user.getUserType())) dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        return createChangeRequestList(lst);
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

    private List<ChangeRequest> createChangeRequestList(List<Object> changeRequestList){
        List<ChangeRequest> list = new ArrayList<>();
        for(final Object o : changeRequestList) {
            list.add((ChangeRequest)o);
        }
        return list;
    }
}
