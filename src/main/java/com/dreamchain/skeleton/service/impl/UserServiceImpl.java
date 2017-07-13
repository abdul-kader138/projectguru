package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.*;
import com.dreamchain.skeleton.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    CompanyDao companyDao;
    @Autowired
    UserAllocationDao userAllocationDao;
    @Autowired
    UserDao userDao;
    @Autowired
    RoleRightDao roleRightDao;
    @Autowired
    RolesDao rolesDao;
    @Autowired
    ApprovalStatusDao approvalStatusDao;
    @Autowired
    Environment environment;

    private static final String EMAIL_EXISTS = "This email address already used.Please try again with new one!!!";
    private static final String PASSWORD_IS_SAME = "New password matched with previous one.Please try again with new one!!!";
    private static final String OLD_PASSWORD_NOT_MATCHED = "Your previous password not matched!!";
    private static final String INVALID_INPUT = "Invalid input";
    private static final String INVALID_USER = "User not exists";
    private static final String USER_ASSOCIATED_APPROVAL_UPDATE = "User can't update due to association with request";
    private static final String USER_ASSOCIATED_APPROVAL_DELETE = "User can't update due to association with request";
    private static final String BACK_DATED_DATA = "User data is old.Please try again with updated data";
    private static final String PHOTO_USER_PATH = "/resources/images/user_photo/";
    private static final String PHOTO_TEAM_PATH = "/resources/images/team_member_photo/";
    private static final String USER_ASSOCIATED_TEAM_ALLOCATION = "User can't delete due to association with allocation";

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
    public Map<String, Object> save(MultipartHttpServletRequest request, String usersType) throws Exception {
        Map<String, Object> obj = new HashMap<>();
        Map<String, Object> msg = new HashMap<>();
        String validationMsg = "";
        User newUser = new User();
        User existingUser = new User();
        User user = createObjForSave(request, "save");
        validationMsg = checkInput(user);
        if ("".equals(validationMsg)) existingUser = userDao.findByUserName(user.getEmail());
        if (existingUser != null && "".equals(validationMsg)) validationMsg = EMAIL_EXISTS;
        if ("".equals(validationMsg) && "user".equals(usersType)) msg = fileSave(request, PHOTO_USER_PATH);
        if ("".equals(validationMsg) && "team_member".equals(usersType)) msg = fileSave(request, PHOTO_TEAM_PATH);
        if ("".equals(msg.get("validationMsg"))) user.setImagePath((String) msg.get("path"));
        if ("".equals(validationMsg)) {
            user.setClientId(environment.getProperty("user.vendor.id"));
            user.setUserType(environment.getProperty("user.type.vendor"));
            long companyId = userDao.save(user);
            newUser = userDao.get(companyId);
        }
        obj.put("user", newUser);
        obj.put("validationError", validationMsg);
        return obj;

    }


    @Transactional
    public Map<String, Object> updateUser(MultipartHttpServletRequest request, String usersType) throws Exception {
        Map<String, Object> obj = new HashMap<>();
        Map<String, Object> msg = new HashMap<>();
        List<ApprovalStatus> approvalStatuses=new ArrayList<>();
        String validationMsg = "";
        User newUser = new User();
        User existingUser = new User();
        User user = createObjForSave(request, "update");
        validationMsg = checkInput(user);
        if ("".equals(validationMsg)) existingUser = userDao.get(user.getId());
        if (existingUser.getName() == null && "".equals(validationMsg)) validationMsg = INVALID_USER;
        if ("".equals(validationMsg)) approvalStatuses= approvalStatusDao.findByApprovedById(existingUser.getId());
        if(approvalStatuses.size() !=0 && "".equals(validationMsg) && user.getCompany().getId() !=existingUser.getCompany().getId()) validationMsg=USER_ASSOCIATED_APPROVAL_UPDATE;
        if (user.getVersion() != existingUser.getVersion() && "".equals(validationMsg)) validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) newUser = userDao.findByNewName(existingUser.getEmail(), user.getEmail());
        if (newUser.getName() != null && "".equals(validationMsg)) validationMsg = EMAIL_EXISTS;
        String fileName = request.getFile("photo").getOriginalFilename();
        if (!("".equals(fileName))) {
            validationMsg = deletePhoto(request.getRealPath(
                    "/"), existingUser.getImagePath());
            if ("".equals(validationMsg) && "user".equals(usersType)) msg = fileSave(request, PHOTO_USER_PATH);
            if ("".equals(validationMsg) && "team_member".equals(usersType)) msg = fileSave(request, PHOTO_TEAM_PATH);
            if ("".equals(validationMsg)) existingUser.setImagePath((String) msg.get("path"));
        }
        if ("".equals(validationMsg)) {
                user.setClientId(getUserId().getClientId());
                user.setUserType(getUserId().getUserType());
            newUser = setUpdateUserValue(user, existingUser);
            userDao.update(newUser);
        }
        obj.put("user", newUser);
        obj.put("validationError", validationMsg);
        return obj;
    }


    @Transactional
    public String delete(Long userId, HttpServletRequest request) {
        String validationMsg = "";
        String fileName = "";
        List<UserAllocation> objList=new ArrayList<>();
        List<ApprovalStatus> approvalStatuses=new ArrayList<>();
        if (userId == 0l) validationMsg = INVALID_INPUT;
        User user = userDao.get(userId);
        if (user == null && "".equals(validationMsg)) validationMsg = INVALID_USER;
        if ("".equals(validationMsg)) objList=userAllocationDao.AllAllocationByApprovedBy(user.getId());
        if (objList.size() > 0 && "".equals(validationMsg)) validationMsg = USER_ASSOCIATED_TEAM_ALLOCATION;
        if ("".equals(validationMsg)) objList=userAllocationDao.AllAllocationByItCoordinator(user.getId());
        if (objList.size() > 0 && "".equals(validationMsg)) validationMsg = USER_ASSOCIATED_TEAM_ALLOCATION;
        if ("".equals(validationMsg)) approvalStatuses= approvalStatusDao.findByApprovedById(user.getId());
        if ("".equals(validationMsg)) approvalStatuses= approvalStatusDao.findByApprovedById(user.getId());
        if(approvalStatuses.size() !=0 && "".equals(validationMsg)) validationMsg=USER_ASSOCIATED_APPROVAL_DELETE;
        if (user != null) fileName = user.getImagePath();
        if ("".equals(validationMsg)) validationMsg = deletePhoto(request.getRealPath("/"), fileName);
        if ("".equals(validationMsg)) {
            userDao.delete(user);
        }
        return validationMsg;
    }


    @Transactional
    public Map changePassword(String oldPassword, String newPassword) throws Exception {
        String validationMsg = "";
        Map<String, Object> obj = new HashMap<>();
        //check for valid input
        validationMsg = checkPasswordInput(oldPassword, newPassword);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = (User) auth.getPrincipal();
        User user = userDao.findByUserName(loggedUser.getEmail());
        if (user == null) validationMsg = INVALID_USER;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(newPassword);
        //check previous password matched or not
        if (!encoder.matches(oldPassword, user.getPassword()) && "".equals(validationMsg))
            validationMsg = OLD_PASSWORD_NOT_MATCHED;
        //check new password
        if (encoder.matches(newPassword, user.getPassword()) && "".equals(validationMsg)) validationMsg = PASSWORD_IS_SAME;
        if ("".equals(validationMsg)) {
            user.setPassword(encodedPassword);
            userDao.update(user);
        }
        obj.put("validationError", validationMsg);
        return obj;
    }

    @Transactional(readOnly=true)
    public User findByUserName(String username) {
        return userDao.findByUserName(username);
    }

    @Transactional(readOnly=true)
    public List<User> findAll() {
        return userDao.findAll();
    }

    private String checkInput(User user) {
        String msg = "";

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(user);
        if (constraintViolations.size() > 0 && "".equals(msg)) msg = INVALID_INPUT;

        return msg;
    }

    private String checkPasswordInput(String oldPassword, String newPassword) {
        String msg = "";
        if (oldPassword == null || newPassword == null)
            msg = INVALID_INPUT;
        return msg;

    }


    // set user value based on operation type

    private User setEditorInfo(String action, MultipartHttpServletRequest request) throws ParseException {
        User user = new User();
        String encodedPassword = "";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if ("save".equals(action)) encodedPassword = encoder.encode(request.getParameter("password"));
        if ("update".equals(action)) encodedPassword = encoder.encode(environment.getProperty("user.default.password"));
        if ("save".equals(action)) user.setEmail(request.getParameter("email").trim());
        if ("update".equals(action)) user.setEmail(environment.getProperty("user.default.email"));
        if ("save".equals(action)) user.setImagePath(request.getFile("photo").getOriginalFilename());
        if ("update".equals(action)) user.setImagePath("/resources");
        user.setPassword(encodedPassword);
        return user;

    }


    // create user object for saving

    private User createObjForSave(MultipartHttpServletRequest request, String operationName) throws Exception {
        String roleName = request.getParameter("roleName");
        Long roleId = Long.parseLong(request.getParameter("roleId"));
        RoleRight roleRight = roleRightDao.findByRolesName(roleId);
        Company company = companyDao.get(Long.parseLong(request.getParameter("companyId")));
        User user = setEditorInfo(operationName, request);
        user.setId(Long.parseLong(request.getParameter("id")));
        user.setVersion(Long.parseLong(request.getParameter("version")));
        user.setName(request.getParameter("name").trim());
        user.setPhone(request.getParameter("phone").trim());
        user.setDesignation(request.getParameter("designation").trim());
        user.setUserType("client");
        user.setClientId(environment.getProperty("user.vendor.id"));
        user.setRoleRight(roleRight);
        user.setRoleRightsId(roleRight.getId());
        user.setCompanyId(company.getId());
        user.setCompany(company);
        user.setRole(roleName);
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        user.setCreatedBy(getUserId().getEmail());
        user.setCreatedOn(date);
        return user;

    }


    private User setUpdateUserValue(User objFromUI, User existingUser) throws ParseException {
        User userObj = new User();
        userObj.setId(objFromUI.getId());
        userObj.setVersion(objFromUI.getVersion());
        userObj.setName(objFromUI.getName().trim());
        userObj.setPhone(objFromUI.getPhone().trim());
        userObj.setDesignation(objFromUI.getDesignation().trim());
        userObj.setRole(objFromUI.getRole());
        userObj.setRoleRight(objFromUI.getRoleRight());
        userObj.setRoleRightsId(objFromUI.getRoleRightsId());
        userObj.setCompany(objFromUI.getCompany());
        userObj.setCompanyId(objFromUI.getCompanyId());
        userObj.setImagePath(existingUser.getImagePath());
        userObj.setEmail(existingUser.getEmail());
        userObj.setCreatedBy(existingUser.getCreatedBy());
        userObj.setCreatedOn(existingUser.getCreatedOn());
        userObj.setPassword(existingUser.getPassword());
        userObj.setClientId(existingUser.getClientId());
        userObj.setUserType(existingUser.getUserType());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        userObj.setUpdatedBy(getUserId().getEmail());
        userObj.setUpdatedOn(date);
        return userObj;
    }


    private Map fileSave(MultipartHttpServletRequest request, String path) {
        Map<String, String> msg = new HashMap<>();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        MultipartFile multipartFile = request.getFile("photo");
        String fileName = multipartFile.getOriginalFilename();
        Random rand = new Random();
        int n = rand.nextInt(1000) + 1;
        fileName = n + "_" + fileName; // create new name for logo file
        try {
            String filePath = path + fileName;
            String realPathFetch = request.getRealPath("/");
            inputStream = multipartFile.getInputStream();
            File newFile = new File(realPathFetch + filePath);
            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[2048];
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


    private String deletePhoto(String realPathFetch, String fileName) {
        String msg = "";
        try {
            File file = new File(realPathFetch + fileName);
            file.setWritable(true);
            if (file.delete()) msg = "";
            else msg = environment.getProperty("user.file.delete.success.msg");
        } catch (Exception e) {
            msg = e.getMessage();
        }
        return msg;
    }



    private User getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User)auth.getPrincipal();
    }


}
