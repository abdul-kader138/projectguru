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
    UserDao userDao ;
    @Autowired
    Environment environment;


    private static String USER_ALLOCATION_EXISTS = "This users already used.Please try again with new one!!!";
    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_USER_ALLOCATION = "User allocation not exists";
    private static String BACK_DATED_DATA = "User allocation data is old.Please try again with updated data";
    private static String ASSOCIATED_REQUEST = "User is allocated with request.First remove tagging and try again";

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
        if ("".equals(validationMsg)) existingUserAllocation = userAllocationDao.findByUserId(userAllocation.getItCoordinatorId(),
                userAllocation.getApprovedById(),userAllocation.getCompanyId(),
                userAllocation.getProductId(),userAllocation.getCategoryId());
//                userAllocation.getDepartmentId(),userAllocation.getProductId(),userAllocation.getCategoryId());
        if (existingUserAllocation.getCategoryName() != null && validationMsg == "") validationMsg = USER_ALLOCATION_EXISTS;
        if ("".equals(validationMsg)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            Date date = dateFormat.parse(dateFormat.format(new Date()));
            userAllocation.setCreatedBy(getUserId());
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
        UserAllocation newObj=new UserAllocation();
        UserAllocation existingUserAllocation=new UserAllocation();
        UserAllocation userAllocation=createObjForSave(userAllocationObj);
        validationMsg = checkInput(userAllocation);
        if (userAllocation.getId() == 0l && validationMsg == "") validationMsg = INVALID_INPUT;
        if ("".equals(validationMsg)) existingUserAllocation = userAllocationDao.get(userAllocation.getId());
        if (existingUserAllocation == null && validationMsg == "") validationMsg = INVALID_USER_ALLOCATION;
        if (userAllocation.getVersion() != existingUserAllocation.getVersion() && validationMsg == "") validationMsg = BACK_DATED_DATA;
//        if ("".equals(validationMsg)) newObj = categoryDao.findByNewName(existingUserAllocation.getName(),category.getName(),category.getCompanyId(),category.getDepartmentId(),category.getProductId());
//        if (newObj.getName() != null && "".equals(validationMsg)) validationMsg = CATEGORY_EXISTS;
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
        if (userAllocationId == 0l) validationMsg = INVALID_INPUT;
        UserAllocation userAllocation = userAllocationDao.get(userAllocationId);
        if (userAllocation == null && validationMsg == "") validationMsg = INVALID_USER_ALLOCATION;

        //@todo need implement after Request implementation
//        List<Object> obj=departmentDao.countOfDepartment(departmentId);
//        if (obj.size() > 0 && validationMsg == "") validationMsg = ASSOCIATED_COMPANY;

        if ("".equals(validationMsg)) {
            userAllocationDao.delete(userAllocation);
        }
        return validationMsg;
    }

    @Override
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
        userAllocation.setCompanyName(category.getCompanyName());
//        userAllocation.setDepartmentId(category.getDepartmentId());
//        userAllocation.setDepartmentName(category.getDepartment().getName());
        userAllocation.setProductId(category.getProductId());
        userAllocation.setProductName(category.getProductName());
        userAllocation.setCategoryId(category.getId());
        userAllocation.setCategoryName(category.getName());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser=(User) auth.getPrincipal();
        userAllocation.setUserType(loggedUser.getUserType());
        userAllocation.setItCoordinatorId(itCoordinator.getId());
        userAllocation.setItCoordinator(itCoordinator.getName());
        userAllocation.setApprovedById(approvedBy.getId());
        userAllocation.setApprovedBy(approvedBy.getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        userAllocation.setUpdatedBy(getUserId());
        userAllocation.setUpdatedOn(date);
        return userAllocation;

    }

    // check for invalid data
    private String checkInput(UserAllocation userAllocation) {
        String msg = "";

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<UserAllocation>> constraintViolations = validator.validate(userAllocation);
        if (constraintViolations.size() > 0 && msg == "") msg = INVALID_INPUT;

        return msg;
    }

    private UserAllocation setUpdateCategoryValue(UserAllocation objFromUI,UserAllocation existingUserAllocation) throws ParseException {
        UserAllocation userAllocation = new UserAllocation();
        userAllocation.setId(objFromUI.getId());
        userAllocation.setVersion(objFromUI.getVersion());
        userAllocation.setUserType(objFromUI.getUserType().trim());
        userAllocation.setCompanyId(objFromUI.getCompanyId());
        userAllocation.setCompanyName(objFromUI.getCompanyName());
//        userAllocation.setDepartmentId(objFromUI.getDepartmentId());
//        userAllocation.setDepartmentName(objFromUI.getDepartmentName());
        userAllocation.setProductId(objFromUI.getProductId());
        userAllocation.setProductName(objFromUI.getProductName());
        userAllocation.setCategoryId(objFromUI.getCategoryId());
        userAllocation.setCategoryName(objFromUI.getCategoryName());
        userAllocation.setItCoordinatorId(objFromUI.getItCoordinatorId());
        userAllocation.setItCoordinator(objFromUI.getItCoordinator());
        userAllocation.setApprovedById(objFromUI.getApprovedById());
        userAllocation.setApprovedBy(objFromUI.getApprovedBy());
        userAllocation.setCreatedBy(existingUserAllocation.getCreatedBy());
        userAllocation.setCreatedOn(existingUserAllocation.getCreatedOn());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        userAllocation.setUpdatedBy(getUserId());
        userAllocation.setUpdatedOn(date);
        return userAllocation;
    }

    private String getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user=(User)auth.getPrincipal();
        return user.getEmail();
    }


}
