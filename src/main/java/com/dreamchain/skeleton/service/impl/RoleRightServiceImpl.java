package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.RoleRightDao;
import com.dreamchain.skeleton.dao.RolesDao;
import com.dreamchain.skeleton.model.RoleRight;
import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.RoleRightService;
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
public class RoleRightServiceImpl implements RoleRightService {

    @Autowired
    RoleRightDao roleRightDao;
    @Autowired
    RolesDao rolesDao;
    @Autowired
    Environment environment;


    private static String ROLE_EXISTS = "This role name is already used.Please try again with new one!!!";

    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_ROLE = "Role not exists";
    private static String BACK_DATED_DATA = "Role data is old.Please try again with updated data";

    @Transactional(readOnly = true)
    public RoleRight get(Long id) {
        return roleRightDao.get(id);
    }

    @Transactional
    public Map<String, Object> save(Map<String, Object> roleObj) throws Exception {
        Map<String, Object> obj = new HashMap<>();
        String validationMsg = "";
        RoleRight newRoleRight = new RoleRight();
        validationMsg = checkInput(roleObj);
        RoleRight roleRight = createRoleObj(roleObj);
        if ("".equals(validationMsg)) {
            long roleId = roleRightDao.save(roleRight);
            newRoleRight = roleRightDao.get(roleId);
        }
        obj.put("role", newRoleRight);
        obj.put("validationError", validationMsg);
        return obj;
    }

    @Transactional
    public Map<String, Object> update(Map<String, Object> roleObj) throws ParseException {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        RoleRight newObj=new RoleRight();
        validationMsg = checkInput(roleObj);
        RoleRight roleRight = createRoleObj(roleObj);
        RoleRight existingRoleRight = roleRightDao.get(roleRight.getId());
        if (roleRight.getId() == 0l && validationMsg == "") validationMsg = INVALID_INPUT;
        if (roleRight.getVersion() != existingRoleRight.getVersion() && validationMsg == "") validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) {
            newObj=setUpdateCompanyValue(roleRight, existingRoleRight);
            roleRightDao.update(newObj);
        }
        obj.put("role",newObj);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public String delete(Long roleId) {
        String validationMsg = "";
        if (roleId == 0l) validationMsg = INVALID_INPUT;
        RoleRight roleRight = roleRightDao.get(roleId);
        if (roleRight == null && validationMsg == "") validationMsg = INVALID_ROLE;
        //@todo need implement after user implementation
//        List<Object> obj=roleDao.countOfRole(roleId);
//        if (obj.size() > 0 && validationMsg == "") validationMsg = ASSOCIATED_ROLE;

        if ("".equals(validationMsg)) {
            roleRightDao.delete(roleRight);
        }
        return validationMsg;

    }

    @Override
    public List<RoleRight> findAll() {
        return roleRightDao.findAll();
    }


    // check for invalid data
    private String checkInput(Map<String, Object> roleObj) throws ParseException {
        String msg = "";

        RoleRight roleRight =createRoleObj(roleObj);
        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<RoleRight>> constraintViolations = validator.validate(roleRight);
        if (constraintViolations.size() > 0 && msg == "") msg = INVALID_INPUT;

        return msg;
    }


    private int isRightSelected(Map<String,String> roleObj) {
        int counter= 0;
        if(roleObj.get("VIEW_PRIVILEGE") == null || roleObj.get("VIEW_PRIVILEGE") =="")  counter++;
        if(roleObj.get("WRITE_PRIVILEGE") == null || roleObj.get("WRITE_PRIVILEGE") =="")  counter++;
        if(roleObj.get("EDIT_PRIVILEGE") == null || roleObj.get("EDIT_PRIVILEGE") =="")  counter++;
        if(roleObj.get("DELETE_PRIVILEGE") == null || roleObj.get("DELETE_PRIVILEGE") =="")  counter++;
        return counter;
    }

    private RoleRight createRoleObj(Map<String, Object> roleObj) throws ParseException {
        RoleRight roleRight = new RoleRight();
        Set<String> rights = new HashSet<>();
        roleRight.setId(Long.parseLong((String) roleObj.get("id")));
        roleRight.setVersion(Long.parseLong((String) roleObj.get("version")));
        roleRight.setRoleId(Long.parseLong((String) roleObj.get("roleId")));
        roleRight.setRoles(rolesDao.get(Long.parseLong((String) roleObj.get("version"))));
        if ((String)roleObj.get("VIEW_PRIVILEGE") != ""){
            rights.add((String)roleObj.get("VIEW_PRIVILEGE"));
        }

        if ((String)roleObj.get("WRITE_PRIVILEGE") != ""){
            rights.add((String)roleObj.get("WRITE_PRIVILEGE"));
        }

        if ((String)roleObj.get("EDIT_PRIVILEGE") != ""){
            rights.add((String)roleObj.get("EDIT_PRIVILEGE"));
        }

        if ((String)roleObj.get("DELETE_PRIVILEGE") != "")
            rights.add((String)roleObj.get("DELETE_PRIVILEGE"));
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        roleRight.setCreatedBy(getUserId());
        roleRight.setCreatedOn(date);
        roleRight.setRights(rights);
        return roleRight;
    }

    private RoleRight setUpdateCompanyValue(RoleRight objFromUI,RoleRight existingRoleRight) throws ParseException {
        RoleRight roleRightObj = new RoleRight();
        roleRightObj.setId(objFromUI.getId());
        roleRightObj.setVersion(objFromUI.getVersion());
        roleRightObj.setRoleId(objFromUI.getRoleId());
        roleRightObj.setRights(objFromUI.getRights());
        roleRightObj.setRoles(objFromUI.getRoles());
        roleRightObj.setCreatedBy(existingRoleRight.getCreatedBy());
        roleRightObj.setCreatedOn(existingRoleRight.getCreatedOn());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        roleRightObj.setUpdatedBy(getUserId());
        roleRightObj.setUpdatedOn(date);
        return roleRightObj;
    }


    private String getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user=(User)auth.getPrincipal();
        return user.getEmail();
    }
}