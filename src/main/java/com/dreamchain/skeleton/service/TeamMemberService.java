package com.dreamchain.skeleton.service;


import com.dreamchain.skeleton.model.User;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface TeamMemberService {

    User get(Long id);

    Map<String, Object> save(MultipartHttpServletRequest request,String usersType) throws Exception;

    Map<String, Object> updateUser(MultipartHttpServletRequest request,String usersType) throws Exception;

    String delete(Long companyId,HttpServletRequest request);

    List<User> findAll(String userName);

    User findByUserName(String username);
    List<User> findAll();
}
