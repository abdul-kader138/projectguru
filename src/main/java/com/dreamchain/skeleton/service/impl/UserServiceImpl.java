package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.list.content.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamchain.skeleton.dao.UserDao;
import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.UserService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service(value = "userService")
@PropertySource("classpath:config.properties")
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    @Autowired
    Environment environment;

    private static String EMAIL_EXISTS = "This email address already used.Please try again with new one!!!";
    private static String PASSWORD_IS_SAME = "New password matched with previous one.Please try again with new one!!!";
    private static String OLD_PASSWORD_NOT_MATCHED = "Your previous password not matched!!";
    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_USER = "User not exists";
    private static String BACK_DATED_DATA = "User data is old.Please try again with updated data";

    @Transactional(readOnly = true)
    public User get(Long id) {
        return userDao.get(id);
    }

    @Transactional
    public void delete(User user) {
        userDao.delete(user);
    }


    @Transactional(readOnly = true)
    public List<User> findAll(String userName) {
        return userDao.findAll(userName);
    }

    @Transactional
    public String save(User user) throws ParseException {
        String validationMsg = "";
        validationMsg = checkInput(user);
        User existingUser = userDao.findByUserName(user.getEmail());
        if (existingUser != null && validationMsg == "") validationMsg = EMAIL_EXISTS;
        if ("".equals(validationMsg)) {
            User userObj = setUserRole(user);
            userObj = setEditorInfo(userObj, "save");
            BCryptPasswordEncoder encodePassword = new BCryptPasswordEncoder();
            String newEncodedPassword = encodePassword.encode(userObj.getPassword());
            userObj.setPassword(newEncodedPassword);
            userDao.save(userObj);
        }
        return validationMsg;
    }

    @Transactional
    public String updateUser(User user) throws ParseException {
        String validationMsg = "";
        validationMsg = checkInput(user);
        User existingUser = userDao.get(user.getId());
        if (existingUser == null && validationMsg == "") validationMsg = INVALID_USER;
        if (user.getVersion() != existingUser.getVersion() && validationMsg == "") validationMsg = BACK_DATED_DATA;
            //@todo
            // email check now omit bcz it is not send from user side
            //if (validationMsg == "") validationMsg = checkForDuplicateEmail(existingUser, user.getEmail());
        if ("".equals(validationMsg)) {
            User newObj=setUpdateUserValue(user,existingUser);
            userDao.remove(existingUser);
            userDao.update(newObj);
        }
        return validationMsg;
    }




    @Transactional
    public String delete(Long userId) {
        String validationMsg = "";
        if (userId == null) validationMsg = INVALID_INPUT;
        User existingUser = userDao.get(userId);
        if (existingUser == null && validationMsg == "") validationMsg = INVALID_USER;
        if ("".equals(validationMsg)) {
            userDao.delete(existingUser);
        }
        return validationMsg;
    }





    @Transactional
    public String changePassword(String userName, String oldPassword, String newPassword) throws Exception {
        String validationMsg = "";
        //check for valid input
        validationMsg = checkInput(userName, oldPassword, newPassword);
        User user = userDao.findByUserName(userName);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(newPassword);
        //check new password
        if (encoder.matches(newPassword, user.getPassword()) && validationMsg == "") validationMsg = PASSWORD_IS_SAME;
        //check previous password matched or not
        if (!encoder.matches(oldPassword, user.getPassword()) && validationMsg == "")
            validationMsg = OLD_PASSWORD_NOT_MATCHED;
        if ("".equals(validationMsg)) {
            user.setPassword(encodedPassword);
            userDao.update(user);
        }
        return validationMsg;
    }

    @Override
    public User findByUserName(String username) {
        return userDao.findByUserName(username);
    }

    @Override
    public List<User> findAll() {
        return userDao.findAll();
    }

    private String checkInput(User user) {
        String msg = "";
        if (user.getName() == null || user.getEmail() == null
                || user.getPhone() == null || user.getPassword() == null || user.getRole() == null)
            msg = INVALID_INPUT;

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        if (constraintViolations.size() > 0 && msg == "") msg = INVALID_INPUT;

        return msg;
    }

    private String checkInput(String userName, String oldPassword, String newPassword) {
        String msg = "";
        if (oldPassword == null || newPassword == null || userName == null)
            msg = INVALID_INPUT;
        return msg;

    }


    /*
      @ set user role based on user user type
     */

    private User setUserRole(User oldUser) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
        User newUser = new User(oldUser.getName(), oldUser.getPassword(), true, true, true, true, grantedAuthorities, oldUser.getName(), oldUser.getEmail(), oldUser.getRole(), oldUser.getPhone(), oldUser.getCreatedBy(), oldUser.getUpdatedBy(), oldUser.getCreatedOn(), oldUser.getUpdatedOn());
        if (environment.getProperty("role.admin").equals(oldUser.getRole())) newUser.setRole(Role.ROLE_ADMIN.name());
        if (environment.getProperty("role.super.admin").equals(oldUser.getRole()))
            newUser.setRole(Role.ROLE_SUPER_ADMIN.name());
        if (environment.getProperty("role.user").equals(oldUser.getRole())) newUser.setRole(Role.ROLE_USER.name());
        if (environment.getProperty("role.super.other").equals(oldUser.getRole()))
            newUser.setRole(Role.ROLE_OTHER.name());
        return newUser;
    }


    private User setEditorInfo(User user, String action) throws ParseException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentLoggedInUser = (User) auth.getPrincipal();
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        if ("save".equals(action)) {
            user.setCreatedBy(currentLoggedInUser.getEmail());
            user.setCreatedOn(date);
            user.setUpdatedBy("");
        }
        if ("update".equals(action)) {
            user.setUpdatedOn(date);
            user.setUpdatedBy(currentLoggedInUser.getEmail());
        }

        return user;

    }


    private String checkForDuplicateEmail(User user, String email) {
        String msg = "";
        if (!email.equals(user.getEmail().toString())) {
            User duplicateObj = userDao.findByUserName(email);
            if (duplicateObj != null) msg = EMAIL_EXISTS;
        }
        return msg;
    }

    private User setUpdateUserValue(User objFromUI,User existingUser) throws ParseException {
        User userObj = setUserRole(objFromUI);
        userObj.setId(objFromUI.getId());
        userObj.setVersion(objFromUI.getVersion());
        userObj = setEditorInfo(userObj, "update");
        userObj.setCreatedBy(existingUser.getCreatedBy());
        userObj.setCreatedOn(existingUser.getCreatedOn());
        userObj.setPassword(existingUser.getPassword());
        return userObj;
    }


}
