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
    public List<Object> findAllStatus(String clientId) {
        List<Object> list=hibernateTemplate.find("select distinct(status), count(*) as number from ChangeRequest where client_id =? group by status order by status desc",new Object[]{clientId});
        return list;
    }

    @Override
    public ChangeRequest findByName(String name,long categoryId) {
        DetachedCriteria dcr= DetachedCriteria.forClass(ChangeRequest.class);
        Criterion cr = Restrictions.eq("name", name.trim()).ignoreCase();
        Criterion cr2 = Restrictions.eq("categoryId", categoryId);
        dcr.add(cr);
        dcr.add(cr2);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new ChangeRequest();
        return (ChangeRequest)lst.get(0);
    }


    @Override
    public ChangeRequest findByCategoryId(long categoryId) {
        DetachedCriteria dcr= DetachedCriteria.forClass(ChangeRequest.class);
        Criterion cr =  Restrictions.eq("categoryId", categoryId);
        dcr.add(cr).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);;
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new ChangeRequest();
        return (ChangeRequest)lst.get(0);
    }

    @Override
    public ChangeRequest findByDepartmentId(long departmentId) {
        DetachedCriteria dcr= DetachedCriteria.forClass(ChangeRequest.class);
        Criterion cr =  Restrictions.eq("departmentId", departmentId);
        dcr.add(cr).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);;
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new ChangeRequest();
        return (ChangeRequest)lst.get(0);
    }


    @Override
    public ChangeRequest findByTeamAllocationId(long teamAllocationId) {
        DetachedCriteria dcr= DetachedCriteria.forClass(ChangeRequest.class);
        Criterion cr =  Restrictions.eq("teamAllocationId", teamAllocationId);
        dcr.add(cr).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);;
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
