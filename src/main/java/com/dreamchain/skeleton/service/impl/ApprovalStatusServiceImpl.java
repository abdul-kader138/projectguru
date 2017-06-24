package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.ApprovalStatusDao;
import com.dreamchain.skeleton.dao.ChangeRequestDao;
import com.dreamchain.skeleton.dao.UserDao;
import com.dreamchain.skeleton.model.ApprovalStatus;
import com.dreamchain.skeleton.model.ChangeRequest;
import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.ApprovalStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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


    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_APPROVAL_DATA = "Data not exists";
    private static String INVALID_REQUEST_DATA = "Data not exists";
    private static String BACK_DATED_DATA = "Approval data is old.Please try again with updated data";

    @Override
    public ApprovalStatus get(Long id) {
        return approvalStatusDao.get(id);
    }

    @Transactional
    public Map<String, Object> update(Map<String, Object> approvalObj, HttpServletRequest request) throws ParseException {
        Map<String, Object> obj = new HashMap<>();
        String validationMsg = "";
        Date deliveryDate=null;
        List<ApprovalStatus> ObjList = new ArrayList<>();
        Integer approvedBy = (Integer) approvalObj.get("id");
        Integer versionId = (Integer) approvalObj.get("version");
        String date=(String) approvalObj.get("date");
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        if(!"".equals(date) && date !=null) deliveryDate = sdf.parse(date);
        long version = versionId.longValue();
        long approvedById = approvedBy.longValue();
        ApprovalStatus approvalStatus = approvalStatusDao.get(approvedById);
        approvalStatus.setDeliverDate(deliveryDate);
        if (approvalStatus.getId() == 0l && validationMsg == "") validationMsg = INVALID_INPUT;
        if (approvalStatus.getVersion() != version && validationMsg == "") validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) ObjList = saveApprovalObj(approvalStatus);
        if ("".equals(validationMsg)) UpdateApprovalObj(ObjList);
        obj.put("approval_details", ObjList.get(0));
        obj.put("validationError", validationMsg);
        if ("".equals(validationMsg)) ObjList = findByUserId(request);
        obj.put("notificationCount", ObjList.size());
        return obj;
    }


    @Override
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
    public String delete(long requestId,long id) {
        String validationMsg = "";
        if (requestId == 0l) validationMsg = INVALID_INPUT;
        ApprovalStatus  approvalStatus = approvalStatusDao.get(id);
        ChangeRequest changeRequest=changeRequestDao.get(requestId);
        if ("".equals(approvalStatus.getStatus())  && "".equals(validationMsg)) validationMsg = INVALID_APPROVAL_DATA;
        if ("".equals(changeRequest.getStatus())  && "".equals(validationMsg)) validationMsg = INVALID_REQUEST_DATA;
        //@todo
//        List<Object> obj=departmentDao.countOfDepartment(departmentId);
//        if (obj.size() > 0 && validationMsg == "") validationMsg = ASSOCIATED_DEPARTMENT;
        if ("".equals(validationMsg)) {
            changeRequestDao.delete(changeRequest);
            approvalStatusDao.delete(requestId);
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
            approvedByStatus.setDeliverDate(approvalStatus.getDeliverDate());
        }
        if (environment.getProperty("approval.user.approvedBy").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.waiting").equals(approvalStatus.getStatus())) {
            approvalStatusList.add(setApprovalStatusValue(approvalStatus, environment.getProperty("approval.status.done")));
            ApprovalStatus acknowledgementITStatus = approvalStatusDao.findByRequestIdAndUserType(approvalStatus.getRequestId(), environment.getProperty("approval.user.acknowledgementIT"));
            approvalStatusList.add(setApprovalStatusValue(acknowledgementITStatus, environment.getProperty("approval.status.waiting")));
            acknowledgementITStatus.setDeliverDate(approvalStatus.getDeliverDate());
        }
        if (environment.getProperty("approval.user.acknowledgementIT").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.waiting").equals(approvalStatus.getStatus())) {
            approvalStatusList.add(setApprovalStatusValue(approvalStatus, environment.getProperty("approval.status.done")));
            ApprovalStatus acknowledgeCheckedByStatus = approvalStatusDao.findByRequestIdAndUserType(approvalStatus.getRequestId(), environment.getProperty("approval.user.acknowledgeCheckedBy"));
            approvalStatusList.add(setApprovalStatusValue(acknowledgeCheckedByStatus, environment.getProperty("approval.status.waiting")));
            acknowledgeCheckedByStatus.setDeliverDate(approvalStatus.getDeliverDate());
        }
        if (environment.getProperty("approval.user.acknowledgeCheckedBy").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.waiting").equals(approvalStatus.getStatus())) {
            approvalStatusList.add(setApprovalStatusValue(approvalStatus, environment.getProperty("approval.status.done")));
            ApprovalStatus acknowledgementStatus = approvalStatusDao.findByRequestIdAndUserType(approvalStatus.getRequestId(), environment.getProperty("approval.user.acknowledgement"));
            approvalStatusList.add(setApprovalStatusValue(acknowledgementStatus, environment.getProperty("approval.status.waiting")));
            acknowledgementStatus.setDeliverDate(approvalStatus.getDeliverDate());
        }
        if (environment.getProperty("approval.user.acknowledgement").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.waiting").equals(approvalStatus.getStatus())) {
            approvalStatusList.add(setApprovalStatusValue(approvalStatus, environment.getProperty("approval.status.done")));
            ChangeRequest changeRequest=changeRequestDao.get(approvalStatus.getRequestId());
            approvalStatus.setDeliverDate(changeRequest.getDeliverDate());
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
    private void UpdateApprovalObj(List<ApprovalStatus> statusList) {
        for (ApprovalStatus approvalStatus : statusList) {
            if (environment.getProperty("approval.user.acknowledgement").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.done").equals(approvalStatus.getStatus())) {
                ChangeRequest changeRequest = changeRequestDao.get(approvalStatus.getRequestId());
                changeRequest.setWipStatus(environment.getProperty("approval.wip.status.done"));
                changeRequest.setStatus(environment.getProperty("approval.status.done"));
                approvalStatus.setDeliverDate(changeRequest.getDeliverDate());
                approvalStatusDao.save(approvalStatus);
                changeRequestDao.save(changeRequest);

            } else if (environment.getProperty("approval.user.checkedBy").equals(approvalStatus.getUserType())
                    && environment.getProperty("approval.status.done").equals(approvalStatus.getStatus())) {
                ChangeRequest changeRequest = changeRequestDao.get(approvalStatus.getRequestId());
                changeRequest.setCheckedByStatus(environment.getProperty("approval.user.checkedBy"));
                changeRequest.setWipStatus(environment.getProperty("approval.status.approve.type.itCoordinatorBy"));
                approvalStatusDao.save(approvalStatus);
                changeRequestDao.save(changeRequest);
            } else if (environment.getProperty("approval.user.itCoordinator").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.done").equals(approvalStatus.getStatus())) {
                ChangeRequest changeRequest = changeRequestDao.get(approvalStatus.getRequestId());
                changeRequest.setWipStatus(environment.getProperty("approval.status.approve.type.approvedBy"));
                changeRequest.setDeliverDate(approvalStatus.getDeliverDate());
                approvalStatusDao.save(approvalStatus);
                changeRequestDao.save(changeRequest);
            } else if (environment.getProperty("approval.user.approvedBy").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.done").equals(approvalStatus.getStatus())) {
                ChangeRequest changeRequest = changeRequestDao.get(approvalStatus.getRequestId());
                changeRequest.setWipStatus(environment.getProperty("approval.status.approve.type.acknowledgeBy.itCoordinator"));
                approvalStatus.setDeliverDate(changeRequest.getDeliverDate());
                approvalStatusDao.save(approvalStatus);
                changeRequestDao.save(changeRequest);
            } else if (environment.getProperty("approval.user.acknowledgementIT").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.done").equals(approvalStatus.getStatus())) {
                ChangeRequest changeRequest = changeRequestDao.get(approvalStatus.getRequestId());
                changeRequest.setWipStatus(environment.getProperty("approval.status.approve.type.acknowledgeBy.checkedBy"));
                approvalStatus.setDeliverDate(changeRequest.getDeliverDate());
                approvalStatusDao.save(approvalStatus);
                changeRequestDao.save(changeRequest);
            } else if (environment.getProperty("approval.user.acknowledgeCheckedBy").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.done").equals(approvalStatus.getStatus())) {
                ChangeRequest changeRequest = changeRequestDao.get(approvalStatus.getRequestId());
                changeRequest.setWipStatus(environment.getProperty("approval.status.approve.type.acknowledgeBy.requestBy"));
                approvalStatus.setDeliverDate(changeRequest.getDeliverDate());
                changeRequestDao.save(changeRequest);
                approvalStatusDao.save(approvalStatus);
            }

        }

    }
}
