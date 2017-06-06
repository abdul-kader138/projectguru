package com.dreamchain.skeleton.dao.impl;

import com.dreamchain.skeleton.dao.RoleRightDao;
import com.dreamchain.skeleton.model.Department;
import com.dreamchain.skeleton.model.RoleRight;
import com.dreamchain.skeleton.model.User;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RoleRightDaoImpl implements RoleRightDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public RoleRight get(Long id) {
        return hibernateTemplate.get(RoleRight.class, id);
    }

    @Override
    public Long save(RoleRight roleRight) {
        return (Long) hibernateTemplate.save(roleRight);
    }

    @Override
    public void update(RoleRight roleRight) {
        hibernateTemplate.merge(roleRight);
    }

    @Override
    public void delete(RoleRight roleRight) {
        hibernateTemplate.delete(roleRight);
    }

    @Override
    public List<RoleRight> findAll() {
        return hibernateTemplate.loadAll(RoleRight.class);

    }

    @Override
    public List<Object> countOfRole(long roleID) {
        DetachedCriteria dcr= DetachedCriteria.forClass(User.class);// user
        Criterion cr = Restrictions.eq("roleId", roleID);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new ArrayList<Object>();
        return lst;
    }



}
