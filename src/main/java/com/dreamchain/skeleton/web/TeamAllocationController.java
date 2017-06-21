package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.TeamAllocation;
import com.dreamchain.skeleton.service.TeamAllocationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@PropertySource("classpath:config.properties")
public class TeamAllocationController {
    @Autowired
    TeamAllocationService teamAllocationService;
    @Autowired
    Environment environment;

    static final Logger logger =
            LoggerFactory.getLogger(TeamAllocationController.class.getName());

    @RequestMapping("/team_allocation")
    public ModelAndView main()  {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ModelAndView model = new ModelAndView();
        String pageName = "team_allocation";
        model.setViewName(pageName);
        return model;

    }

    @RequestMapping(value = "team_allocation/team_allocationList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<TeamAllocation> loadTeamAllocationList() {
        List<TeamAllocation> teamAllocationList = new ArrayList();
        logger.info("Loading all allocation info: >> ");
        teamAllocationList = teamAllocationService.findAll();
        logger.info("Loading all allocation info: << total " + teamAllocationList.size());
        return teamAllocationList;
    }


    @RequestMapping(value = "team_allocation/save", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    Map saveTeamAllocation(@RequestBody Map<String, Object> teamAllocationInfo) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new allocation: >>");
        objList = teamAllocationService.save(teamAllocationInfo);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("teamAllocation.save.success.msg"));
            successMsg = environment.getProperty("teamAllocation.save.success.msg");
        }
        logger.info("creating new allocation: << " + successMsg + validationError);
        return objList;
    }


    @RequestMapping(value = "team_allocation/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Map deleteTeamAllocation(@RequestBody String teamAllocationId) throws ParseException {
        HashMap serverResponse = new HashMap();
        String successMsg = "";
        String validationError = "";
        logger.info("Delete allocation:  >> ");
        validationError = teamAllocationService.delete(Long.parseLong(teamAllocationId));
        if (validationError.length() == 0) successMsg = environment.getProperty("teamAllocation.delete.success.msg");
        logger.info("Delete allocation:  << " + successMsg + validationError);
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        return serverResponse;
    }


    @RequestMapping(value = "team_allocation/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map updateUserAllocation(@RequestBody Map<String, Object> teamAllocationObj) throws ParseException {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("Updating allocation: >>");
        objList = teamAllocationService.update(teamAllocationObj);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("teamAllocation.update.success.msg"));
            successMsg = environment.getProperty("teamAllocation.update.success.msg");
        }
        logger.info("Updating allocation:  << " + successMsg + validationError);
        return objList;
    }
}
