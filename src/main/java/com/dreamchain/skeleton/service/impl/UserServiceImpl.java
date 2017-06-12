package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.RoleRightDao;
import com.dreamchain.skeleton.model.RoleRight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamchain.skeleton.dao.UserDao;
import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.UserService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Service(value = "userService")
@PropertySource("classpath:config.properties")
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    @Autowired
    RoleRightDao roleRightDao;
    @Autowired
    Environment environment;

    private static String EMAIL_EXISTS = "This email address already used.Please try again with new one!!!";
    private static String PASSWORD_IS_SAME = "New password matched with previous one.Please try again with new one!!!";
    private static String OLD_PASSWORD_NOT_MATCHED = "Your previous password not matched!!";
    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_USER = "User not exists";
    private static String BACK_DATED_DATA = "User data is old.Please try again with updated data";
    private static String LOGO_PATH = "/resources/images/user_photo/";

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
    public Map<String, Object> save(MultipartHttpServletRequest request) throws Exception {
        Map<String, Object> obj = new HashMap<>();
        Map<String, Object> msg = new HashMap<>();
        String validationMsg = "";
        User newUser = new User();
        User existingUser = new User();
        User user = createObjForSave(request);
        validationMsg = checkInput(user);
        if ("".equals(validationMsg)) existingUser = userDao.findByUserName(user.getEmail());
        if (existingUser != null && validationMsg == "") validationMsg = EMAIL_EXISTS;
        if ("".equals(validationMsg)) msg = fileSave(request);
        if (msg.get("validationMsg") == "") user.setImagePath((String) msg.get("path"));
        if ("".equals(validationMsg)) {
            long companyId = userDao.save(user);
            newUser = userDao.get(companyId);
        }
        obj.put("user", newUser);
        obj.put("validationError", validationMsg);
        return obj;

    }



    @Transactional
    public Map<String, Object> updateUser(MultipartHttpServletRequest request) throws Exception {
        Map<String, Object> obj = new HashMap<>();
        Map<String, Object> msg = new HashMap<>();
        String validationMsg = "";
        User newUser = new User();
        User existingUser = new User();
        User user = createObjForSave(request);
        validationMsg = checkInput(user);
        if ("".equals(validationMsg)) existingUser = userDao.get(user.getId());
        if (existingUser.getName() == null && "".equals(validationMsg)) validationMsg = INVALID_USER;
        if (user.getVersion() != existingUser.getVersion() && "".equals(validationMsg)) validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) newUser = userDao.findByNewName(existingUser.getEmail(),user.getEmail());
        if (newUser.getName() != null && "".equals(validationMsg)) validationMsg = EMAIL_EXISTS;
        String fileName = request.getFile("photo").getOriginalFilename();
        if (!("".equals(fileName))) {
            validationMsg = deleteLogo(request.getRealPath(
                    "/"), existingUser.getImagePath());
            if ("".equals(validationMsg)) msg = fileSave(request);
            if ("".equals(validationMsg)) existingUser.setImagePath((String) msg.get("path"));
        }
        if ("".equals(validationMsg)) {
            newUser = setUpdateUserValue(user, existingUser);
            userDao.update(newUser);
        }
        obj.put("user", newUser);
        obj.put("validationError", validationMsg);
        return obj;
    }




    @Transactional
    public String delete(Long userId,HttpServletRequest request) {
        String validationMsg = "";
        String fileName = "";
        if (userId == 0l) validationMsg = INVALID_INPUT;
        User user = userDao.get(userId);
        if (user == null && "".equals(validationMsg)) validationMsg = INVALID_USER;

        // later implementation
//        List<Object> obj = userDao.countOfCompany(companyId);
//        if (obj.size() > 0 && "".equals(validationMsg)) validationMsg = ASSOCIATED_COMPANY;
        if (user != null) fileName = user.getImagePath();
        if ("".equals(validationMsg)) validationMsg = deleteLogo(request.getRealPath("/"),fileName);
        if ("".equals(validationMsg)) {
            userDao.delete(user);
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

//    private User setUserRole(User oldUser) {
//        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
//        User newUser = new User(oldUser.getName(), oldUser.getPassword(), true, true, true, true, grantedAuthorities, oldUser.getName(), oldUser.getEmail(), oldUser.getRole(), oldUser.getPhone(), oldUser.getRights(), oldUser.getCreatedBy(), oldUser.getUpdatedBy(), oldUser.getCreatedOn(), oldUser.getUpdatedOn());
//        if (environment.getProperty("role.admin").equals(oldUser.getRole())) newUser.setRole(Role.ROLE_ADMIN.name());
//        if (environment.getProperty("role.super.admin").equals(oldUser.getRole()))
//            newUser.setRole(Role.ROLE_SUPER_ADMIN.name());
//        if (environment.getProperty("role.user").equals(oldUser.getRole())) newUser.setRole(Role.ROLE_USER.name());
//        if (environment.getProperty("role.super.other").equals(oldUser.getRole()))
//            newUser.setRole(Role.ROLE_OTHER.name());
//        return newUser;
//    }


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



    // create user object for saving

    private User createObjForSave(MultipartHttpServletRequest request) throws Exception {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(request.getParameter("password"));
        RoleRight roleRight=roleRightDao.findByRolesName(Long.parseLong(request.getParameter("roleId")));
        Set rightList=new HashSet();
        if(roleRight !=null) rightList.addAll(roleRight.getRights());
        User user = new User();
        user.setName(request.getParameter("name").trim());
        user.setPassword(encodedPassword);
        user.setEmail(request.getParameter("email").trim());
        user.setPhone(request.getParameter("phone").trim());
        user.setDesignation(request.getParameter("designation").trim());
        user.setRole(request.getParameter("roleName"));
        user.setRights(rightList);
        user.setImagePath(request.getFile("photo").getOriginalFilename());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        user.setCreatedBy(getUserId());
        user.setCreatedOn(date);
        return user;

    }

    private User setUpdateUserValue(User objFromUI, User existingUser) throws ParseException {
        User userObj = new User();
        userObj.setId(objFromUI.getId());
        userObj.setVersion(objFromUI.getVersion());
        userObj.setName(objFromUI.getName().trim());
        userObj.setEmail(objFromUI.getEmail().trim());
        userObj.setPhone(objFromUI.getPhone().trim());
        userObj.setDesignation(objFromUI.getDesignation().trim());
        userObj.setRole(objFromUI.getRole());
        userObj.setRights(objFromUI.getRights());
        userObj.setImagePath(existingUser.getImagePath());
        userObj.setCreatedBy(existingUser.getCreatedBy());
        userObj.setCreatedOn(existingUser.getCreatedOn());
        userObj.setPassword(existingUser.getPassword());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        userObj.setUpdatedBy(getUserId());
        userObj.setUpdatedOn(date);
        return userObj;
    }


    private Map fileSave(MultipartHttpServletRequest request) {
        Map<String, String> msg = new HashMap<>();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        MultipartFile multipartFile = request.getFile("photo");
        String fileName = multipartFile.getOriginalFilename();
        Random rand = new Random();
        int n = rand.nextInt(1000) + 1;
        fileName = n + "_" + fileName; // create new name for logo file
        try {
            String filePath = LOGO_PATH + fileName;
            String realPathFetch = request.getRealPath( "/");
            inputStream = multipartFile.getInputStream();
            File newFile = new File(realPathFetch + filePath);
            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
            msg.put("validationMsg", "");
            msg.put("path", filePath.trim());
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            msg.put("validationMsg", e.getMessage());
            msg.put("path", "");
        }
        return msg;

    }


    private String deleteLogo(String realPathFetch, String fileName) {
        String msg = "";
        try {
            File file = new File(realPathFetch+fileName);
            file.setWritable(true);
            if (file.delete()) msg = "";
            else msg = environment.getProperty("user.file.delete.success.msg");
        } catch (Exception e) {
            msg = e.getMessage();
        }
        return msg;
    }

    // create company object for updating



    private String getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user=(User)auth.getPrincipal();
        return user.getEmail();
    }


}
