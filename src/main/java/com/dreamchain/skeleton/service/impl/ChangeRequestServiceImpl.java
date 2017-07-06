package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.*;
import com.dreamchain.skeleton.model.*;
import com.dreamchain.skeleton.service.ChangeRequestService;
import com.dreamchain.skeleton.utility.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
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
public class ChangeRequestServiceImpl implements ChangeRequestService {

    @Autowired
    ChangeRequestDao changeRequestDao;
    @Autowired
    UserAllocationDao userAllocationDao;
    @Autowired
    TeamAllocationDao teamAllocationDao;
    @Autowired
    UserDao userDao;
    @Autowired
    CategoryDao categoryDao;
    @Autowired
    ApprovalStatusDao approvalStatusDao;
    @Autowired
    Environment environment;

    private static String CHANGED_REQUEST_EXISTS = "This request name is already used.Please try again with new one!!!";
    private static String TEAM_ALLOCATION = "Team member not allocated please contact with admin....";
    private static String USER_ALLOCATION = "User not allocated please contact with admin....";
    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_COMPANY = "Company not exists";
    private static String BACK_DATED_DATA = "Company data is old.Please try again with updated data";
    private static String ASSOCIATED_COMPANY = "Company is tagged with Department.First remove tagging and try again";
    private static String DOC_PATH = "/resources/images/doc/";
    private static String EMAIL_HEADER_SAVE= "New Request is waiting for approval.Request Name ##";
    private static String EMAIL_BODY_SAVE= "New request is generate and waiting for you approval.Request Name ##";

    @Override

    @Transactional(readOnly = true)
    public ChangeRequest get(Long id) {
        return changeRequestDao.get(id);
    }

    @Transactional
    public Map<String, Object> save(MultipartHttpServletRequest request) throws Exception {
        Map<String, Object> obj = new HashMap<>();
        Map<String, Object> msg = new HashMap<>();
        Map<String, User> userLst = new HashMap<>();
        Map<String, Object> validationObj = new HashMap<>();
        ChangeRequest changeRequest = new ChangeRequest();
        String validationMsg = "";
        ChangeRequest newChangeRequest = new ChangeRequest();
        ChangeRequest existingChangeRequest = new ChangeRequest();
        validationObj = createObjForSave(request, request.getFile("doc").getOriginalFilename());
        validationMsg = (String) validationObj.get("validationMsg");
        if ("".equals(validationMsg)) changeRequest = (ChangeRequest) validationObj.get("changeRequest");
        if ("".equals(validationMsg)) validationMsg = checkInput(changeRequest);
        if ("".equals(validationMsg))
            existingChangeRequest = changeRequestDao.findByName(changeRequest.getName(), changeRequest.getCategory().getId());
        if (existingChangeRequest.getCheckedBy() != null && "".equals(validationMsg))
            validationMsg = CHANGED_REQUEST_EXISTS;
        if ("".equals(validationMsg)) msg = fileSave(request);
        if (msg.get("validationMsg") == "") changeRequest.setDocPath((String) msg.get("path"));
        if ("".equals(validationMsg)) {
            long companyId = changeRequestDao.save(changeRequest);
            newChangeRequest = changeRequestDao.get(companyId);
            userLst = createApprovalList(newChangeRequest);
            saveApprovalObj(userLst, newChangeRequest);
            sendEmail(newChangeRequest.getCheckedBy().getEmail(),EMAIL_HEADER_SAVE,EMAIL_BODY_SAVE+changeRequest.getName()); //mail sent to notify user for approving
        }
        obj.put("changeRequest", newChangeRequest);
        obj.put("validationError", validationMsg);
        return obj;
    }

    @Override
    public Map<String, Object> update(MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
        return null;
    }

    @Override
    public String delete(Long companyId, HttpServletRequest request) {
        return null;
    }

    @Override
    public List<ChangeRequest> findAll() {
        User user=userDao.findByUserName(getUserId().getEmail());
        Set<ApprovalStatus> approvalStatuses =approvalStatusDao.findByApprovedId(user.getId());
        Set<Long> requestId =requestIdList(approvalStatuses);
        if(requestId.size() == 0) requestId.add(0l);
        return changeRequestDao.findAll(requestId);
    }

    @Override
    public List<ChangeRequest> findAllForDeveloper() {
        return changeRequestDao.findAllForDeveloper();
    }

    // create user object for saving

