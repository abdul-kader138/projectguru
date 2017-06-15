package com.dreamchain.skeleton.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.dreamchain.skeleton.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.dreamchain.skeleton.service.UserService;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.util.*;

//@RequestMapping(UserController.URL)
@PropertySource("classpath:config.properties")
@Controller

public class UserController {

    private static final Logger logger =
            LoggerFactory.getLogger(UserController.class.getName());


    static final String URL = "/user";


    @Autowired
    UserService userService;
    @Autowired
    Environment environment;


    @RequestMapping("/user")
    public ModelAndView main()  {
        ModelAndView model = new ModelAndView();
        String pageName = "user";
        model.setViewName(pageName);
        return model;

    }


    @RequestMapping(value = "/user/save", method = RequestMethod.POST)
    public
    @ResponseBody
    Map saveUser(MultipartHttpServletRequest request) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new user: >>");
        objList = userService.save(request,"user");
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("user.save.success.msg"));
            successMsg = environment.getProperty("user.save.success.msg");
        }
        logger.info("creating new user: << " + successMsg + validationError);
        return objList;
    }


    @RequestMapping(value = "/user/changePassword", method = RequestMethod.POST, headers = {"Content-type=application/json"})
    public
    @ResponseBody
    Map ChangePassword(@RequestBody Map<String, String> userInfo, HttpSession httpSession) throws Exception {
        String successMsg = "";
        String invalidUserError="";
        String validationError="";
        Map<String, Object> objList = new HashMap<>();
        logger.info("Changing user password: >>");
        objList = userService.changePassword(userInfo.get("oldPassword"), userInfo.get("password"));
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0 ) {
            objList.put("successMsg", environment.getProperty("user.password.change.success.msg"));
            successMsg = environment.getProperty("user.password.change.success.msg");
            httpSession.setAttribute("passwordMsg",environment.getProperty("user.password.change.success.msg"));
        }
        logger.info("Changing user password: << "+successMsg+invalidUserError+invalidUserError);
        return objList;
    }



    @RequestMapping(value = "/user/userList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<User> loadUserList() {
        List userList=new ArrayList();
        logger.info("Loading all user info: >> ");
            userList = userService.findAll();
        logger.info("Loading all user info: << total "+userList.size());
        return userList;
    }




    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map updateUser(MultipartHttpServletRequest request) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("Updating user: >>");
        objList = userService.updateUser(request,"user");
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("user.update.success.msg"));
            successMsg = environment.getProperty("user.update.success.msg");
        }
        logger.info("Updating user:: << " + successMsg + validationError);
        return objList;

   }


    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Map deleteUser(@RequestBody String userId,HttpServletRequest request) throws ParseException {
        HashMap serverResponse = new HashMap();
        String successMsg = "";
        String validationError = "";
        logger.info("Delete user:  >> ");
        validationError = userService.delete(Long.parseLong(userId), request);
        if (validationError.length() == 0) successMsg = environment.getProperty("user.delete.success.msg");
        logger.info("Delete user:  << " + successMsg + validationError);
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        return serverResponse;
    }



    private boolean checkLoggedInUserExistence(HttpSession httpSession) {
        boolean isLoggedUserExists = false;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = (User) auth.getPrincipal();
        User user = userService.findByUserName(loggedUser.getEmail());
        if (user == null) {
            isLoggedUserExists = true;
            httpSession.invalidate();
        }
        return isLoggedUserExists;
    }



//    start team member


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
        objList = userService.save(request,"team_member");
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
        userList = userService.findAll();
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
        objList = userService.updateUser(request,"team_member");
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
        validationError = userService.delete(Long.parseLong(userId), request);
        if (validationError.length() == 0) successMsg = environment.getProperty("team.delete.success.msg");
        logger.info("Delete team:  << " + successMsg + validationError);
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        return serverResponse;
    }


}
