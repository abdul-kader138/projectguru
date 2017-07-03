package com.dreamchain.skeleton.service.impl;


import com.dreamchain.skeleton.dao.ApprovalStatusDao;
import com.dreamchain.skeleton.dao.ChangeRequestDao;
import com.dreamchain.skeleton.dao.DeclineRequestDao;
import com.dreamchain.skeleton.model.ApprovalStatus;
import com.dreamchain.skeleton.model.ChangeRequest;
import com.dreamchain.skeleton.model.DeclineRequest;
import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.ChangeRequestService;
import com.dreamchain.skeleton.service.DeclineRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
@PropertySource("classpath:config.properties")
public class DeclineRequestServiceImpl implements DeclineRequestService {
    @Autowired
    ChangeRequestDao changeRequestDao;
    @Autowired
    ApprovalStatusDao approvalStatusDao;
    @Autowired
    DeclineRequestDao declineRequestDao;
    @Autowired
    Environment environment;

    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_REQUEST = "Request not exists";
    private static String BACK_DATED_DATA = "Request data is old.Please try again with updated data";




    @Transactional(readOnly = true)
    public DeclineRequest get(Long id) {
        return null;
    }

    @Transactional
    public Map<String, Object> save(Map<String, Object> declineRequestObj) throws ParseException {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        long approveId=Long.parseLong((String) declineRequestObj.get("approveId"));
        long requestedId=Long.parseLong((String)declineRequestObj.get("requestedId"));
        long version=Long.parseLong((String)declineRequestObj.get("version"));
        DeclineRequest newDeclineRequest=new DeclineRequest();
        ApprovalStatus approvalStatus= approvalStatusDao.get(approveId);
        ChangeRequest changeRequest= changeRequestDao.get(requestedId);
        if("".equals(validationMsg) && approvalStatus.getStatus() ==null) validationMsg=INVALID_INPUT;
        if("".equals(validationMsg) && changeRequest.getStatus() ==null) validationMsg=INVALID_INPUT;
        if("".equals(validationMsg) && approveId != approvalStatus.getId() && requestedId != changeRequest.getId()) validationMsg=INVALID_INPUT;
        if ("".equals(validationMsg) && version != approvalStatus.getVersion()) validationMsg=BACK_DATED_DATA;
        DeclineRequest declineRequest=createObjForSave(declineRequestObj,changeRequest,approvalStatus);
        validationMsg = checkInput(declineRequest);
        if ("".equals(validationMsg)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            Date date = dateFormat.parse(dateFormat.format(new Date()));
            changeRequest.setUpdatedBy(getUserId().getEmail());
            changeRequest.setCreatedOn(date);
            changeRequest.setDeclineCause(declineRequest.getDeclineCause());
            changeRequest.setWipStatus(environment.getProperty("approval.status.approve.type.acknowledgeBy.itCoordinator"));
            // need to get IT cordinator object and set it status waiting
            long requestId= changeRequestDao.save(changeRequest);
            long declineId= declineRequestDao.save(declineRequest);
        }
        obj.put("validationError",validationMsg);
        return obj;
    }

    // check for invalid data
    private String checkInput(DeclineRequest declineRequest) {
        String msg = "";

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<DeclineRequest>> constraintViolations = validator.validate(declineRequest);
        if (constraintViolations.size() > 0 && "".equals(msg)) msg = INVALID_INPUT;

        return msg;
    }

    private DeclineRequest createObjForSave(Map<String, Object> declineRequestObj,ChangeRequest changeRequest,ApprovalStatus approvalStatus) throws ParseException {
        DeclineRequest declineRequest = new DeclineRequest();
        declineRequest.setDeclineCause((String) declineRequestObj.get("cause"));
        declineRequest.setApprovalStatusId(approvalStatus.getId());
        declineRequest.setRequestsId(changeRequest.getId());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        declineRequest.setCreatedBy(getUserId().getEmail());
        declineRequest.setCreatedOn(date);
        return declineRequest;

    }

    private User getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User)auth.getPrincipal();
    }

}
