package com.dreamchain.skeleton.dao.impl;


import com.dreamchain.skeleton.dao.ChangeRequestDao;
import com.dreamchain.skeleton.model.ChangeRequest;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Repository
@PropertySource("classpath:config.properties")
public class ChangeRequestDaoImpl implements ChangeRequestDao {
    @Autowired
    Environment environment;

    @Autowired
    private HibernateTemplate hibernateTemplate;


    @Override
    public ChangeRequest get(Long id) {
        DetachedCriteria dcr= DetachedCriteria.forClass(ChangeRequest.class);
        Criterion cr = Restrictions.eq("id", id);
        dcr.add(cr).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new ChangeRequest();
        return (ChangeRequest)lst.get(0);
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
    public List<ChangeRequest> findAll(Set<Long> requestId){
        DetachedCriteria dcr= DetachedCriteria.forClass(ChangeRequest.class);
        Criterion cr = Restrictions.in("id", requestId);
        Criterion cr1 = Restrictions.eq("checkedByStatus", environment.getProperty("approval.user.checkedBy"));
        dcr.add(cr).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        dcr.add(cr1).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        return createChangeRequestList(lst);
    }

    @Override
    public List<ChangeRequest> findAllForDeveloper() {
        DetachedCriteria dcr= DetachedCriteria.forClass(ChangeRequest.class);
        Criterion cr = Restrictions.eq("status", environment.getProperty("request.status.open"));
        Criterion cr1 = Restrictions.isNotNull("deliverDate");
        dcr.add(cr).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        dcr.add(cr1);
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

    @Override
    public List<Object> findAllStatus() {
        List<Object> list=hibernateTemplate.find("select distinct(status), count(*) as number from ChangeRequest group by status order by status desc");
        return list;
    }

    @Override
    public ChangeRequest findByName(String name, long companyId, long productId, long categoryId) {
        DetachedCriteria dcr= DetachedCriteria.forClass(ChangeRequest.class);
        Criterion cr = Restrictions.eq("name", name.trim()).ignoreCase();
        Criterion cr1 = Restrictions.eq("companyId", companyId);
        Criterion cr2 = Restrictions.eq("productId", productId);
        Criterion cr3 = Restrictions.eq("categoryId", categoryId);
        dcr.add(cr);
        dcr.add(cr1);
        dcr.add(cr2);
        dcr.add(cr3);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new ChangeRequest();
        return (ChangeRequest)lst.get(0);
    }



    private List<ChangeRequest> createChangeRequestList(List<Object> changeRequestList){
        List<ChangeRequest> list = new ArrayList<>();
        for(final Object o : changeRequestList) {
            list.add((ChangeRequest)o);
        }
        return list;
    }
}
