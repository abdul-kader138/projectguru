package com.dreamchain.skeleton.dao.impl;


import com.dreamchain.skeleton.dao.TeamAllocationDao;
import com.dreamchain.skeleton.model.TeamAllocation;
import com.dreamchain.skeleton.model.User;
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
public class TeamAllocationDaoImpl implements TeamAllocationDao {

    @Autowired
    Environment environment;
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
        Criterion cr = Restrictions.eq("clientId", user.getClientId());
//        if (environment.getProperty("company.client.id").equals(user.getClientId()))
        dcr.add(cr);
        dcr.add(cr1).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        return createTeamAllocationList(lst);
    }

    @Override
    public List<TeamAllocation> AllAllocationByCheckedBy(long checkedBy) {
        DetachedCriteria dcr= DetachedCriteria.forClass(TeamAllocation.class);
        Criterion cr = Restrictions.eq("checkedBy.id", checkedBy);
        dcr.add(cr).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        return createTeamAllocationList(lst);
    }

    @Override
    public List<TeamAllocation> AllAllocationByRequestedBy(long requestedBy) {
        DetachedCriteria dcr= DetachedCriteria.forClass(TeamAllocation.class);
        Criterion cr = Restrictions.eq("requestedBy.id", requestedBy);
        dcr.add(cr).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        return createTeamAllocationList(lst);
    }


    @Override
    public TeamAllocation findByProductAndCategory(long companyId, long productId, long categoryId) {
        DetachedCriteria dcr= DetachedCriteria.forClass(TeamAllocation.class);
        Criterion cr = Restrictions.eq("companyId", companyId);
        Criterion cr1 = Restrictions.eq("productId", productId);
        Criterion cr2 = Restrictions.eq("category.id", categoryId);
        dcr.add(cr);
        dcr.add(cr1);
        dcr.add(cr2);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new TeamAllocation();
        return (TeamAllocation)lst.get(0);
    }


    @Override
    public List<Object> countOfAllocation(long categoryId) {
        DetachedCriteria dcr= DetachedCriteria.forClass(TeamAllocation.class);//Category
        Criterion cr = Restrictions.eq("category.id", categoryId);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new ArrayList<Object>();
        return lst;
    }

    @Override
    public List<Object> countOfAllocationByProduct(long productId) {
        DetachedCriteria dcr= DetachedCriteria.forClass(TeamAllocation.class);//Category
        Criterion cr = Restrictions.eq("productId", productId);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new ArrayList<Object>();
        return lst;
    }

    @Override
    public TeamAllocation findByRequestById(long requestById) {
        DetachedCriteria dcr= DetachedCriteria.forClass(TeamAllocation.class);
        Criterion cr = Restrictions.eq("requestedBy.id", requestById);
        dcr.add(cr).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new TeamAllocation();
        return (TeamAllocation)lst.get(0);
    }

    private List<TeamAllocation> createTeamAllocationList(List<Object> teamAllocationList){
        List<TeamAllocation> list = new ArrayList<>();
        for(final Object o : teamAllocationList) {
            list.add((TeamAllocation)o);
        }
        return list;
    }
}
