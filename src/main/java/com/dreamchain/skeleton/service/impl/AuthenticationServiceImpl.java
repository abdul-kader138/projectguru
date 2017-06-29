package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.ApprovalStatusDao;
import com.dreamchain.skeleton.dao.ChangeRequestDao;
import com.dreamchain.skeleton.dao.UserDao;
import com.dreamchain.skeleton.model.ApprovalStatus;
import com.dreamchain.skeleton.model.ChangeRequest;
import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.AuthenticationService;
import com.dreamchain.skeleton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Service
@PropertySource("classpath:config.properties")
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    UserDao userDao;

    @Autowired
    ApprovalStatusDao approvalStatusDao;

    @Autowired
    ChangeRequestDao changeRequestDao;



    @Override
    public void setSessionValue(HttpServletRequest request, User user) {
        User existingUser=userDao.findByUserName(user.getEmail());
        String hasChangeRequest="No";
        List<ApprovalStatus> list=approvalStatusDao.findByUserId(existingUser.getId());
        ChangeRequest changeRequest=changeRequestDao.findByRequestById(existingUser.getId());
        if(changeRequest.getRequestBy() !=null) hasChangeRequest="Yes";
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("passwordMsg", null);
        httpSession.setAttribute("isPasswordChanged", null);
        httpSession.setAttribute("name", user.getName());
        httpSession.setAttribute("email", user.getEmail());
        httpSession.setAttribute("role", user.getRole());
        httpSession.setAttribute("path", user.getImagePath());
        httpSession.setAttribute("userType", user.getUserType());
        httpSession.setAttribute("imagePath", user.getImagePath());
        httpSession.setAttribute("notificationCount", list.size());
        httpSession.setAttribute("hasChangeRequest", hasChangeRequest);
    }
}
