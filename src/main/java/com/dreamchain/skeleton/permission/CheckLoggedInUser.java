package com.dreamchain.skeleton.permission;

import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpSession;


public class CheckLoggedInUser {
    @Autowired
    UserService userService;


   private boolean checkLoggedInUserExistence(HttpSession httpSession) {
        boolean isLoggedUserExists = false;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = (User) auth.getPrincipal();
        User user = userService.findByUserName(loggedUser.getEmail());
        if (user == null) {
            isLoggedUserExists = true;
            httpSession.invalidate();
        }
        return isLoggedUserExists;
    }

}
