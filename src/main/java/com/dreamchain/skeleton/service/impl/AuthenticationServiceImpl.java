package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.ApprovalStatusDao;
import com.dreamchain.skeleton.dao.ChangeRequestDao;
import com.dreamchain.skeleton.dao.TeamAllocationDao;
import com.dreamchain.skeleton.dao.UserDao;
import com.dreamchain.skeleton.model.ApprovalStatus;
import com.dreamchain.skeleton.model.TeamAllocation;
import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
@PropertySource("classpath:config.properties")
public class AuthenticationServiceImpl implements AuthenticationService {
    @Autowired
    UserDao userDao;

    @Autowired
    ApprovalStatusDao approvalStatusDao;

    @Autowired
    ChangeRequestDao changeRequestDao;

    @Autowired
    TeamAllocationDao teamAllocationDao;
    @Autowired
    Environment environment;


    @Transactional(readOnly=true)
    public void setSessionValue(HttpServletRequest request, User user) {
        User existingUser = userDao.findByUserName(user.getEmail());
        String hasChangeRequest = "No";
        List<ApprovalStatus> list = approvalStatusDao.findByUserId(existingUser.getId());
        TeamAllocation obj = teamAllocationDao.findByRequestById(existingUser.getId());
        List<Object> requestListCount = changeRequestDao.findAllStatus(existingUser.getClientId());
        Map<String, Long> totalRequest = TotalRequest(requestListCount);
        if (obj.getId() != 0l) hasChangeRequest = "Yes";
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("passwordMsg", null);
        httpSession.setAttribute("isPasswordChanged", null);
        httpSession.setAttribute("user", existingUser);
        httpSession.setAttribute("notificationCount", list.size());
        httpSession.setAttribute("hasChangeRequest", hasChangeRequest);
        httpSession.setAttribute("totalRequest", totalRequest.get(environment.getProperty("request.status.all")));
        httpSession.setAttribute("totalRequestDone", totalRequest.get(environment.getProperty("request.status.done")));
        httpSession.setAttribute("totalRequestOpen", totalRequest.get(environment.getProperty("request.status.open")));
    }


    private Map<String, Long> TotalRequest(List<Object> obj) {
        Map<String, Long> objList = new HashMap<>();
        Long totalRequest = 0l;
        objList.put(environment.getProperty("request.status.open"), 0l);
        objList.put(environment.getProperty("request.status.done"), 0l);
        for (Object object : obj) {
            Object[] row = (Object[]) object;
            Map<String, Long> requestDetails = setRequest(row);
            if (requestDetails.containsKey(environment.getProperty("request.status.open"))) {
                totalRequest += (Long) requestDetails.get(environment.getProperty("request.status.open"));
                objList.put(environment.getProperty("request.status.open"), (Long) requestDetails.get(environment.getProperty("request.status.open")));
            }
            if (requestDetails.containsKey(environment.getProperty("request.status.done"))) {
                objList.put(environment.getProperty("request.status.done"), (Long) requestDetails.get(environment.getProperty("request.status.done")));
                totalRequest += (Long) requestDetails.get(environment.getProperty("request.status.done"));
            }
        }
        objList.put(environment.getProperty("request.status.all"), totalRequest);
        return objList;
    }

    private Map<String, Long> setRequest(Object[] obj) {
        Map<String, Long> objList = new HashMap<>();
        String statusType = "";
        Long statusCount = 0l;
        for (Object object : obj) {
            if (object instanceof String) statusType = (String) object;
            if (object instanceof Long) statusCount = (Long) object;
        }
        objList.put(statusType, statusCount);
        return objList;
    }


}
