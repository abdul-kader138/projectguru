package com.dreamchain.skeleton.service.impl;


import com.dreamchain.skeleton.dao.ApprovalStatusDao;
import com.dreamchain.skeleton.dao.ChangeRequestDao;
import com.dreamchain.skeleton.dao.DeclineRequestDao;
import com.dreamchain.skeleton.dao.UserDao;
import com.dreamchain.skeleton.model.ApprovalStatus;
import com.dreamchain.skeleton.model.ChangeRequest;
import com.dreamchain.skeleton.model.DeclineRequest;
import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.DeclineRequestService;
import com.dreamchain.skeleton.utility.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

//import javax.mail.Authenticator;


import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    UserDao userDao;
    @Autowired
    Environment environment;

    private static final String INVALID_INPUT = "Invalid input";
    private static final String INVALID_REQUEST = "Request not exists";
    private static final String BACK_DATED_DATA = "Request data is old.Please try again with updated data";
    private static final String EMAIL_BODY_SAVE = "Request is declined by the user.Request Name ##";
    private static final String EMAIL_HEADER_SAVE= "Request is declined.Request Name ##";





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
//            changeRequest.setDeployedOn(null); @todo - need to talk (For null value passing or previous value passing)
            changeRequest.setWipStatus(environment.getProperty("approval.status.approve.type.acknowledgeBy.itCoordinator"));
            ApprovalStatus approvalStatusForItCod=approvalStatusDao.findByRequestIdAndUserType(changeRequest.getId(),environment.getProperty("approval.user.acknowledgementIT"));
            approvalStatusForItCod.setStatus(environment.getProperty("approval.status.waiting"));
            approvalStatus.setStatus(environment.getProperty("approval.status.none"));
            long requestId= changeRequestDao.save(changeRequest);
            long declineId= declineRequestDao.save(declineRequest);
            long approvalId=approvalStatusDao.save(approvalStatusForItCod);
            User acknowledgedItCoordinator=userDao.get(changeRequest.getItCoordinatorId());
            sendEmail(acknowledgedItCoordinator.getEmail(), EMAIL_HEADER_SAVE+ changeRequest.getName(), EMAIL_BODY_SAVE + changeRequest.getName());
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

}
