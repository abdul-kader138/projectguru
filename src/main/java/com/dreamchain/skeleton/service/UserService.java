package com.dreamchain.skeleton.service;

import com.dreamchain.skeleton.model.User;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface UserService {
	
	User get(Long id);

	Map<String, Object> save(MultipartHttpServletRequest request,String usersType) throws Exception;

	Map<String, Object> updateUser(MultipartHttpServletRequest request,String usersType) throws Exception;

	String delete(Long companyId,HttpServletRequest request);

	Map<String, Object> changePassword(String oldPassword,String newPassword) throws Exception;

	List<User> findAll(String userName);
	
	User findByUserName(String username);
	List<User> findAll();

}
