package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.CategoryDao;
import com.dreamchain.skeleton.dao.ChangeRequestDao;
import com.dreamchain.skeleton.dao.TeamAllocationDao;
import com.dreamchain.skeleton.dao.UserDao;
import com.dreamchain.skeleton.model.Category;
import com.dreamchain.skeleton.model.ChangeRequest;
import com.dreamchain.skeleton.model.TeamAllocation;
import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.TeamAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.*;

@Service
public class TeamAllocationServiceImpl implements TeamAllocationService {


    @Autowired
    TeamAllocationDao teamAllocationDao ;
    @Autowired
    CategoryDao categoryDao ;
    @Autowired
    UserDao userDao ;
    @Autowired
    ChangeRequestDao changeRequestDao;
    @Autowired
    Environment environment;


    private static final String TEAM_ALLOCATION_EXISTS = "This Product and Team members are already allocated.Please try again with new one!!!";
    private static final String INVALID_INPUT = "Invalid input";
    private static final String INVALID_TEAM_ALLOCATION = "Team allocation not exists";
    private static final String BACK_DATED_DATA = "Team allocation data is old.Please try again with updated data";
    private static final String SAME_ALLOCATED_USER = "Checked By and Requester name can't be same";
    private static final String ASSOCIATED_REQUEST = "This allocation is associated with request.First remove tagging and try again";

    @Transactional(readOnly = true)
    public TeamAllocation get(Long id) {
        return teamAllocationDao.get(id);
    }

