package com.dreamchain.skeleton.service.impl;


import com.dreamchain.skeleton.dao.RolesDao;
import com.dreamchain.skeleton.model.Roles;
import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("classpath:config.properties")
public class RolesServiceImpl implements RolesService {
    @Autowired
    RolesDao rolesDao;
    @Autowired
    Environment environment;


    private static String ROLES_EXISTS = "This roles name is already used.Please try again with new one!!!";
    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_ROLES = "Roles not exists";
    private static String BACK_DATED_DATA = "Roles data is old.Please try again with updated data";
    private static String ASSOCIATED_ROLES = "Roles is tagged with User.First remove tagging and try again";


    @Override
    public Roles get(Long id) {
        return rolesDao.get(id);
    }

    @Transactional
    public Map<String, Object> save(Map<String, Object> rolesObj) throws Exception {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        Roles newRoles=new Roles();
        Roles existingRoles=new Roles();
        Roles roles=createObjForSave(rolesObj);
        validationMsg = checkInput(roles);
        if ("".equals(validationMsg)) existingRoles = rolesDao.findByRolesName(roles.getName());
        if (existingRoles.getName() != null && validationMsg == "") validationMsg = ROLES_EXISTS;
        if ("".equals(validationMsg)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            Date date = dateFormat.parse(dateFormat.format(new Date()));
            roles.setCreatedBy(getUserId());
            roles.setCreatedOn(date);
            long rolesId= rolesDao.save(roles);
            newRoles=rolesDao.get(rolesId);
        }
        obj.put("roles",newRoles);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public Map<String, Object> update(Map<String, Object> rolesObj) throws ParseException {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        Roles newObj=new Roles();
        Roles existingRoles=new Roles();
        Roles roles=createObjForSave(rolesObj);
        validationMsg = checkInput(roles);
        if (roles.getId() == 0l && validationMsg == "") validationMsg = INVALID_INPUT;
        if ("".equals(validationMsg)) existingRoles = rolesDao.get(roles.getId());
        if (existingRoles.getName() == null && validationMsg == "") validationMsg = INVALID_ROLES;
        if (roles.getVersion() != existingRoles.getVersion() && validationMsg == "") validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) newObj = rolesDao.findByNewName(existingRoles.getName(),roles.getName());
        if (newObj.getName() != null && "".equals(validationMsg)) validationMsg = ROLES_EXISTS;
        if ("".equals(validationMsg)) {
            newObj=setUpdateRolesValue(roles, existingRoles);
            rolesDao.update(newObj);
        }
        obj.put("roles",newObj);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public String delete(Long rolesId) {
        String validationMsg = "";
        if (rolesId == 0l) validationMsg = INVALID_INPUT;
        Roles roles = rolesDao.get(rolesId);
        if (roles == null && validationMsg == "") validationMsg = INVALID_ROLES;

        //@todo need implement after User implementation
//        List<Object> obj=departmentDao.countOfDepartment(departmentId);
//        if (obj.size() > 0 && validationMsg == "") validationMsg = ASSOCIATED_COMPANY;

        if ("".equals(validationMsg)) {
            rolesDao.delete(roles);
        }
        return validationMsg;
    }

    @Override
    public List<Roles> findAll() {
        return rolesDao.findAll();
    }


    // check for invalid data
    private String checkInput(Roles roles) {
        String msg = "";

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Roles>> constraintViolations = validator.validate(roles);
        if (constraintViolations.size() > 0 && msg == "") msg = INVALID_INPUT;

        return msg;
    }


    // create Roles object for saving

    private Roles createObjForSave(Map<String, Object> rolesObj){
        Roles roles = new Roles();
        roles.setName(((String) rolesObj.get("name")).trim());
        roles.setDescription(((String) rolesObj.get("description")).trim());
        roles.setId(Long.parseLong((String) rolesObj.get("id")));
        roles.setVersion(Long.parseLong((String) rolesObj.get("version")));
        return roles;

    }


    // create Roles object for updating

    private Roles setUpdateRolesValue(Roles objFromUI,Roles existingRoles) throws ParseException {
        Roles rolesObj = new Roles();
        rolesObj.setId(objFromUI.getId());
        rolesObj.setVersion(objFromUI.getVersion());
        rolesObj.setName(objFromUI.getName().trim());
        rolesObj.setDescription(objFromUI.getDescription().trim());
        rolesObj.setCreatedBy(existingRoles.getCreatedBy());
        rolesObj.setCreatedOn(existingRoles.getCreatedOn());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        rolesObj.setUpdatedBy(getUserId());
        rolesObj.setUpdatedOn(date);
        return rolesObj;
    }

    private String getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user=(User)auth.getPrincipal();
        return user.getEmail();
    }

}
