package com.dreamchain.skeleton.service;

import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.web.UserCommand;
import com.dreamchain.skeleton.web.UserGrid;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface UserService {
	
	User get(Long id);

	Map<String, Object> save(MultipartHttpServletRequest request) throws Exception;

	Map<String, Object> updateUser(MultipartHttpServletRequest request) throws Exception;

	String delete(Long companyId,HttpServletRequest request);

	Map<String, Object> changePassword(String oldPassword,String newPassword) throws Exception;

	List<User> findAll(String userName);
	
//	List<String> findAllUserRole();

	User findByUserName(String username);
	List<User> findAll();

}