    @Transactional
    public Map<String, Object> save(Map<String, Object> teamAllocationObj) throws ParseException {
        Map<String,Object> obj=new HashMap<>();
        String validationMsg = "";
        TeamAllocation newTeamAllocation=new TeamAllocation();
        TeamAllocation existingTeamAllocation=new TeamAllocation();
        TeamAllocation teamAllocation=createObjForSave(teamAllocationObj);
        validationMsg = checkInput(teamAllocation);
        if ("".equals(validationMsg)) existingTeamAllocation = teamAllocationDao.findByProductAndCategory(teamAllocation.getCompanyId(),
                teamAllocation.getProductId(),teamAllocation.getCategory().getId());
        if (existingTeamAllocation.getCheckedBy() != null && "".equals(validationMsg)) validationMsg = TEAM_ALLOCATION_EXISTS;
        if(teamAllocation.getCheckedBy().getId() == teamAllocation.getRequestedBy().getId() ) validationMsg=SAME_ALLOCATED_USER;
        if ("".equals(validationMsg)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            Date date = dateFormat.parse(dateFormat.format(new Date()));
            teamAllocation.setCreatedBy(getUserId().getEmail());
            teamAllocation.setCreatedOn(date);
            long id= teamAllocationDao.save(teamAllocation);
            newTeamAllocation=teamAllocationDao.get(id);
        }
        obj.put("teamAllocation",newTeamAllocation);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public Map<String, Object> update(Map<String, Object> teamAllocationObj) throws ParseException {
        Map<String,Object> obj=new HashMap<>();
        ChangeRequest changeRequest=new ChangeRequest();
        String validationMsg = "";
        TeamAllocation newObj=new TeamAllocation();
        TeamAllocation existingTeamAllocation=new TeamAllocation();
        TeamAllocation teamAllocation=createObjForSave(teamAllocationObj);
        validationMsg = checkInput(teamAllocation);
        if (teamAllocation.getId() == 0l && "".equals(validationMsg)) validationMsg = INVALID_INPUT;
        if(teamAllocation.getCheckedBy().getId() == teamAllocation.getRequestedBy().getId() ) validationMsg=SAME_ALLOCATED_USER;
        if ("".equals(validationMsg)) existingTeamAllocation = teamAllocationDao.get(teamAllocation.getId());
        if (existingTeamAllocation == null && "".equals(validationMsg)) validationMsg = INVALID_TEAM_ALLOCATION;
        if("".equals(validationMsg)) changeRequest=changeRequestDao.findByTeamAllocationId(existingTeamAllocation.getId());
        if("".equals(validationMsg) && changeRequest.getName() !=null) validationMsg=ASSOCIATED_REQUEST;
        if (teamAllocation.getVersion() != existingTeamAllocation.getVersion() && "".equals(validationMsg)) validationMsg = BACK_DATED_DATA;
        if ("".equals(validationMsg)) {
            newObj=setUpdateCategoryValue(teamAllocation, existingTeamAllocation);
            teamAllocationDao.update(newObj);
        }
        obj.put("teamAllocation",newObj);
        obj.put("validationError",validationMsg);
        return obj;
    }

    @Transactional
    public String delete(Long teamAllocationId) {
        String validationMsg = "";
        ChangeRequest changeRequest=new ChangeRequest();
        if (teamAllocationId == 0l) validationMsg = INVALID_INPUT;
        TeamAllocation teamAllocation = teamAllocationDao.get(teamAllocationId);
        if (teamAllocation == null && "".equals(validationMsg)) validationMsg = INVALID_TEAM_ALLOCATION;
        if("".equals(validationMsg)) changeRequest=changeRequestDao.findByTeamAllocationId(teamAllocation.getId());
        if("".equals(validationMsg) && changeRequest.getName() !=null) validationMsg=ASSOCIATED_REQUEST;
        if ("".equals(validationMsg)) {
            teamAllocationDao.delete(teamAllocation);
        }
        return validationMsg;
    }

    @Transactional(readOnly=true)
    public List<TeamAllocation> findAll() {
        return teamAllocationDao.findAll();
    }


    // create User Allocation object for saving

    private TeamAllocation createObjForSave(Map<String, Object> teamAllocationObj) throws ParseException {
        TeamAllocation teamAllocation = new TeamAllocation();
        Category category=categoryDao.get(Long.parseLong((String)teamAllocationObj.get("categoryId")));
        User requestByUser=userDao.get(Long.parseLong((String)teamAllocationObj.get("requestById")));
        User checkedByUser=userDao.get(Long.parseLong((String)teamAllocationObj.get("checkedById")));
        teamAllocation.setId(Long.parseLong((String) teamAllocationObj.get("id")));
        teamAllocation.setVersion(Long.parseLong((String) teamAllocationObj.get("version")));
        teamAllocation.setCompanyId(category.getCompanyId());
        teamAllocation.setProductId(category.getProductId());
        teamAllocation.setCategory(category);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser=(User) auth.getPrincipal();
        teamAllocation.setUserType(loggedUser.getUserType());
        teamAllocation.setCheckedBy(checkedByUser);
        teamAllocation.setRequestedBy(requestByUser);
        teamAllocation.setClientId(getUserId().getClientId());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        teamAllocation.setUpdatedBy(getUserId().getEmail());
        teamAllocation.setUpdatedOn(date);
        return teamAllocation;

    }

    // check for invalid data
    private String checkInput(TeamAllocation teamAllocation) {
        String msg = "";

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<TeamAllocation>> constraintViolations = validator.validate(teamAllocation);
        if (constraintViolations.size() > 0 && "".equals(msg)) msg = INVALID_INPUT;

        return msg;
    }

    private TeamAllocation setUpdateCategoryValue(TeamAllocation objFromUI,TeamAllocation existingTeamAllocation) throws ParseException {
        TeamAllocation teamAllocation = new TeamAllocation();
        teamAllocation.setId(objFromUI.getId());
        teamAllocation.setVersion(objFromUI.getVersion());
        teamAllocation.setUserType(objFromUI.getUserType().trim());
        teamAllocation.setCompanyId(objFromUI.getCompanyId());
        teamAllocation.setProductId(objFromUI.getProductId());
        teamAllocation.setCategory(objFromUI.getCategory());
        teamAllocation.setRequestedBy(objFromUI.getRequestedBy());
        teamAllocation.setCheckedBy(objFromUI.getCheckedBy());
        teamAllocation.setClientId(getUserId().getClientId());
        teamAllocation.setCreatedBy(existingTeamAllocation.getCreatedBy());
        teamAllocation.setCreatedOn(existingTeamAllocation.getCreatedOn());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        teamAllocation.setUpdatedBy(getUserId().getEmail());
        teamAllocation.setUpdatedOn(date);
        return teamAllocation;
    }

    private User getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User)auth.getPrincipal();
    }


}