    private Map createObjForSave(MultipartHttpServletRequest request, String fileName) throws Exception {
        String validationMsg = "";
        Map<String, Object> objList = new HashMap<>();
        Category category = categoryDao.get(Long.parseLong(request.getParameter("categoryId")));
        String[] descriptionObj=request.getParameterValues("description[]");
        List<String> descriptionList= setDescriptionList(descriptionObj);
        Set<String> descriptions=new HashSet<>(descriptionList);
        TeamAllocation teamAllocation = teamAllocationDao.findByProductAndCategory(category.getCompanyId(), category.getProductId(), category.getId());
        UserAllocation userAllocation = userAllocationDao.findByProductAndCategory(category.getCompanyId(), category.getProductId(), category.getId());
        ChangeRequest changeRequest = setChangeRequestValue(category, teamAllocation, userAllocation);
        if (category.getCompany() == null) validationMsg = INVALID_INPUT;
        if (teamAllocation.getCheckedBy() == null && "".equals(validationMsg)) validationMsg = TEAM_ALLOCATION;
        if (userAllocation.getApprovedBy() == null && "".equals(validationMsg)) validationMsg = USER_ALLOCATION;
        if ("".equals(validationMsg)) {
            changeRequest.setName(request.getParameter("name").trim());
            changeRequest.setDescription(descriptions);
            changeRequest.setDocPath(DOC_PATH + fileName.trim());
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            changeRequest.setStatus(environment.getProperty("request.status.open"));
            Date date = dateFormat.parse(dateFormat.format(new Date()));
            changeRequest.setCreatedBy(getUserId().getEmail());
            changeRequest.setCreatedOn(date);
        }

        objList.put("validationMsg", validationMsg);
        objList.put("changeRequest", changeRequest);
        return objList;

    }


