package com.dreamchain.skeleton.dao.impl;

import com.dreamchain.skeleton.dao.RolesDao;
import com.dreamchain.skeleton.model.RoleRight;
import com.dreamchain.skeleton.model.Roles;
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
public class RolesDaoImpl implements RolesDao {

    @Autowired
    private HibernateTemplate hibernateTemplate;

    @Autowired
    Environment environment;

    @Override
    public Roles get(Long id) {
        return hibernateTemplate.get(Roles.class,id);
    }

    @Override
    public Long save(Roles roles) {
        return (Long) hibernateTemplate.save(roles);
    }

    @Override
    public void update(Roles roles) {
        hibernateTemplate.merge(roles);
    }

    @Override
    public void delete(Roles roles) {
        hibernateTemplate.delete(roles);
    }

    @Override
    public List<Roles> findAll() {
        DetachedCriteria dcr= DetachedCriteria.forClass(Roles.class);
        Criterion cr = Restrictions.ne("name", environment.getProperty("role.super.admin.hide"));
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        return createRolesList(lst);
    }

    @Override
    public Roles findByRolesName(String rolesName) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Roles.class);
        Criterion cr = Restrictions.eq("name", rolesName);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new Roles();
        return (Roles)lst.get(0);
    }

    @Override
    public List<Object> countOfRoles(long rolesId) {
        DetachedCriteria dcr= DetachedCriteria.forClass(RoleRight.class);
        Criterion cr = Restrictions.eq("roleId", rolesId);
        dcr.add(cr);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new ArrayList<Object>();
        return lst;
    }

    @Override
    public Roles findByNewName(String CurrentName,String newName) {
        DetachedCriteria dcr= DetachedCriteria.forClass(Roles.class);
        Criterion cr = Restrictions.eq("name", newName.trim()).ignoreCase();
        Criterion cr1 = Restrictions.ne("name", CurrentName.trim()).ignoreCase();
        dcr.add(cr);
        dcr.add(cr1);
        List<Object> lst= hibernateTemplate.findByCriteria(dcr);
        if(lst.size()==0)return new Roles();
        return (Roles)lst.get(0);
    }

    private List<Roles> createRolesList(List<Object> rolesList){
        List<Roles> list = new ArrayList<>();
        for(final Object o : rolesList) {
            list.add((Roles)o);
        }
        return list;
    }
}
