package com.dreamchain.skeleton.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;
import org.springframework.stereotype.Repository;

import com.dreamchain.skeleton.dao.UserDao;
import com.dreamchain.skeleton.model.User;

@Repository
public class UserDaoImpl implements UserDao {

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
		DetachedCriteria dcr= DetachedCriteria.forClass(User.class);
		Criterion cr = Restrictions.ne("email", userName);
		dcr.add(cr);
		List<Object> lst= hibernateTemplate.findByCriteria(dcr);
		for(Object user:lst){
			userList.add((User) user);
		}
		return userList;
	}

	@Override
	public List<User> findAll() {
		return hibernateTemplate.loadAll(User.class);
	}


	public void save(User user) {
		hibernateTemplate.merge(user);

	}

	@Override
	public void update(User user) {
		hibernateTemplate.update(user);
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

}
