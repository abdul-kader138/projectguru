package com.dreamchain.skeleton.dao.impl;

import java.util.ArrayList;
import java.util.List;

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

import com.dreamchain.skeleton.dao.UserDao;
import com.dreamchain.skeleton.model.User;

@Repository
@PropertySource("classpath:config.properties")
public class UserDaoImpl implements UserDao {

	@Autowired
	Environment environment;

	@Autowired
	private HibernateTemplate hibernateTemplate;

	public User get(Long id) {
		return (User) hibernateTemplate.get(User.class, id);
	}

	public void delete(User user) {
		hibernateTemplate.delete(user);
	}

	@Override
	public void remove(User user) {
	hibernateTemplate.evict(user);
	}

	@SuppressWarnings("unchecked")
	public List<User> findAll(String userName) {
		List<User> userList=new ArrayList<>();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user=(User)auth.getPrincipal();
		DetachedCriteria dcr= DetachedCriteria.forClass(User.class);
		Criterion cr = Restrictions.ne("email", userName);
		Criterion cr1 = Restrictions.eq("clientId", user.getClientId());
		dcr.add(cr).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		dcr.add(cr1);
		List<Object> lst= hibernateTemplate.findByCriteria(dcr);
		for(Object userObj:lst){
			userList.add((User) userObj);
		}
		return userList;
	}

	@Override
	public List<User> findAll() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user=(User)auth.getPrincipal();
		DetachedCriteria dcr= DetachedCriteria.forClass(User.class);
		Criterion cr = Restrictions.ne("email", user.getEmail());
		Criterion cr1 = Restrictions.eq("clientId", user.getClientId());
		dcr.add(cr).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		dcr.add(cr1);
		List<Object> lst= hibernateTemplate.findByCriteria(dcr);
		return createProductList(lst);
	}


	public Long save(User user) {
		return (Long) hibernateTemplate.save(user);

	}

	@Override
	public void update(User user) {
		hibernateTemplate.merge(user);
	}

	@Override
	public User findByUserName(String username) {
		User user=null;
		DetachedCriteria dcr= DetachedCriteria.forClass(User.class);
		Criterion cr = Restrictions.eq("email", username);
		dcr.add(cr);
		List<Object> lst= hibernateTemplate.findByCriteria(dcr);
		if(lst.size()==0)return user;
		return (User)lst.get(0);
	}

	@Override
	public User findByNewName(String CurrentName,String newName) {
		DetachedCriteria dcr= DetachedCriteria.forClass(User.class);
		Criterion cr = Restrictions.eq("name", newName.trim()).ignoreCase();
		Criterion cr1 = Restrictions.ne("name", CurrentName.trim()).ignoreCase();
		dcr.add(cr);
		dcr.add(cr1);
		List<Object> lst= hibernateTemplate.findByCriteria(dcr);
		if(lst.size()==0)return new User();
		return (User)lst.get(0);
	}


	@Override
	public User findByRoleRightId(Long roleRightsId) {
		DetachedCriteria dcr= DetachedCriteria.forClass(User.class);
		Criterion cr = Restrictions.eq("roleRightsId", roleRightsId);
		dcr.add(cr);
		List<Object> lst= hibernateTemplate.findByCriteria(dcr);
		if(lst.size()==0)return new User();
		return (User)lst.get(0);
	}


	private List<User> createProductList(List<Object> userList){
		List<User> list = new ArrayList<>();
		for(final Object o : userList) {
			list.add((User)o);
		}
		return list;
	}

}
