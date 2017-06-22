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
        DetachedCriteria dcr = DetachedCriteria.forClass(ApprovalStatus.class);
        Criterion cr = Restrictions.eq("approvedById", userId);
        Criterion cr1 = Restrictions.eq("status", environment.getProperty("approval.status.waiting"));
        dcr.add(cr);
        dcr.add(cr1).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Object> lst = hibernateTemplate.findByCriteria(dcr);
        return createApprovalList(lst);
    }

    @Override
    public ApprovalStatus findByCompanyAndProductAndCategory(long companyId, long productId, long categoryId, String name, String UserType) {
        DetachedCriteria dcr = DetachedCriteria.forClass(ApprovalStatus.class);
        Criterion cr = Restrictions.eq("requestName", name.trim()).ignoreCase();
        Criterion cr1 = Restrictions.eq("userType", UserType.trim()).ignoreCase();
        Criterion cr2 = Restrictions.eq("companyId", companyId);
        Criterion cr3 = Restrictions.eq("productId", productId);
        Criterion cr4 = Restrictions.eq("categoryId", categoryId);
        dcr.add(cr);
        dcr.add(cr1);
        dcr.add(cr2);
        dcr.add(cr3);
        dcr.add(cr4);
        List<Object> lst = hibernateTemplate.findByCriteria(dcr);
        if (lst.size() == 0) return new ApprovalStatus();
        return (ApprovalStatus) lst.get(0);
    }

    @Override
    public ApprovalStatus findById(long id) {
        DetachedCriteria dcr = DetachedCriteria.forClass(ApprovalStatus.class);
        Criterion cr = Restrictions.eq("id", id);
        dcr.add(cr);
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
}
