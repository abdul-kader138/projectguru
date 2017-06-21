package com.dreamchain.skeleton.service;


import com.dreamchain.skeleton.model.User;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationService {
    void setSessionValue(HttpServletRequest request,User user);

}
