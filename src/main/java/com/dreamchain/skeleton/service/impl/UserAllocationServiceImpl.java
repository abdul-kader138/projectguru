package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.*;
import com.dreamchain.skeleton.model.*;
import com.dreamchain.skeleton.service.UserAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserAllocationServiceImpl implements UserAllocationService {


    @Autowired
    UserAllocationDao userAllocationDao ;
    @Autowired
    CategoryDao categoryDao ;
    @Autowired
    ChangeRequestDao changeRequestDao;
    @Autowired
    UserDao userDao ;
    @Autowired
    Environment environment;


    private static final String USER_ALLOCATION_EXISTS = "This Product and users are already allocated.Please try again with new one!!!";
    private static final String INVALID_INPUT = "Invalid input";
    private static final String INVALID_USER_ALLOCATION = "User allocation not exists";
    private static final String BACK_DATED_DATA = "User allocation data is old.Please try again with updated data";
    private static final String ASSOCIATED_REQUEST = "This allocation is associated with request.First remove tagging and try again";
    private static final String SAME_ALLOCATED_USER = "Approved By and Coordinator name can't be same";


    @Transactional(readOnly = true)
    public UserAllocation get(Long id) {
        return userAllocationDao.get(id);
    }

    @Transactional
    public Map<String, Object> save(Map<String, Object> userAllocationObj) throws ParseException {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        UserAllocation newUserAllocation=new UserAllocation();
        UserAllocation existingUserAllocation=new UserAllocation();
        UserAllocation userAllocation=createObjForSave(userAllocationObj);
        validationMsg = checkInput(userAllocation);
        if ("".equals(validationMsg)) existingUserAllocation = userAllocationDao.findByProductAndCategory(userAllocation.getCompanyId(),
                userAllocation.getProductId(),userAllocation.getCategoryId());
        if (existingUserAllocation.getApprovedBy() != null && "".equals(validationMsg)) validationMsg = USER_ALLOCATION_EXISTS;
        if(userAllocation.getApprovedBy().getId() == userAllocation.getItCoordinator().getId() ) validationMsg=SAME_ALLOCATED_USER;
        if ("".equals(validationMsg)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            Date date = dateFormat.parse(dateFormat.format(new Date()));
            userAllocation.setCreatedBy(getUserId().getEmail());
            userAllocation.setCreatedOn(date);
            long id= userAllocationDao.save(userAllocation);
            newUserAllocation=userAllocationDao.get(id);
        }
        obj.put("userAllocation",newUserAllocation);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public Map<String, Object> update(Map<String, Object> userAllocationObj) throws ParseException {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        ChangeRequest changeRequest=new ChangeRequest();
        UserAllocation newObj=new UserAllocation();
        UserAllocation existingUserAllocation=new UserAllocation();
        UserAllocation userAllocation=createObjForSave(userAllocationObj);
        validationMsg = checkInput(userAllocation);
        if (userAllocation.getId() == 0l && "".equals(validationMsg)) validationMsg = INVALID_INPUT;
        if(userAllocation.getApprovedBy().getId() == userAllocation.getItCoordinator().getId() ) validationMsg=SAME_ALLOCATED_USER;
        if ("".equals(validationMsg)) existingUserAllocation = userAllocationDao.get(userAllocation.getId());
        if (existingUserAllocation == null && "".equals(validationMsg)) validationMsg = INVALID_USER_ALLOCATION;
        if("".equals(validationMsg)) changeRequest=changeRequestDao.findByTeamAllocationId(existingUserAllocation.getId());
        if("".equals(validationMsg) && changeRequest.getName() !=null) validationMsg=ASSOCIATED_REQUEST;
        if (userAllocation.getVersion() != existingUserAllocation.getVersion() && "".equals(validationMsg)) validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) {
            newObj=setUpdateCategoryValue(userAllocation, existingUserAllocation);
            userAllocationDao.update(newObj);
        }
        obj.put("userAllocation",newObj);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public String delete(Long userAllocationId) {
        String validationMsg = "";
        ChangeRequest changeRequest=new ChangeRequest();
        if (userAllocationId == 0l) validationMsg = INVALID_INPUT;
        UserAllocation userAllocation = userAllocationDao.get(userAllocationId);
        if (userAllocation == null && "".equals(validationMsg)) validationMsg = INVALID_USER_ALLOCATION;
        if("".equals(validationMsg)) changeRequest=changeRequestDao.findByTeamAllocationId(userAllocation.getId());
        if("".equals(validationMsg) && changeRequest.getName() !=null) validationMsg=ASSOCIATED_REQUEST;
        if ("".equals(validationMsg)) {
            userAllocationDao.delete(userAllocation);
        }
        return validationMsg;
    }

    @Transactional(readOnly=true)
    public List<UserAllocation> findAll() {
        return userAllocationDao.findAll();
    }


    // create User Allocation object for saving

    private UserAllocation createObjForSave(Map<String, Object> userAllocationObj) throws ParseException {
        UserAllocation userAllocation = new UserAllocation();
        Category category=categoryDao.get(Long.parseLong((String)userAllocationObj.get("categoryId")));
        User itCoordinator=userDao.get(Long.parseLong((String)userAllocationObj.get("itCoordinatorId")));
        User approvedBy=userDao.get(Long.parseLong((String)userAllocationObj.get("approvedById")));

        userAllocation.setId(Long.parseLong((String) userAllocationObj.get("id")));
        userAllocation.setVersion(Long.parseLong((String) userAllocationObj.get("version")));
        userAllocation.setCompanyId(category.getCompanyId());
        userAllocation.setProductId(category.getProductId());
        userAllocation.setCategoryId(category.getId());
        userAllocation.setCategory(category);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser=(User) auth.getPrincipal();
        userAllocation.setUserType(loggedUser.getUserType());
        userAllocation.setItCoordinator(itCoordinator);
        userAllocation.setApprovedBy(approvedBy);
        userAllocation.setClientId(getUserId().getClientId());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        userAllocation.setUpdatedBy(getUserId().getEmail());
        userAllocation.setUpdatedOn(date);
        return userAllocation;

    }

    // check for invalid data
    private String checkInput(UserAllocation userAllocation) {
        String msg = "";

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<UserAllocation>> constraintViolations = validator.validate(userAllocation);
        if (constraintViolations.size() > 0 && "".equals(msg)) msg = INVALID_INPUT;
        return msg;
    }

    private UserAllocation setUpdateCategoryValue(UserAllocation objFromUI,UserAllocation existingUserAllocation) throws ParseException {
        UserAllocation userAllocation = new UserAllocation();
        userAllocation.setId(objFromUI.getId());
        userAllocation.setVersion(objFromUI.getVersion());
        userAllocation.setUserType(objFromUI.getUserType().trim());
        userAllocation.setCompanyId(objFromUI.getCompanyId());
        userAllocation.setProductId(objFromUI.getProductId());
        userAllocation.setCategoryId(objFromUI.getCategoryId());
        userAllocation.setCategory(objFromUI.getCategory());
        userAllocation.setItCoordinator(objFromUI.getItCoordinator());
        userAllocation.setApprovedBy(objFromUI.getApprovedBy());
        userAllocation.setClientId(getUserId().getClientId());
        userAllocation.setCreatedBy(existingUserAllocation.getCreatedBy());
        userAllocation.setCreatedOn(existingUserAllocation.getCreatedOn());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        userAllocation.setUpdatedBy(getUserId().getEmail());
        userAllocation.setUpdatedOn(date);
        return userAllocation;
    }

    private User getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User)auth.getPrincipal();
    }



}
