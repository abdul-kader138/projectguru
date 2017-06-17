package com.dreamchain.skeleton.dao.impl;


import com.dreamchain.skeleton.dao.TeamAllocationDao;
import com.dreamchain.skeleton.model.TeamAllocation;
import com.dreamchain.skeleton.model.User;
import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class TeamAllocationDaoImpl implements TeamAllocationDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;


    @Override
    public TeamAllocation get(Long id) {
        return hibernateTemplate.get(TeamAllocation.class,id);
    }

    @Override
    public Long save(TeamAllocation teamAllocation) {
        return (Long) hibernateTemplate.save(teamAllocation);
    }

    @Override
    public void update(TeamAllocation teamAllocation) {
        hibernateTemplate.merge(teamAllocation);
    }

    @Override
    public void delete(TeamAllocation teamAllocation) {
        hibernateTemplate.delete(teamAllocation);
    }

    @Override
    public List<TeamAllocation> findAll() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user=(User)auth.getPrincipal();
        DetachedCriteria dcr= DetachedCriteria.forClass(TeamAllocation.class);
        Criterion cr1 = Restrictions.eq("userType", user.getUserType());
        dcr.add(cr1).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        return createProductList(lst);
    }

    @Override
//    public UserAllocation findByUserId(long requestById, long checkedById, long companyId, long departmentId, long productId,long categoryId) {
    public TeamAllocation findByUserId(long requestById, long checkedById, long companyId, long productId,long categoryId) {
        DetachedCriteria dcr= DetachedCriteria.forClass(TeamAllocation.class);
        Criterion cr =  Restrictions.eq("requestById", requestById);
        Criterion cr1 =  Restrictions.eq("checkedById", checkedById);
        Criterion cr2 = Restrictions.eq("companyId", companyId);
//        Criterion cr3 = Restrictions.eq("departmentId", departmentId);
        Criterion cr4 = Restrictions.eq("productId", productId);
        Criterion cr5 = Restrictions.eq("categoryId", categoryId);
        dcr.add(cr);
        dcr.add(cr1);
        dcr.add(cr2);
//        dcr.add(cr3);
        dcr.add(cr4);
        dcr.add(cr5);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new TeamAllocation();
        return (TeamAllocation)lst.get(0);
    }

//    @Override
//    public UserAllocation findByUserIdAtUpdate(long requestById, long checkedById,
//                                               long companyId, long departmentId, long productId, long categoryId) {
//        DetachedCriteria dcr= DetachedCriteria.forClass(UserAllocation.class);
//        List<Long> userIdList=new ArrayList<Long>();
//        userIdList.add(requestById);
//        userIdList.add(checkedById);
//        Criterion cr =  Restrictions.in("requestById", userIdList);
//        Criterion cr6 =  Restrictions.in("checkedById", userIdList);
//        Criterion cr1 =  Restrictions.eq("checkedById", checkedById);
//        Criterion cr2 = Restrictions.eq("companyId", companyId);
//        Criterion cr3 = Restrictions.eq("departmentId", departmentId);
//        Criterion cr4 = Restrictions.eq("productId", productId);
//        Criterion cr5 = Restrictions.eq("categoryId", categoryId);
//        dcr.add(cr);
//        dcr.add(cr1);
//        dcr.add(cr2);
//        dcr.add(cr3);
//        dcr.add(cr4);
//        dcr.add(cr5);
//        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
//        if(lst.size()==0)return new UserAllocation();
//        return (UserAllocation)lst.get(0);
//    }

    @Override
    public List<Object> countOfAllocation(long allocationId) {
        return null;
    }

    private List<TeamAllocation> createProductList(List<Object> userAllocationList){
        List<TeamAllocation> list = new ArrayList<>();
        for(final Object o : userAllocationList) {
            list.add((TeamAllocation)o);
        }
        return list;
    }
}
