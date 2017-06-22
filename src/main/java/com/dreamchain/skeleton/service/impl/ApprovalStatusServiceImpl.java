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
    private static String BACK_DATED_DATA = "Approval data is old.Please try again with updated data";

    @Override
    public ApprovalStatus get(Long id) {
        return approvalStatusDao.get(id);
    }

    @Transactional
    public Map<String, Object> update(Map<String, Object> approvalObj,HttpServletRequest request) throws ParseException {
        Map<String, Object> obj=new HashMap<>();
        String validationMsg = "";
        List<ApprovalStatus> ObjList=new ArrayList<>();
        Integer approvedBy = (Integer)approvalObj.get("id");
        Integer versionId = (Integer)approvalObj.get("version");
        long version = versionId.longValue();
        long approvedById = approvedBy.longValue();
        ApprovalStatus approvalStatus = approvalStatusDao.findById(approvedById);
        if (approvalStatus.getId() == 0l && validationMsg == "") validationMsg = INVALID_INPUT;
        if (approvalStatus.getVersion() != version && validationMsg == "") validationMsg = BACK_DATED_DATA;
        if("".equals(validationMsg)) ObjList =saveApprovalObj(approvalStatus);
        if("".equals(validationMsg)) UpdateApprovalObj(ObjList);
        obj.put("approval_details",ObjList.get(0));
        obj.put("validationError",validationMsg);
        if("".equals(validationMsg)) ObjList=findByUserId(request);
        obj.put("notificationCount",ObjList.size());
        return obj;
    }


    @Override
    public List<ApprovalStatus> findByUserId(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        User existingUser = userDao.findByUserName(user.getEmail());
        List<ApprovalStatus> approvalStatusList=approvalStatusDao.findByUserId(existingUser.getId());
        httpSession.setAttribute("notificationCount",approvalStatusList.size());
        return approvalStatusList;
    }

    private List<ApprovalStatus> saveApprovalObj(ApprovalStatus approvalStatus) throws ParseException {
        List<ApprovalStatus> approvalStatusList = new ArrayList<>();

        if (environment.getProperty("approval.user.checkedBy").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.waiting").equals(approvalStatus.getStatus())) {
            approvalStatusList.add(setApprovalStatusValue(approvalStatus, environment.getProperty("approval.status.done")));
            ApprovalStatus itCoordinatorStatus = approvalStatusDao.findByCompanyAndProductAndCategory(approvalStatus.getCompanyId(), approvalStatus.getProductId(), approvalStatus.getCategoryId(), approvalStatus.getRequestName(), environment.getProperty("approval.user.itCoordinator"));
            approvalStatusList.add(setApprovalStatusValue(itCoordinatorStatus, environment.getProperty("approval.status.waiting")));
        }
        if (environment.getProperty("approval.user.itCoordinator").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.waiting").equals(approvalStatus.getStatus())) {
            approvalStatusList.add(setApprovalStatusValue(approvalStatus, environment.getProperty("approval.status.done")));
            ApprovalStatus approvedByStatus = approvalStatusDao.findByCompanyAndProductAndCategory(approvalStatus.getCompanyId(), approvalStatus.getProductId(), approvalStatus.getCategoryId(), approvalStatus.getRequestName(), environment.getProperty("approval.user.approvedBy"));
            approvalStatusList.add(setApprovalStatusValue(approvedByStatus, environment.getProperty("approval.status.waiting")));
        }
        if (environment.getProperty("approval.user.approvedBy").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.waiting").equals(approvalStatus.getStatus())) {
            approvalStatusList.add(setApprovalStatusValue(approvalStatus, environment.getProperty("approval.status.done")));
            ApprovalStatus acknowledgementITStatus = approvalStatusDao.findByCompanyAndProductAndCategory(approvalStatus.getCompanyId(), approvalStatus.getProductId(), approvalStatus.getCategoryId(), approvalStatus.getRequestName(), environment.getProperty("approval.user.acknowledgementIT"));
            approvalStatusList.add(setApprovalStatusValue(acknowledgementITStatus, environment.getProperty("approval.status.waiting")));
        }
        if (environment.getProperty("approval.user.acknowledgementIT").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.waiting").equals(approvalStatus.getStatus())) {
            approvalStatusList.add(setApprovalStatusValue(approvalStatus, environment.getProperty("approval.status.done")));
            ApprovalStatus acknowledgeCheckedByStatus = approvalStatusDao.findByCompanyAndProductAndCategory(approvalStatus.getCompanyId(), approvalStatus.getProductId(), approvalStatus.getCategoryId(), approvalStatus.getRequestName(), environment.getProperty("approval.user.acknowledgeCheckedBy"));
            approvalStatusList.add(setApprovalStatusValue(acknowledgeCheckedByStatus, environment.getProperty("approval.status.waiting")));
        }
        if (environment.getProperty("approval.user.acknowledgeCheckedBy").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.waiting").equals(approvalStatus.getStatus())) {
            approvalStatusList.add(setApprovalStatusValue(approvalStatus, environment.getProperty("approval.status.done")));
            ApprovalStatus acknowledgementStatus = approvalStatusDao.findByCompanyAndProductAndCategory(approvalStatus.getCompanyId(), approvalStatus.getProductId(), approvalStatus.getCategoryId(), approvalStatus.getRequestName(), environment.getProperty("approval.user.acknowledgement"));
            approvalStatusList.add(setApprovalStatusValue(acknowledgementStatus, environment.getProperty("approval.status.waiting")));
        }
        if (environment.getProperty("approval.user.acknowledgement").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.waiting").equals(approvalStatus.getStatus())) {
            approvalStatusList.add(setApprovalStatusValue(approvalStatus, environment.getProperty("approval.status.done")));
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

    private void UpdateApprovalObj(List<ApprovalStatus> statusList){
        for(ApprovalStatus approvalStatus:statusList){
            approvalStatusDao.save(approvalStatus);
            if(environment.getProperty("approval.user.acknowledgement").equals(approvalStatus.getUserType()) && environment.getProperty("approval.status.done").equals(approvalStatus.getStatus())) {
                ChangeRequest changeRequest=changeRequestDao.findByName(approvalStatus.getRequestName(),approvalStatus.getCompanyId(),approvalStatus.getProductId(),approvalStatus.getCategoryId());
                changeRequest.setStatus(environment.getProperty("approval.status.done"));
                changeRequestDao.save(changeRequest);
            }
        }
    }

}
