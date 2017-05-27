package com.dreamchain.skeleton.dao.impl;

import com.dreamchain.skeleton.dao.RoleDao;
import com.dreamchain.skeleton.model.Role;
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
public class RoleDaoInpl implements RoleDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Override
    public Role get(Long id) {
        return hibernateTemplate.get(Role.class, id);
    }

    @Override
    public Long save(Role role) {
        return (Long) hibernateTemplate.save(role);
    }

    @Override
    public void update(Role role) {
        hibernateTemplate.merge(role);
    }

    @Override
    public void delete(Role role) {
        hibernateTemplate.delete(role);
    }

    @Override
    public List<Role> findAll() {
        return hibernateTemplate.loadAll(Role.class);

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

    @Override
    public Role findByRoleName(String roleName) {
        DetachedCriteria dcr = DetachedCriteria.forClass(Role.class);
        Criterion cr = Restrictions.eq("name", roleName);
        dcr.add(cr);
        List<Object> lst = hibernateTemplate.findByCriteria(dcr);
        if (lst.size() == 0) return new Role();
        return (Role) lst.get(0);
    }
}
