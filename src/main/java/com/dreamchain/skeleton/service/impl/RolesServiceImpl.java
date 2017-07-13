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


    private static final String ROLES_EXISTS = "This roles name is already used.Please try again with new one!!!";
    private static final String INVALID_INPUT = "Invalid input";
    private static final String INVALID_ROLES = "Roles not exists";
    private static final String BACK_DATED_DATA = "Roles data is old.Please try again with updated data";
    private static final String ASSOCIATED_ROLES = "Roles is tagged with rights.First remove tagging and try again";
    private static final String INVALID_PRIVILEGE_UPDATE = "You have not enough privilege to update role info.Please contact with System Admin!!!";
    private static final String INVALID_PRIVILEGE_DELETE = "You have not enough privilege to delete role info.Please contact with System Admin!!!";



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
        if (existingRoles.getName() != null && "".equals(validationMsg)) validationMsg = ROLES_EXISTS;
        if ("".equals(validationMsg)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            Date date = dateFormat.parse(dateFormat.format(new Date()));
            roles.setCreatedBy(getUserId().getEmail());
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
        if (roles.getId() == 0l && "".equals(validationMsg)) validationMsg = INVALID_INPUT;
        if ("".equals(validationMsg)) existingRoles = rolesDao.get(roles.getId());
        if (existingRoles.getName() == null && "".equals(validationMsg)) validationMsg = INVALID_ROLES;
        if (!getUserId().getClientId().equals(existingRoles.getClientId()) && "".equals(validationMsg)) validationMsg = INVALID_PRIVILEGE_UPDATE;
        if (roles.getVersion() != existingRoles.getVersion() && "".equals(validationMsg)) validationMsg = BACK_DATED_DATA;
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
        List<Object> obj=new ArrayList<>();
        if (rolesId == 0l) validationMsg = INVALID_INPUT;
        Roles roles = rolesDao.get(rolesId);
        if (roles == null && "".equals(validationMsg)) validationMsg = INVALID_ROLES;
        if (!getUserId().getClientId().equals(roles.getClientId()) && "".equals(validationMsg)) validationMsg = INVALID_PRIVILEGE_DELETE;
        if ("".equals(validationMsg)) obj=rolesDao.countOfRoles(rolesId);
        if (obj.size() > 0 && "".equals(validationMsg)) validationMsg = ASSOCIATED_ROLES;
        if ("".equals(validationMsg)) {
            rolesDao.delete(roles);
        }
        return validationMsg;
    }

    @Transactional(readOnly=true)
    public List<Roles> findAll() {
        return rolesDao.findAll();
    }


    // check for invalid data
    private String checkInput(Roles roles) {
        String msg = "";

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Roles>> constraintViolations = validator.validate(roles);
        if (constraintViolations.size() > 0 && "".equals(msg)) msg = INVALID_INPUT;

        return msg;
    }


    // create Roles object for saving

    private Roles createObjForSave(Map<String, Object> rolesObj){
        Roles roles = new Roles();
        roles.setName(((String) rolesObj.get("name")).trim());
        roles.setDescription(((String) rolesObj.get("description")).trim());
        roles.setId(Long.parseLong((String) rolesObj.get("id")));
        roles.setVersion(Long.parseLong((String) rolesObj.get("version")));
        roles.setClientId(getUserId().getClientId());
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
        rolesObj.setUpdatedBy(getUserId().getEmail());
        rolesObj.setUpdatedOn(date);
        rolesObj.setClientId(getUserId().getClientId());
        return rolesObj;
    }

    private User getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User)auth.getPrincipal();
    }

}

