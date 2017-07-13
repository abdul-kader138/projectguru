package com.dreamchain.skeleton.dao.impl;


import com.dreamchain.skeleton.dao.UserAllocationDao;
import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.model.UserAllocation;
import org.hibernate.Criteria;
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
public class UserAllocationDaoImpl implements UserAllocationDao {
    @Autowired
    Environment environment;
    @Autowired
    private HibernateTemplate hibernateTemplate;


    @Override
    public UserAllocation get(Long id) {
        return hibernateTemplate.get(UserAllocation.class, id);
    }

    @Override
    public Long save(UserAllocation userAllocation) {
        return (Long) hibernateTemplate.save(userAllocation);
    }

    @Override
    public void update(UserAllocation userAllocation) {
        hibernateTemplate.merge(userAllocation);
    }

    @Override
    public void delete(UserAllocation userAllocation) {
        hibernateTemplate.delete(userAllocation);
    }

    @Override
    public List<UserAllocation> findAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        DetachedCriteria dcr = DetachedCriteria.forClass(UserAllocation.class);
        Criterion cr = Restrictions.eq("clientId", user.getClientId());
//        if (environment.getProperty("company.client.id").equals(user.getClientId()))
        dcr.add(cr);
        Criterion cr1 = Restrictions.eq("userType", user.getUserType());
        dcr.add(cr1).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Object> lst = hibernateTemplate.findByCriteria(dcr);
        return createUserAllocationList(lst);
    }

    @Override
    public List<UserAllocation> AllAllocationByItCoordinator(long itCoordinator) {
        DetachedCriteria dcr= DetachedCriteria.forClass(UserAllocation.class);
        Criterion cr = Restrictions.eq("itCoordinator.id", itCoordinator);
        dcr.add(cr).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        return createUserAllocationList(lst);
    }

    @Override
    public List<UserAllocation> AllAllocationByApprovedBy(long approvedBy) {
        DetachedCriteria dcr= DetachedCriteria.forClass(UserAllocation.class);
        Criterion cr = Restrictions.eq("approvedBy.id", approvedBy);
        dcr.add(cr).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        return createUserAllocationList(lst);
    }

    @Override
    public UserAllocation findByProductAndCategory(long companyId, long productId, long categoryId) {
        DetachedCriteria dcr = DetachedCriteria.forClass(UserAllocation.class);
        Criterion cr = Restrictions.eq("companyId", companyId);
        Criterion cr1 = Restrictions.eq("productId", productId);
        Criterion cr2 = Restrictions.eq("categoryId", categoryId);
        dcr.add(cr);
        dcr.add(cr1);
        dcr.add(cr2);
        List<Object> lst = hibernateTemplate.findByCriteria(dcr);
        if (lst.size() == 0) return new UserAllocation();
        return (UserAllocation) lst.get(0);
    }


    @Override
    public List<Object> countOfAllocation(long categoryId) {
        DetachedCriteria dcr = DetachedCriteria.forClass(UserAllocation.class);//Category
        Criterion cr = Restrictions.eq("categoryId", categoryId);
        dcr.add(cr);
        List<Object> lst = hibernateTemplate.findByCriteria(dcr);
        if (lst.size() == 0) return new ArrayList<Object>();
        return lst;
    }

    private List<UserAllocation> createUserAllocationList(List<Object> userAllocationList) {
        List<UserAllocation> list = new ArrayList<>();
        for (final Object o : userAllocationList) {
            list.add((UserAllocation) o);
        }
        return list;
    }
}
