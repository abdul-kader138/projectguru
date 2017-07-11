package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.ApprovalStatusDao;
import com.dreamchain.skeleton.dao.ChangeRequestDao;
import com.dreamchain.skeleton.dao.UserDao;
import com.dreamchain.skeleton.model.ApprovalStatus;
import com.dreamchain.skeleton.model.ChangeRequest;
import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.ApprovalStatusService;
import com.dreamchain.skeleton.utility.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@PropertySource("classpath:config.properties")
public class ApprovalStatusServiceImpl implements ApprovalStatusService {
    @Autowired
    ApprovalStatusDao approvalStatusDao;
    @Autowired
    ChangeRequestDao changeRequestDao;
    @Autowired
    UserDao userDao;
    @Autowired
    Environment environment;


    private static final String INVALID_INPUT = "Invalid input";
    private static final String INVALID_APPROVAL_DATA = "Data not exists";
    private static final String INVALID_REQUEST_DATA = "Data not exists";
    private static final String BACK_DATED_DATA = "Approval data is old.Please try again with updated data";
    private static final String EMAIL_HEADER_SAVE = "Request is waiting for approval.Request Name ##";
    private static final String EMAIL_HEADER_DELETE = "Request is Deleted.Request Name ##";
    private static final String EMAIL_BODY_DELETE = "Your asking request is deleted.Request Name ##";
    private static final String EMAIL_BODY_SAVE= "Request is waiting at your Approval Explorer for approve.Request Name ##";



    @Override
    public ApprovalStatus get(Long id) {
        return approvalStatusDao.get(id);
    }

