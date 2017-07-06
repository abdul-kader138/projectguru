package com.dreamchain.skeleton.dao;

import java.util.List;

import com.dreamchain.skeleton.model.User;

public interface UserDao {

	User get(Long id);
	Long save(User user);
	void update(User user);
	void delete(User user);
	void remove(User user);
	List<User> findAll(String username);
	List<User> findAll();
	User findByUserName(String username);
	User findByNewName(String CurrentName,String newName);
	User findByRoleRightId(Long roleRightsId);

}
