package com.dreamchain.skeleton.dao.impl;

import com.dreamchain.skeleton.dao.ApprovalStatusDao;
import com.dreamchain.skeleton.model.ApprovalStatus;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@PropertySource("classpath:config.properties")
public class ApprovalStatusDaoImpl implements ApprovalStatusDao {

    @Autowired
    Environment environment;

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public ApprovalStatus get(Long id) {
        return hibernateTemplate.get(ApprovalStatus.class, id);
    }

    @Override
    public Long save(ApprovalStatus approvalStatus) {
        return (Long) hibernateTemplate.save(approvalStatus);
    }


    @Override
    public void update(ApprovalStatus approvalStatus) {
        hibernateTemplate.merge(approvalStatus);
    }

    @Override
    public List<ApprovalStatus> findByUserId(long userId) {
        DetachedCriteria dcr = DetachedCriteria.forClass(ApprovalStatus.class);
        Criterion cr = Restrictions.eq("approvedById", userId);
        Criterion cr1 = Restrictions.eq("status", environment.getProperty("approval.status.waiting"));
        dcr.add(cr).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        dcr.add(cr1).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Object> lst = hibernateTemplate.findByCriteria(dcr);
        return createApprovalList(lst);
    }

    @Override
    public List<ApprovalStatus> findByUserIdAndRequestId(long userId, long requestId) {
        DetachedCriteria dcr = DetachedCriteria.forClass(ApprovalStatus.class);
        Criterion cr = Restrictions.eq("requestId", requestId);
        Criterion cr1 = Restrictions.eq("approvedById", userId);
        dcr.add(cr).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        dcr.add(cr1).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Object> lst = hibernateTemplate.findByCriteria(dcr);
        return createApprovalList(lst);
    }

    @Override
    public ApprovalStatus findByRequestIdAndUserType(long requestId, String UserType) {
        DetachedCriteria dcr = DetachedCriteria.forClass(ApprovalStatus.class);
        Criterion cr = Restrictions.eq("userType", UserType.trim()).ignoreCase();
        Criterion cr1 = Restrictions.eq("requestId", requestId);
        dcr.add(cr);
        dcr.add(cr1);
        List<Object> lst = hibernateTemplate.findByCriteria(dcr);
        if (lst.size() == 0) return new ApprovalStatus();
        return (ApprovalStatus) lst.get(0);
    }


    public List<ApprovalStatus> createApprovalList(List<Object> approvalStatusList) {
        List<ApprovalStatus> list = new ArrayList<>();
        for (final Object o : approvalStatusList) {
            list.add((ApprovalStatus) o);
        }
        return list;
    }


    @Override
    public Set<ApprovalStatus> findByApprovedId(long approvedId) {
        DetachedCriteria dcr = DetachedCriteria.forClass(ApprovalStatus.class);
        Criterion cr = Restrictions.eq("approvedById", approvedId);
        dcr.add(cr).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Object> lst = hibernateTemplate.findByCriteria(dcr);
        List<ApprovalStatus> approvalStatuses = createApprovalList(lst);
        Set<ApprovalStatus> approvalStatusSet = new HashSet(approvalStatuses);
        return approvalStatusSet;
    }

    @Override
    public void delete(Long id) {
        DetachedCriteria dcr = DetachedCriteria.forClass(ApprovalStatus.class);
        Criterion cr = Restrictions.eq("requestId", id);
        dcr.add(cr);
        List<Object> lst = hibernateTemplate.findByCriteria(dcr);
        List<ApprovalStatus> approvalStatusList = createApprovalList(lst);
        hibernateTemplate.deleteAll(approvalStatusList);
    }

    @Override
    public List<ApprovalStatus> findByApprovedById(long userId) {
        DetachedCriteria dcr = DetachedCriteria.forClass(ApprovalStatus.class);
        Criterion cr = Restrictions.eq("approvedById", userId);
        dcr.add(cr).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Object> lst = hibernateTemplate.findByCriteria(dcr);
        return createApprovalList(lst);
    }

}
