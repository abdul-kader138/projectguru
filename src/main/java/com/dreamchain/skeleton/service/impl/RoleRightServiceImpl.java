package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.RoleRightDao;
import com.dreamchain.skeleton.dao.RolesDao;
import com.dreamchain.skeleton.dao.UserDao;
import com.dreamchain.skeleton.model.RoleRight;
import com.dreamchain.skeleton.model.Roles;
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
    UserDao userDao;
    @Autowired
    Environment environment;


    private static final String ROLE_EXISTS = "This role name is already used.Please try again with new one!!!";
    private static final String INVALID_INPUT = "Invalid input";
    private static final String INVALID_ROLE = "Role not exists";
    private static final String BACK_DATED_DATA = "Role data is old.Please try again with updated data";
    private static final String ASSOCIATED_RIGHTS = "Rights is tagged with user.First remove tagging and try again";
    private static final String INVALID_PRIVILEGE_UPDATE = "You have not enough privilege to update rights info.Please contact with System Admin!!!";
    private static final String INVALID_PRIVILEGE_DELETE = "You have not enough privilege to delete rights info.Please contact with System Admin!!!";


    @Transactional(readOnly = true)
    public RoleRight get(Long id) {
        return roleRightDao.get(id);
    }

    @Transactional
    public Map<String, Object> save(Map<String, Object> roleObj) throws Exception {
        Map<String, Object> obj = new HashMap<>();
        String validationMsg = "";
        RoleRight newRoleRight = new RoleRight();
        RoleRight roleRight = new RoleRight();
        RoleRight existingRoleRight = new RoleRight();
        validationMsg = checkInput(roleObj);
        if ("".equals(validationMsg)) roleRight = createRoleObj(roleObj);
        if ("".equals(validationMsg)) existingRoleRight = roleRightDao.findByRolesName(roleRight.getRoleId());
        if (existingRoleRight.getRoleName() != null && "".equals(validationMsg)) validationMsg = ROLE_EXISTS;
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
        Map<String, Object> obj = new HashMap<>();
        String validationMsg = "";
        RoleRight newObj = new RoleRight();
        validationMsg = checkInput(roleObj);
        RoleRight roleRight = createRoleObj(roleObj);
        RoleRight existingRoleRight = roleRightDao.get(roleRight.getId());
        if (roleRight.getId() == 0l && "".equals(validationMsg)) validationMsg = INVALID_INPUT;
        if (!getUserId().getClientId().equals(existingRoleRight.getClientId()) && "".equals(validationMsg)) validationMsg = INVALID_PRIVILEGE_UPDATE;
        if (roleRight.getVersion() != existingRoleRight.getVersion() && "".equals(validationMsg))
            validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg))
            newObj = roleRightDao.findByNewName(existingRoleRight.getRoleId(), roleRight.getRoleId());
        if (newObj.getRoleName() != null && "".equals(validationMsg)) validationMsg = ROLE_EXISTS;
        if ("".equals(validationMsg)) {
            newObj = setUpdateCompanyValue(roleRight, existingRoleRight);
            roleRightDao.update(newObj);
        }
        obj.put("role", newObj);
        obj.put("validationError", validationMsg);
        return obj;
    }

    @Transactional
    public String delete(Long roleId) {
        String validationMsg = "";
        User obj=new User();
        if (roleId == 0l) validationMsg = INVALID_INPUT;
        RoleRight roleRight = roleRightDao.get(roleId);
        if (roleRight == null && "".equals(validationMsg)) validationMsg = INVALID_ROLE;
        if (!getUserId().getClientId().equals(roleRight.getClientId()) && "".equals(validationMsg)) validationMsg = INVALID_PRIVILEGE_DELETE;
        if ("".equals(validationMsg)) obj = userDao.findByRoleRightId(roleRight.getId());
        if (obj.getEmail() !=null && "".equals(validationMsg)) validationMsg = ASSOCIATED_RIGHTS;

        if ("".equals(validationMsg)) {
            roleRightDao.delete(roleRight);
        }
        return validationMsg;

    }

    @Transactional(readOnly=true)
    public List<RoleRight> findAll() {
        return roleRightDao.findAll();
    }


    // check for invalid data
    private String checkInput(Map<String, Object> roleObj) throws ParseException {
        String msg = "";

        RoleRight roleRight = createRoleObj(roleObj);
        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<RoleRight>> constraintViolations = validator.validate(roleRight);
        if (constraintViolations.size() > 0 && "".equals(msg)) msg = INVALID_INPUT;

        return msg;
    }


    private int isRightSelected(Map<String, String> roleObj) {
        int counter = 0;
        if (roleObj.get("VIEW_PRIVILEGE") == null || "".equals(roleObj.get("VIEW_PRIVILEGE"))) counter++;
        if (roleObj.get("WRITE_PRIVILEGE") == null || "".equals(roleObj.get("WRITE_PRIVILEGE"))) counter++;
        if (roleObj.get("EDIT_PRIVILEGE") == null || "".equals(roleObj.get("EDIT_PRIVILEGE"))) counter++;
        if (roleObj.get("DELETE_PRIVILEGE") == null || "".equals(roleObj.get("DELETE_PRIVILEGE"))) counter++;
        return counter;
    }

    private RoleRight createRoleObj(Map<String, Object> roleObj) throws ParseException {
        RoleRight roleRight = new RoleRight();
        Roles roles = rolesDao.get(Long.parseLong((String) roleObj.get("roleId")));
        Set<String> rights = new HashSet<>();
        roleRight.setId(Long.parseLong((String) roleObj.get("id")));
        roleRight.setVersion(Long.parseLong((String) roleObj.get("version")));
        roleRight.setRoleId(roles.getId());
        roleRight.setRoleName(roles.getName());
        if (!"".equals((String) roleObj.get("VIEW_PRIVILEGE"))) {
            rights.add((String) roleObj.get("VIEW_PRIVILEGE"));
        }

        if (!"".equals((String) roleObj.get("WRITE_PRIVILEGE"))) {
            rights.add((String) roleObj.get("WRITE_PRIVILEGE"));
        }

        if (!"".equals((String) roleObj.get("EDIT_PRIVILEGE"))) {
            rights.add((String) roleObj.get("EDIT_PRIVILEGE"));
        }

        if (!"".equals((String) roleObj.get("DELETE_PRIVILEGE") )) {
            rights.add((String) roleObj.get("DELETE_PRIVILEGE"));
        }
        roleRight.setClientId(getUserId().getClientId());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        roleRight.setCreatedBy(getUserId().getEmail());
        roleRight.setCreatedOn(date);
        roleRight.setRights(rights);
        return roleRight;
    }

    private RoleRight setUpdateCompanyValue(RoleRight objFromUI, RoleRight existingRoleRight) throws ParseException {
        RoleRight roleRightObj = new RoleRight();
        roleRightObj.setId(objFromUI.getId());
        roleRightObj.setVersion(objFromUI.getVersion());
        roleRightObj.setRoleId(objFromUI.getRoleId());
        roleRightObj.setRights(objFromUI.getRights());
        roleRightObj.setRoleName(objFromUI.getRoleName());
        roleRightObj.setCreatedBy(existingRoleRight.getCreatedBy());
        roleRightObj.setCreatedOn(existingRoleRight.getCreatedOn());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        roleRightObj.setUpdatedBy(getUserId().getEmail());
        roleRightObj.setUpdatedOn(date);
        roleRightObj.setClientId(getUserId().getClientId());
        return roleRightObj;
    }


    private User getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }
}
