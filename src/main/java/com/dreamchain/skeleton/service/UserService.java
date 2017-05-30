package com.dreamchain.skeleton.service;

import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.web.UserCommand;
import com.dreamchain.skeleton.web.UserGrid;

import java.text.ParseException;
import java.util.List;

public interface UserService {
	
	User get(Long id);
	
	String save(User user) throws ParseException;

	String updateUser(User user) throws ParseException;

	String delete(Long userId);

	String changePassword(String userId,String oldPassword,String newPassword) throws Exception;

	List<User> findAll(String userName);
	
//	List<String> findAllUserRole();

	User findByUserName(String username);
	List<User> findAll();

}
