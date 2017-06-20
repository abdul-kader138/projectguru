package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.TeamMemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:config.properties")
@Controller
public class TeamMemberController {
    private static final Logger logger =
            LoggerFactory.getLogger(TeamMemberController.class.getName());

    @Autowired
    TeamMemberService teamMemberService;
    @Autowired
    Environment environment;



    @RequestMapping("/team")
    public ModelAndView mainView()  {
        ModelAndView model = new ModelAndView();
        String pageName = "team";
        model.setViewName(pageName);
        return model;

    }



    @RequestMapping(value = "/team/save", method = RequestMethod.POST)
    public
    @ResponseBody
    Map saveTeam(MultipartHttpServletRequest request) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new team: >>");
        objList = teamMemberService.save(request,"team_member");
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("team.save.success.msg"));
            successMsg = environment.getProperty("team.save.success.msg");
        }
        logger.info("creating new team: << " + successMsg + validationError);
        return objList;
    }



    @RequestMapping(value = "/team/teamList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<User> loadTeamList() {
        List userList=new ArrayList();
        logger.info("Loading all team info: >> ");
        userList = teamMemberService.findAll();
        logger.info("Loading all team info: << total "+userList.size());
        return userList;
    }




    @RequestMapping(value = "/team/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map updateTeam(MultipartHttpServletRequest request) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("Updating team: >>");
        objList = teamMemberService.updateUser(request,"team_member");
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("team.update.success.msg"));
            successMsg = environment.getProperty("team.update.success.msg");
        }
        logger.info("Updating team:: << " + successMsg + validationError);
        return objList;

    }


    @RequestMapping(value = "/team/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Map deleteTeam(@RequestBody String userId,HttpServletRequest request) throws ParseException {
        HashMap serverResponse = new HashMap();
        String successMsg = "";
        String validationError = "";
        logger.info("Delete team:  >> ");
        validationError = teamMemberService.delete(Long.parseLong(userId), request);
        if (validationError.length() == 0) successMsg = environment.getProperty("team.delete.success.msg");
        logger.info("Delete team:  << " + successMsg + validationError);
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        return serverResponse;
    }



}