    private Map fileSave(MultipartHttpServletRequest request) {
        Map<String, String> msg = new HashMap<>();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        MultipartFile multipartFile = request.getFile("doc");
        String fileName = multipartFile.getOriginalFilename();
        Random rand = new Random();
        int n = rand.nextInt(1000) + 1;
        fileName = n + "_" + fileName; // create new name for doc file
        try {
            String filePath = DOC_PATH + fileName;
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

    // check for invalid data
    private String checkInput(ChangeRequest changeRequest) {
        String msg = "";

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<ChangeRequest>> constraintViolations = validator.validate(changeRequest);
        if (constraintViolations.size() > 0 && "".equals(msg)) msg = INVALID_INPUT;

        return msg;
    }


    private User getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return user;
    }


    private ChangeRequest setChangeRequestValue(Category category, TeamAllocation teamAllocation, UserAllocation userAllocation) {
        ChangeRequest changeRequest = new ChangeRequest();
        changeRequest.setCategory(category);
        changeRequest.setRequestBy(teamAllocation.getRequestedBy());
        changeRequest.setCheckedBy(teamAllocation.getCheckedBy());
        changeRequest.setAcknowledgeChecked(teamAllocation.getCheckedBy());
        changeRequest.setAcknowledgement(teamAllocation.getRequestedBy());
        changeRequest.setItCoordinator(userAllocation.getItCoordinator());
        changeRequest.setApprovedBy(userAllocation.getApprovedBy());
        changeRequest.setDepartmentId(category.getDepartment().getId());
        changeRequest.setAcknowledgedItCoordinator(userAllocation.getItCoordinator());
        changeRequest.setCheckedByStatus(environment.getProperty("approval.wip.checkedBy.status.none"));
        changeRequest.setWipStatus(environment.getProperty("approval.status.approve.type.checkedBy"));
        changeRequest.setTeamAllocationId(teamAllocation.getId());
        changeRequest.setUserAllocationId(userAllocation.getId());
        return changeRequest;
    }

    private ApprovalStatus setApprovalStatusValue(ChangeRequest changeRequest, User user, String status, String userType, String approveType) throws ParseException {
        ApprovalStatus approvalStatus = new ApprovalStatus();
        approvalStatus.setCategory(changeRequest.getCategory());
        approvalStatus.setApprovedBy(user);
        approvalStatus.setStatus(status);
        approvalStatus.setRequestName(changeRequest.getName());
        approvalStatus.setUserType(userType);
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        approvalStatus.setCreatedBy(getUserId().getEmail());
        approvalStatus.setCreatedOn(date);
        approvalStatus.setApproveType(approveType);
        approvalStatus.setRequestId(changeRequest.getId());
        approvalStatus.setDocPath(changeRequest.getDocPath());
        return approvalStatus;
    }


    private Map<String, User> createApprovalList(ChangeRequest changeRequest) {
        Map<String, User> userMap = new HashMap<>();
        if (changeRequest.getRequestBy() != null)
            userMap.put(environment.getProperty("approval.user.requestBy"), changeRequest.getRequestBy());
        if (changeRequest.getCheckedBy() != null)
            userMap.put(environment.getProperty("approval.user.checkedBy"), changeRequest.getCheckedBy());
        if (changeRequest.getItCoordinator() != null)
            userMap.put(environment.getProperty("approval.user.itCoordinator"), changeRequest.getItCoordinator());
        if (changeRequest.getApprovedBy() != null)
            userMap.put(environment.getProperty("approval.user.approvedBy"), changeRequest.getApprovedBy());
        if (changeRequest.getAcknowledgeChecked() != null)
            userMap.put(environment.getProperty("approval.user.acknowledgeCheckedBy"), changeRequest.getAcknowledgeChecked());
        if (changeRequest.getAcknowledgement() != null)
            userMap.put(environment.getProperty("approval.user.acknowledgement"), changeRequest.getAcknowledgement());
        if (changeRequest.getAcknowledgedItCoordinator() != null)
            userMap.put(environment.getProperty("approval.user.acknowledgementIT"), changeRequest.getAcknowledgedItCoordinator());
        return userMap;
    }


    private void saveApprovalObj(Map<String, User> userLst, ChangeRequest changeRequest) throws ParseException {
        ApprovalStatus approvalStatus = null;
        User user = null;
        for (Map.Entry<String, User> entry : userLst.entrySet()) {
            if (environment.getProperty("approval.user.requestBy").equals(entry.getKey())) {
                user = entry.getValue();
                approvalStatus = setApprovalStatusValue(changeRequest, user, environment.getProperty("approval.status.done"),
                        environment.getProperty("approval.user.requestBy"), environment.getProperty("approval.status.approve.type.requestBy"));
                approvalStatusDao.save(approvalStatus);
            }
            if (environment.getProperty("approval.user.checkedBy").equals(entry.getKey())) {
                user = entry.getValue();
                approvalStatus = setApprovalStatusValue(changeRequest, user, environment.getProperty("approval.status.waiting"),
                        environment.getProperty("approval.user.checkedBy"), environment.getProperty("approval.status.approve.type.checkedBy"));
                approvalStatusDao.save(approvalStatus);
            }
                if (environment.getProperty("approval.user.itCoordinator").equals(entry.getKey())) {
                    user = entry.getValue();
                    approvalStatus = setApprovalStatusValue(changeRequest, user, environment.getProperty("approval.status.none"),
                            environment.getProperty("approval.user.itCoordinator"), environment.getProperty("approval.status.approve.type.itCoordinatorBy"));
                    approvalStatusDao.save(approvalStatus);
                }

                if (environment.getProperty("approval.user.approvedBy").equals(entry.getKey())) {
                    user = entry.getValue();
                    approvalStatus = setApprovalStatusValue(changeRequest, user, environment.getProperty("approval.status.none"),
                            environment.getProperty("approval.user.approvedBy"), environment.getProperty("approval.status.approve.type.approvedBy"));
                    approvalStatusDao.save(approvalStatus);
                }

                if (environment.getProperty("approval.user.acknowledgeCheckedBy").equals(entry.getKey())) {
                    user = entry.getValue();
                    approvalStatus = setApprovalStatusValue(changeRequest, user, environment.getProperty("approval.status.none"),
                            environment.getProperty("approval.user.acknowledgeCheckedBy"), environment.getProperty("approval.status.approve.type.acknowledgeBy.checkedBy"));
                    approvalStatusDao.save(approvalStatus);
                }

                if (environment.getProperty("approval.user.acknowledgement").equals(entry.getKey())) {
                    user = entry.getValue();
                    approvalStatus = setApprovalStatusValue(changeRequest, user, environment.getProperty("approval.status.none"),
                            environment.getProperty("approval.user.acknowledgement"), environment.getProperty("approval.status.approve.type.acknowledgeBy.requestBy"));
                    approvalStatusDao.save(approvalStatus);
                }
                if (environment.getProperty("approval.user.acknowledgementIT").equals(entry.getKey())) {
                    user = entry.getValue();
                    approvalStatus = setApprovalStatusValue(changeRequest, user, environment.getProperty("approval.status.none"),
                            environment.getProperty("approval.user.acknowledgementIT"), environment.getProperty("approval.status.approve.type.acknowledgeBy.itCoordinator"));
                    approvalStatusDao.save(approvalStatus);
                }
            }
        }

    private Set<Long> requestIdList (Set<ApprovalStatus> approvalStatuses){
        Set<Long> requestIdList =new HashSet<>();
        for( ApprovalStatus approvalStatus:approvalStatuses){
            requestIdList.add(approvalStatus.getRequestId());
        }
        return requestIdList;
    }

    private void sendEmail(String toEmail, String header, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.paragon.com.bd");
        props.put("mail.smtp.socketFactory.port", "25");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "25");
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(environment.getProperty("approval.send.email.from.id"), environment.getProperty("approval.send.email.from.password"));
            }
        };

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(environment.getProperty("approval.send.email.from.id"), environment.getProperty("approval.send.email.from.password"));
                    }
                });

        EmailUtil.sendEmail(session, toEmail, header, body);

    }

    private List<String> setDescriptionList(String[] list){
        List<String> obj=new ArrayList<>();
        for(String desObj:list){
            if(! "".equals(desObj)) obj.add(desObj);
        }
        return obj;
    }



}

