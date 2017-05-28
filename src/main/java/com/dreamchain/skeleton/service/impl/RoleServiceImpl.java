package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.RoleDao;
import com.dreamchain.skeleton.model.Role;
import com.dreamchain.skeleton.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
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
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDao roleDao;
    @Autowired
    Environment environment;


    private static String ROLE_EXISTS = "This role name is already used.Please try again with new one!!!";
    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_ROLE = "Role not exists";
    private static String BACK_DATED_DATA = "Role data is old.Please try again with updated data";

    @Transactional(readOnly = true)
    public Role get(Long id) {
        return roleDao.get(id);
    }

    @Transactional
    public Map<String, Object> save(Map<String, String> roleObj) throws Exception {
        Map<String, Object> obj = new HashMap<>();
        String validationMsg = "";
        Role newRole = new Role();
        validationMsg = checkInput(roleObj);
        Role role = createRoleObj(roleObj);
        Role existingRole = roleDao.findByRoleName(role.getName());
        if (existingRole.getName() != null && validationMsg == "") validationMsg = ROLE_EXISTS;
        if ("".equals(validationMsg)) {
            long roleId = roleDao.save(role);
            newRole = roleDao.get(roleId);
        }
        obj.put("role", newRole);
        obj.put("validationError", validationMsg);
        return obj;
    }

    @Override
    public Map<String, Object> update(Map<String, String> roleObj) throws ParseException {
        return null;
    }

    @Override
    public String delete(Long roleId) {
        return null;
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }


    // check for invalid data
    private String checkInput(Map<String, String> roleObj) throws ParseException {
        String msg = "";

        if (roleObj.get("name") == null || roleObj.get("description") == null ||
                roleObj.get("READ_PREVILEGE") == null)
            msg = INVALID_INPUT;

        Role role =createRoleObj(roleObj);
        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Role>> constraintViolations = validator.validate(role);
        if (constraintViolations.size() > 0 && msg == "") msg = INVALID_INPUT;

        return msg;
    }

    private Role createRoleObj(Map<String, String> roleObj) throws ParseException {
        Role role = new Role();
        Set<String> rights = new HashSet<>();
        role.setName(roleObj.get("name"));
        role.setDescription(roleObj.get("description"));
        if (roleObj.get("READ_PREVILEGE") != ""){
            rights.add(roleObj.get("READ_PREVILEGE"));
        }

        if (roleObj.get("WRITE_PREVILEGE") != ""){
            rights.add(roleObj.get("WRITE_PREVILEGE"));
        }

        if (roleObj.get("EDIT_PREVILEGE") != ""){
            rights.add(roleObj.get("EDIT_PREVILEGE"));
        }

        if (roleObj.get("DELETE_PREVILEGE") != "")
            rights.add(roleObj.get("DELETE_PREVILEGE"));
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        role.setCreatedBy(UserDetailServiceImpl.userId);
        role.setCreatedOn(date);
        role.setRights(rights);
        return role;
    }
}
