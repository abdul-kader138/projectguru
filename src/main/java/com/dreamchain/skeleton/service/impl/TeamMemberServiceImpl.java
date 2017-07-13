package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.*;
import com.dreamchain.skeleton.model.*;
import com.dreamchain.skeleton.service.TeamMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

@Service
@PropertySource("classpath:config.properties")

public class TeamMemberServiceImpl implements TeamMemberService {
    @Autowired
    CompanyDao companyDao;
    @Autowired
    TeamMemberDao teamMemberDao;
    @Autowired
    TeamAllocationDao teamAllocationDao;
    @Autowired
    RoleRightDao roleRightDao;
    @Autowired
    RolesDao rolesDao;
    @Autowired
    ApprovalStatusDao approvalStatusDao;
    @Autowired
    Environment environment;

    private static final String EMAIL_EXISTS = "This email address already used.Please try again with new one!!!";
    private static final String INVALID_INPUT = "Invalid input";
    private static final String INVALID_USER = "Team Member not exists";
    private static final String BACK_DATED_DATA = "This data is old.Please try again with updated data";
    private static final String PHOTO_TEAM_PATH = "/resources/images/team_member_photo/";
    private static final String USER_ASSOCIATED_APPROVAL_UPDATE = "Team member can't update due to association with request";
    private static final String USER_ASSOCIATED_APPROVAL_DELETE = "Team member can't delete due to association with request";
    private static final String USER_ASSOCIATED_TEAM_ALLOCATION = "Team member can't delete due to association with allocation";

    @Transactional(readOnly = true)
    public User get(Long id) {
        return teamMemberDao.get(id);
    }

    @Transactional
    public void delete(User user) {
        teamMemberDao.delete(user);
    }


    @Transactional(readOnly = true)
    public List<User> findAll(String userName) {
        return teamMemberDao.findAll(userName);
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
        if ("".equals(validationMsg)) existingUser = teamMemberDao.findByUserName(user.getEmail());
        if (existingUser != null && "".equals(validationMsg)) validationMsg = EMAIL_EXISTS;
        msg = fileSave(request, PHOTO_TEAM_PATH);
        if ("".equals(msg.get("validationMsg"))) user.setImagePath((String) msg.get("path"));
        if ("".equals(validationMsg)) {
            long companyId = teamMemberDao.save(user);
            newUser = teamMemberDao.get(companyId);
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
        if ("".equals(validationMsg)) existingUser = teamMemberDao.get(user.getId());
        if (existingUser.getName() == null && "".equals(validationMsg)) validationMsg = INVALID_USER;
        if ("".equals(validationMsg)) approvalStatuses= approvalStatusDao.findByApprovedById(existingUser.getId());
        if(approvalStatuses.size() !=0 && "".equals(validationMsg) && user.getCompanyId() !=existingUser.getCompanyId()) validationMsg=USER_ASSOCIATED_APPROVAL_UPDATE;
        if (user.getVersion() != existingUser.getVersion() && "".equals(validationMsg)) validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) newUser = teamMemberDao.findByNewName(existingUser.getEmail(), user.getEmail());
        if (newUser.getName() != null && "".equals(validationMsg)) validationMsg = EMAIL_EXISTS;
        String fileName = request.getFile("photo").getOriginalFilename();
        if (!("".equals(fileName))) {
            validationMsg = deletePhoto(request.getRealPath(
                    "/"), existingUser.getImagePath());
            if ("".equals(validationMsg)) msg = fileSave(request, PHOTO_TEAM_PATH);
            if ("".equals(validationMsg)) existingUser.setImagePath((String) msg.get("path"));
        }
        if ("".equals(validationMsg)) {
            user.setClientId(getUserId().getClientId());
            user.setUserType(getUserId().getUserType());
            newUser = setUpdateUserValue(user, existingUser);
            teamMemberDao.update(newUser);
        }
        obj.put("user", newUser);
        obj.put("validationError", validationMsg);
        return obj;
    }


    @Transactional
    public String delete(Long userId, HttpServletRequest request) {
        String validationMsg = "";
        String fileName = "";
        List<TeamAllocation> objList=new ArrayList<>();
        List<ApprovalStatus> approvalStatuses=new ArrayList<>();
        if (userId == 0l) validationMsg = INVALID_INPUT;
        User user = teamMemberDao.get(userId);
        if (user == null && "".equals(validationMsg)) validationMsg = INVALID_USER;
        if ("".equals(validationMsg)) objList=teamAllocationDao.AllAllocationByCheckedBy(user.getId());
        if (objList.size() > 0 && "".equals(validationMsg)) validationMsg = USER_ASSOCIATED_TEAM_ALLOCATION;
        if ("".equals(validationMsg)) objList=teamAllocationDao.AllAllocationByRequestedBy(user.getId());
        if (objList.size() > 0 && "".equals(validationMsg)) validationMsg = USER_ASSOCIATED_TEAM_ALLOCATION;
        if ("".equals(validationMsg)) approvalStatuses= approvalStatusDao.findByApprovedById(user.getId());
        if(approvalStatuses.size() !=0 && "".equals(validationMsg)) validationMsg=USER_ASSOCIATED_APPROVAL_DELETE;
        if (! "".equals(user.getClientId())) fileName = user.getImagePath();
        if ("".equals(validationMsg)) validationMsg = deletePhoto(request.getRealPath("/"), fileName);
        if ("".equals(validationMsg)) {
            teamMemberDao.delete(user);
        }
        return validationMsg;
    }

    @Transactional(readOnly=true)
    public User findByUserName(String username) {
        return teamMemberDao.findByUserName(username);
    }

    @Transactional(readOnly=true)
    public List<User> findAll() {
        return teamMemberDao.findAll();
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
        String roleName = environment.getProperty("team.default.role");
        Roles roles = rolesDao.findByRolesName(roleName);
        RoleRight roleRight = roleRightDao.findByRolesName(roles.getId());
        Company company = companyDao.get(Long.parseLong(request.getParameter("companyId")));
        User user = setEditorInfo(operationName, request);
        user.setId(Long.parseLong(request.getParameter("id")));
        user.setVersion(Long.parseLong(request.getParameter("version")));
        user.setName(request.getParameter("name").trim());
        user.setPhone(request.getParameter("phone").trim());
        user.setDesignation(request.getParameter("designation").trim());
        user.setUserType(getUserId().getUserType());
        user.setClientId(getUserId().getClientId());
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
        userObj.setCompany(objFromUI.getCompany());
        userObj.setCompanyId(objFromUI.getCompanyId());
        userObj.setImagePath(existingUser.getImagePath());
        userObj.setUserType(objFromUI.getUserType());
        userObj.setClientId(getUserId().getClientId());

        userObj.setRole(existingUser.getRole());
        userObj.setRoleRight(existingUser.getRoleRight());
        userObj.setRoleRightsId(existingUser.getRoleRightsId());
        userObj.setEmail(existingUser.getEmail());
        userObj.setCreatedBy(existingUser.getCreatedBy());
        userObj.setCreatedOn(existingUser.getCreatedOn());
        userObj.setPassword(existingUser.getPassword());

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


    private User getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }


}
