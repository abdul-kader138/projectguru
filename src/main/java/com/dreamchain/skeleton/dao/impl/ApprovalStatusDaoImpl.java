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

import java.util.ArrayList;
import java.util.List;

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
        DetachedCriteria dcr= DetachedCriteria.forClass(ApprovalStatus.class);
        Criterion cr = Restrictions.eq("approvedById", userId);
        Criterion cr1 = Restrictions.eq("status", environment.getProperty("approval.status.waiting"));
        dcr.add(cr);
        dcr.add(cr1).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        return createCategoryList(lst);
    }
    public List<ApprovalStatus> createCategoryList(List<Object> approvalStatusList){
        List<ApprovalStatus> list = new ArrayList<>();
        for(final Object o : approvalStatusList) {
            list.add((ApprovalStatus)o);
        }
        return list;
    }
}