    @Transactional
    public Map<String, Object> update(Map<String, Object> approvalObj, HttpServletRequest request) throws ParseException {
        Map<String, Object> obj = new HashMap<>();
        String validationMsg = "";
        Date deliveryDate = null;
        Integer days;
        List<ApprovalStatus> ObjList = new ArrayList<>();
        Integer approvedBy = (Integer) approvalObj.get("id");
        Integer versionId = (Integer) approvalObj.get("version");
        String date = (String) approvalObj.get("date");
        String day = (String) approvalObj.get("day");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (!"".equals(date) && date != null) deliveryDate = sdf.parse(date);
        long version = versionId.longValue();
        long approvedById = approvedBy.longValue();
        ApprovalStatus approvalStatus = approvalStatusDao.get(approvedById);
        days = approvalStatus.getRequiredDay();
        if (!"".equals(day) && day != null) days = Integer.parseInt(day);
        if (approvalStatus.getUserType().equals(environment.getProperty("approval.user.approvedBy")))
            approvalStatus.setDeliverDate(setDeliveryDate(days));
        if (!"".equals(day) && day != null) approvalStatus.setRequiredDay(days);
        if (approvalStatus.getId() == 0l && "".equals(validationMsg)) validationMsg = INVALID_INPUT;
        if (approvalStatus.getVersion() != version && "".equals(validationMsg)) validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) ObjList = saveApprovalObj(approvalStatus);
        if ("".equals(validationMsg)) UpdateApprovalObj(ObjList);
        obj.put("approval_details", ObjList.get(0));
        obj.put("validationError", validationMsg);
        if ("".equals(validationMsg)) ObjList = findByUserId(request);
        obj.put("notificationCount", ObjList.size());
        return obj;
    }


    @Transactional(readOnly=true)
    public List<ApprovalStatus> findByUserId(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        User existingUser = userDao.findByUserName(user.getEmail());
        List<ApprovalStatus> approvalStatusList = approvalStatusDao.findByUserId(existingUser.getId());
        httpSession.setAttribute("notificationCount", approvalStatusList.size());
        return approvalStatusList;
    }


    @Transactional
    public String delete(long requestId, long id, HttpServletRequest request) {
        String validationMsg = "";
        if (requestId == 0l) validationMsg = INVALID_INPUT;
        ApprovalStatus approvalStatus = approvalStatusDao.get(id);
        ChangeRequest changeRequest = changeRequestDao.get(requestId);
        if ("".equals(approvalStatus.getStatus()) && "".equals(validationMsg)) validationMsg = INVALID_APPROVAL_DATA;
        if ("".equals(changeRequest.getStatus()) && "".equals(validationMsg)) validationMsg = INVALID_REQUEST_DATA;
        if ("".equals(validationMsg)) {
            String filePath = changeRequest.getDocPath();
            deleteDoc(filePath, request);
            changeRequestDao.delete(changeRequest);
            approvalStatusDao.delete(requestId);
            User requestByUser= userDao.get(changeRequest.getRequestById());
            sendEmail(requestByUser.getEmail(), EMAIL_HEADER_DELETE+ changeRequest.getName(), EMAIL_BODY_DELETE + changeRequest.getName());
        }
        return validationMsg;
    }


    // status done for current approvedId and waiting for new approvedId
    private List<ApprovalStatus> saveApprovalObj(ApprovalStatus approvalStatus) throws ParseException {
        List<ApprovalStatus> approvalStatusList = new ArrayList<>();
        if (environment.getProperty("approval.user.checkedBy").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.waiting").equals(approvalStatus.getStatus())) {
            approvalStatusList.add(setApprovalStatusValue(approvalStatus, environment.getProperty("approval.status.done")));
            ApprovalStatus itCoordinatorStatus = approvalStatusDao.findByRequestIdAndUserType(approvalStatus.getRequestId(), environment.getProperty("approval.user.itCoordinator"));
            approvalStatusList.add(setApprovalStatusValue(itCoordinatorStatus, environment.getProperty("approval.status.waiting")));
        }
        if (environment.getProperty("approval.user.itCoordinator").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.waiting").equals(approvalStatus.getStatus())) {
            approvalStatusList.add(setApprovalStatusValue(approvalStatus, environment.getProperty("approval.status.done")));
            ApprovalStatus approvedByStatus = approvalStatusDao.findByRequestIdAndUserType(approvalStatus.getRequestId(), environment.getProperty("approval.user.approvedBy"));
            approvalStatusList.add(setApprovalStatusValue(approvedByStatus, environment.getProperty("approval.status.waiting")));
            approvedByStatus.setRequiredDay(approvalStatus.getRequiredDay()); // set required Days for developer
        }
        if (environment.getProperty("approval.user.approvedBy").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.waiting").equals(approvalStatus.getStatus())) {
            approvalStatusList.add(setApprovalStatusValue(approvalStatus, environment.getProperty("approval.status.done")));
            ApprovalStatus acknowledgementITStatus = approvalStatusDao.findByRequestIdAndUserType(approvalStatus.getRequestId(), environment.getProperty("approval.user.acknowledgementIT"));
            approvalStatusList.add(setApprovalStatusValue(acknowledgementITStatus, environment.getProperty("approval.status.waiting")));
            acknowledgementITStatus.setDeliverDate(approvalStatus.getDeliverDate());
            acknowledgementITStatus.setRequiredDay(approvalStatus.getRequiredDay());
        }
        if (environment.getProperty("approval.user.acknowledgementIT").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.waiting").equals(approvalStatus.getStatus())) {
            approvalStatusList.add(setApprovalStatusValue(approvalStatus, environment.getProperty("approval.status.done")));
            ApprovalStatus acknowledgeCheckedByStatus = approvalStatusDao.findByRequestIdAndUserType(approvalStatus.getRequestId(), environment.getProperty("approval.user.acknowledgeCheckedBy"));
            approvalStatusList.add(setApprovalStatusValue(acknowledgeCheckedByStatus, environment.getProperty("approval.status.waiting")));
            acknowledgeCheckedByStatus.setDeliverDate(approvalStatus.getDeliverDate());
            acknowledgeCheckedByStatus.setRequiredDay(approvalStatus.getRequiredDay());
        }
        if (environment.getProperty("approval.user.acknowledgeCheckedBy").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.waiting").equals(approvalStatus.getStatus())) {
            approvalStatusList.add(setApprovalStatusValue(approvalStatus, environment.getProperty("approval.status.done")));
            ApprovalStatus acknowledgementStatus = approvalStatusDao.findByRequestIdAndUserType(approvalStatus.getRequestId(), environment.getProperty("approval.user.acknowledgement"));
            approvalStatusList.add(setApprovalStatusValue(acknowledgementStatus, environment.getProperty("approval.status.waiting")));
            acknowledgementStatus.setDeliverDate(approvalStatus.getDeliverDate());
            acknowledgementStatus.setRequiredDay(approvalStatus.getRequiredDay());
        }
        if (environment.getProperty("approval.user.acknowledgement").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.waiting").equals(approvalStatus.getStatus())) {
            approvalStatusList.add(setApprovalStatusValue(approvalStatus, environment.getProperty("approval.status.done")));
            ChangeRequest changeRequest = changeRequestDao.get(approvalStatus.getRequestId());
            approvalStatus.setDeliverDate(changeRequest.getDeliverDate());
            approvalStatus.setRequiredDay(changeRequest.getRequiredDay());
        }
        return approvalStatusList;
    }

    private ApprovalStatus setApprovalStatusValue(ApprovalStatus approvalStatus, String status) throws ParseException {
        ApprovalStatus newObj = approvalStatus;
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        approvalStatus.setStatus(status);
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        newObj.setUpdatedBy(getUserId().getEmail());
        newObj.setUpdatedOn(date);
        return newObj;
    }

    private User getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }


    //save Change Request and Approve status
    private void UpdateApprovalObj(List<ApprovalStatus> statusList) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        for (ApprovalStatus approvalStatus : statusList) {
            if (environment.getProperty("approval.user.acknowledgement").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.done").equals(approvalStatus.getStatus())) {
                ChangeRequest changeRequest = changeRequestDao.get(approvalStatus.getRequestId());
                changeRequest.setWipStatus(environment.getProperty("approval.wip.status.done"));
                changeRequest.setStatus(environment.getProperty("approval.status.done"));
                UpdateApprovalStatus(changeRequest,approvalStatus,"",date);
            } else if (environment.getProperty("approval.user.checkedBy").equals(approvalStatus.getUserType())
                    && environment.getProperty("approval.status.done").equals(approvalStatus.getStatus())) {
                ChangeRequest changeRequest = changeRequestDao.get(approvalStatus.getRequestId());
                changeRequest.setCheckedByStatus(environment.getProperty("approval.user.checkedBy"));
                changeRequest.setWipStatus(environment.getProperty("approval.status.approve.type.itCoordinatorBy"));
                User itCoordinatorUser= userDao.get(changeRequest.getItCoordinatorId());
                UpdateApprovalStatus(changeRequest,approvalStatus,itCoordinatorUser.getEmail(),date);
            } else if (environment.getProperty("approval.user.itCoordinator").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.done").equals(approvalStatus.getStatus())) {
                ChangeRequest changeRequest = changeRequestDao.get(approvalStatus.getRequestId());
                changeRequest.setWipStatus(environment.getProperty("approval.status.approve.type.approvedBy"));
                changeRequest.setRequiredDay(approvalStatus.getRequiredDay());
                User approvedByUser= userDao.get(changeRequest.getApprovedById());
                UpdateApprovalStatus(changeRequest,approvalStatus,approvedByUser.getEmail(),date);
            } else if (environment.getProperty("approval.user.approvedBy").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.done").equals(approvalStatus.getStatus())) {
                ChangeRequest changeRequest = changeRequestDao.get(approvalStatus.getRequestId());
                changeRequest.setWipStatus(environment.getProperty("approval.status.approve.type.acknowledgeBy.itCoordinator"));
                changeRequest.setDeliverDate(approvalStatus.getDeliverDate());
                User acknowledgedItCoordinatorUser= userDao.get(changeRequest.getAcknowledgedItCoordinatorId());
                UpdateApprovalStatus(changeRequest,approvalStatus,acknowledgedItCoordinatorUser.getEmail(),date);
            } else if (environment.getProperty("approval.user.acknowledgementIT").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.done").equals(approvalStatus.getStatus())) {
                ChangeRequest changeRequest = changeRequestDao.get(approvalStatus.getRequestId());
                changeRequest.setWipStatus(environment.getProperty("approval.status.approve.type.acknowledgeBy.checkedBy"));
                changeRequest.setDeployedOn(date);
                approvalStatus.setDeliverDate(changeRequest.getDeliverDate());
                approvalStatus.setRequiredDay(changeRequest.getRequiredDay());
                User acknowledgeCheckedUser= userDao.get(changeRequest.getAcknowledgeCheckedId());
                UpdateApprovalStatus(changeRequest,approvalStatus,acknowledgeCheckedUser.getEmail(),date);
            } else if (environment.getProperty("approval.user.acknowledgeCheckedBy").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.done").equals(approvalStatus.getStatus())) {
                ChangeRequest changeRequest = changeRequestDao.get(approvalStatus.getRequestId());
                changeRequest.setWipStatus(environment.getProperty("approval.status.approve.type.acknowledgeBy.requestBy"));
                approvalStatus.setDeliverDate(changeRequest.getDeliverDate());
                approvalStatus.setRequiredDay(changeRequest.getRequiredDay());
                User acknowledgementUser= userDao.get(changeRequest.getAcknowledgementId());
                UpdateApprovalStatus(changeRequest,approvalStatus,acknowledgementUser.getEmail(),date);
            }
        }
    }

    private String deleteDoc(String realPathFetch, HttpServletRequest request) {
        String msg = "";
        try {
            String realPath = request.getRealPath("/");
            File newFile = new File(realPath + realPathFetch);
            newFile.setWritable(true);
            if (newFile.delete()) msg = "";
            else msg = environment.getProperty("request.file.delete.success.msg");
        } catch (Exception e) {
            msg = e.getMessage();
        }
        return msg;
    }

    private Date setDeliveryDate(int requiredDays) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, requiredDays);
        date = c.getTime();
        return date;
    }


    private void sendEmail(String toEmail, String header, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "mail.paragon.com.bd");
        props.put("mail.smtp.socketFactory.port", "25");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "25");
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(environment.getProperty("approval.send.email.from.id"), environment.getProperty("approval.send.email.from.password"));
                    }
                });
        EmailUtil.sendEmail(session, toEmail, header, body);

    }

    private void UpdateApprovalStatus(ChangeRequest changeRequest,ApprovalStatus approvalStatus,String mail,Date date){
        changeRequest.setUpdatedOn(date);
        changeRequest.setUpdatedBy(getUserId().getEmail());
        changeRequestDao.save(changeRequest);
        approvalStatusDao.save(approvalStatus);
        if(!"".equals(mail)) sendEmail(mail, EMAIL_HEADER_SAVE + changeRequest.getName(), EMAIL_BODY_SAVE + changeRequest.getName());
    }

}
