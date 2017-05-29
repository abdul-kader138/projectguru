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

    @Transactional
    public Map<String, Object> update(Map<String, String> roleObj) throws ParseException {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        Role newObj=new Role();
        validationMsg = checkInput(roleObj);
        Role role = createRoleObj(roleObj);
        Role existingRole = roleDao.get(role.getId());
        if (role.getId() == 0l && validationMsg == "") validationMsg = INVALID_INPUT;
        if (existingRole.getName() == null && validationMsg == "") validationMsg = INVALID_ROLE;
        if (role.getVersion() != existingRole.getVersion() && validationMsg == "") validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) {
            newObj=setUpdateCompanyValue(role, existingRole);
            roleDao.update(newObj);
        }
        obj.put("role",newObj);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public String delete(Long roleId) {
        String validationMsg = "";
        if (roleId == 0l) validationMsg = INVALID_INPUT;
        Role role = roleDao.get(roleId);
        if (role == null && validationMsg == "") validationMsg = INVALID_ROLE;
        //@todo need implement after user implementation
//        List<Object> obj=roleDao.countOfRole(roleId);
//        if (obj.size() > 0 && validationMsg == "") validationMsg = ASSOCIATED_ROLE;

        if ("".equals(validationMsg)) {
            roleDao.delete(role);
        }
        return validationMsg;

    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }


    // check for invalid data
    private String checkInput(Map<String, String> roleObj) throws ParseException {
        String msg = "";

        int num=isRightSelected(roleObj);
        if (roleObj.get("name") == null || roleObj.get("description") == null ||
                num < 0)
            msg = INVALID_INPUT;

        Role role =createRoleObj(roleObj);
        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<Role>> constraintViolations = validator.validate(role);
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

    private Role createRoleObj(Map<String, String> roleObj) throws ParseException {
        Role role = new Role();
        Set<String> rights = new HashSet<>();
        role.setId(Long.parseLong(roleObj.get("id")));
        role.setVersion(Long.parseLong(roleObj.get("version")));
        role.setName(roleObj.get("name"));
        role.setDescription(roleObj.get("description"));
        if (roleObj.get("VIEW_PRIVILEGE") != ""){
            rights.add(roleObj.get("VIEW_PRIVILEGE"));
        }

        if (roleObj.get("WRITE_PRIVILEGE") != ""){
            rights.add(roleObj.get("WRITE_PRIVILEGE"));
        }

        if (roleObj.get("EDIT_PRIVILEGE") != ""){
            rights.add(roleObj.get("EDIT_PRIVILEGE"));
        }

        if (roleObj.get("DELETE_PRIVILEGE") != "")
            rights.add(roleObj.get("DELETE_PRIVILEGE"));
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        role.setCreatedBy(UserDetailServiceImpl.userId);
        role.setCreatedOn(date);
        role.setRights(rights);
        return role;
    }

    private Role setUpdateCompanyValue(Role objFromUI,Role existingRole) throws ParseException {
        Role roleObj = new Role();
        roleObj.setId(objFromUI.getId());
        roleObj.setVersion(objFromUI.getVersion());
        roleObj.setName(objFromUI.getName());
        roleObj.setDescription(objFromUI.getDescription());
        roleObj.setRights(objFromUI.getRights());
        roleObj.setCreatedBy(existingRole.getCreatedBy());
        roleObj.setCreatedOn(existingRole.getCreatedOn());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        roleObj.setUpdatedBy(UserDetailServiceImpl.userId);
        roleObj.setUpdatedOn(date);
        return roleObj;
    }
}
