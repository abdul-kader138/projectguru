package com.dreamchain.skeleton.service.impl;

import com.dreamchain.skeleton.dao.CategoryDao;
import com.dreamchain.skeleton.dao.TeamAllocationDao;
import com.dreamchain.skeleton.dao.UserDao;
import com.dreamchain.skeleton.model.Category;
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
    Environment environment;


    private static String TEAM_ALLOCATION_EXISTS = "This member already used.Please try again with new one!!!";
    private static String INVALID_INPUT = "Invalid input";
    private static String INVALID_TEAM_ALLOCATION = "Team allocation not exists";
    private static String BACK_DATED_DATA = "Team allocation data is old.Please try again with updated data";
    private static String ASSOCIATED_REQUEST = "Team is allocated with request.First remove tagging and try again";

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
        if ("".equals(validationMsg)) existingTeamAllocation = teamAllocationDao.findByUserId(teamAllocation.getItCoordinatorId(),
                teamAllocation.getApprovedById(),teamAllocation.getCompanyId(),
                teamAllocation.getProductId(),teamAllocation.getCategoryId());
//                userAllocation.getDepartmentId(),userAllocation.getProductId(),userAllocation.getCategoryId());
        if (existingTeamAllocation.getCategoryName() != null && validationMsg == "") validationMsg = TEAM_ALLOCATION_EXISTS;
        if ("".equals(validationMsg)) {
            SimpleDateFormat dateFormat = new SimpleDateFormat();
            Date date = dateFormat.parse(dateFormat.format(new Date()));
            teamAllocation.setCreatedBy(getUserId());
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
        String validationMsg = "";
        TeamAllocation newObj=new TeamAllocation();
        TeamAllocation existingTeamAllocation=new TeamAllocation();
        TeamAllocation teamAllocation=createObjForSave(teamAllocationObj);
        validationMsg = checkInput(teamAllocation);
        if (teamAllocation.getId() == 0l && validationMsg == "") validationMsg = INVALID_INPUT;
        if ("".equals(validationMsg)) existingTeamAllocation = teamAllocationDao.get(teamAllocation.getId());
        if (existingTeamAllocation == null && validationMsg == "") validationMsg = INVALID_TEAM_ALLOCATION;
        if (teamAllocation.getVersion() != existingTeamAllocation.getVersion() && validationMsg == "") validationMsg = BACK_DATED_DATA;
//        if ("".equals(validationMsg)) newObj = categoryDao.findByNewName(existingUserAllocation.getName(),category.getName(),category.getCompanyId(),category.getDepartmentId(),category.getProductId());
//        if (newObj.getName() != null && "".equals(validationMsg)) validationMsg = CATEGORY_EXISTS;
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
        if (teamAllocationId == 0l) validationMsg = INVALID_INPUT;
        TeamAllocation teamAllocation = teamAllocationDao.get(teamAllocationId);
        if (teamAllocation == null && validationMsg == "") validationMsg = INVALID_TEAM_ALLOCATION;

        //@todo need implement after Request implementation
//        List<Object> obj=departmentDao.countOfDepartment(departmentId);
//        if (obj.size() > 0 && validationMsg == "") validationMsg = ASSOCIATED_COMPANY;

        if ("".equals(validationMsg)) {
            teamAllocationDao.delete(teamAllocation);
        }
        return validationMsg;
    }

    @Override
    public List<TeamAllocation> findAll() {
        return teamAllocationDao.findAll();
    }


    // create User Allocation object for saving

    private TeamAllocation createObjForSave(Map<String, Object> teamAllocationObj) throws ParseException {
        TeamAllocation teamAllocation = new TeamAllocation();
        Category category=categoryDao.get(Long.parseLong((String)teamAllocationObj.get("categoryId")));
        User itCoordinator=userDao.get(Long.parseLong((String)teamAllocationObj.get("itCoordinatorId")));
        User approvedBy=userDao.get(Long.parseLong((String)teamAllocationObj.get("approvedById")));
        teamAllocation.setId(Long.parseLong((String) teamAllocationObj.get("id")));
        teamAllocation.setVersion(Long.parseLong((String) teamAllocationObj.get("version")));
        teamAllocation.setCompanyId(category.getCompanyId());
        teamAllocation.setCompanyName(category.getCompany().getName());
//        userAllocation.setDepartmentId(category.getDepartmentId());
//        userAllocation.setDepartmentName(category.getDepartment().getName());
        teamAllocation.setProductId(category.getProductId());
        teamAllocation.setProductName(category.getProduct().getName());
        teamAllocation.setCategoryId(category.getId());
        teamAllocation.setCategoryName(category.getName());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser=(User) auth.getPrincipal();
        teamAllocation.setUserType(loggedUser.getUserType());
        teamAllocation.setItCoordinatorId(itCoordinator.getId());
        teamAllocation.setItCoordinator(itCoordinator);
        teamAllocation.setApprovedById(approvedBy.getId());
        teamAllocation.setApprovedBy(approvedBy);
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        teamAllocation.setUpdatedBy(getUserId());
        teamAllocation.setUpdatedOn(date);
        return teamAllocation;

    }

    // check for invalid data
    private String checkInput(TeamAllocation teamAllocation) {
        String msg = "";

        //server side validation check
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Set<ConstraintViolation<TeamAllocation>> constraintViolations = validator.validate(teamAllocation);
        if (constraintViolations.size() > 0 && msg == "") msg = INVALID_INPUT;

        return msg;
    }

    private TeamAllocation setUpdateCategoryValue(TeamAllocation objFromUI,TeamAllocation existingTeamAllocation) throws ParseException {
        TeamAllocation teamAllocation = new TeamAllocation();
        teamAllocation.setId(objFromUI.getId());
        teamAllocation.setVersion(objFromUI.getVersion());
        teamAllocation.setUserType(objFromUI.getUserType().trim());
        teamAllocation.setCompanyId(objFromUI.getCompanyId());
        teamAllocation.setCompanyName(objFromUI.getCompanyName());
//        userAllocation.setDepartmentId(objFromUI.getDepartmentId());
//        userAllocation.setDepartmentName(objFromUI.getDepartmentName());
        teamAllocation.setProductId(objFromUI.getProductId());
        teamAllocation.setProductName(objFromUI.getProductName());
        teamAllocation.setCategoryId(objFromUI.getCategoryId());
        teamAllocation.setCategoryName(objFromUI.getCategoryName());
        teamAllocation.setItCoordinatorId(objFromUI.getItCoordinator().getId());
        teamAllocation.setItCoordinator(objFromUI.getItCoordinator());
        teamAllocation.setApprovedById(objFromUI.getApprovedBy().getId());
        teamAllocation.setApprovedBy(objFromUI.getApprovedBy());
        teamAllocation.setCreatedBy(existingTeamAllocation.getCreatedBy());
        teamAllocation.setCreatedOn(existingTeamAllocation.getCreatedOn());
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        Date date = dateFormat.parse(dateFormat.format(new Date()));
        teamAllocation.setUpdatedBy(getUserId());
        teamAllocation.setUpdatedOn(date);
        return teamAllocation;
    }

    private String getUserId(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user=(User)auth.getPrincipal();
        return user.getEmail();
    }


}
