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
    Map saveUser(@RequestBody MultipartHttpServletRequest request) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new user: >>");
        objList = userService.save(request);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("user.save.success.msg"));
            successMsg = environment.getProperty("user.save.success.msg");
        }
        logger.info("creating new user: << " + successMsg + validationError);
        return objList;
    }


    @RequestMapping(value = "/principle", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    Map userPrinciple() {
        logger.info("Getting Logged in user info: >>");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) auth.getPrincipal();
        HashMap<String, String> userDetails = new HashMap<>();
        userDetails.put("userName", currentUser.getName());
        userDetails.put("email", currentUser.getEmail());
        userDetails.put("id", String.valueOf(currentUser.getId()));
        userDetails.put("version", String.valueOf(currentUser.getVersion()));
        logger.info("Getting Logged in user info: << "+currentUser.getName());
        return userDetails;
    }



    @RequestMapping(value = "/user/changePassword", method = RequestMethod.POST, headers = {"Content-type=application/json"})
    public
    @ResponseBody
    Map ChangePassword(@RequestBody Map<String, String> userInfo, HttpSession httpSession) throws Exception {
        String successMsg = "";
        String invalidUserError="";
        String validationError="";
        logger.info("Changing user password: >>");
        boolean isLoggedUserInvalid=checkLoggedInUserExistence(httpSession);
        if(isLoggedUserInvalid) invalidUserError= environment.getProperty("user.invalid.error.msg");
        if(!isLoggedUserInvalid) validationError = userService.changePassword(userInfo.get("userName"), userInfo.get("oldPassword"), userInfo.get("newPassword"));
        if (validationError.length() == 0 && !isLoggedUserInvalid) {
            httpSession.invalidate();
            successMsg = environment.getProperty("user.password.change.success.msg");
        }
        logger.info("Changing user password: << "+successMsg+invalidUserError+invalidUserError);
        return createServerResponse(successMsg,validationError,invalidUserError,null);
    }





    @RequestMapping(value = "/user/userList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<User> loadUserList(@RequestBody String email,HttpSession httpSession) {
        List userList=new ArrayList();
        logger.info("Loading all user info: >> ");
        boolean isLoggedUserInvalid=checkLoggedInUserExistence(httpSession);
        if(!isLoggedUserInvalid)
        userList = userService.findAll(email);
        logger.info("Loading all user info: << total "+userList.size());
        return userList;
    }

    @RequestMapping(value = "/user/userList1", method = RequestMethod.GET)
    public
    @ResponseBody
    List<User> loadUserList1() {
        List userList=new ArrayList();
        logger.info("Loading all user info: >> ");
            userList = userService.findAll();
        logger.info("Loading all user info: << total "+userList.size());
        return userList;
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map updateUser(@RequestBody MultipartHttpServletRequest request) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("Updating user: >>");
        objList = userService.updateUser(request);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("user.update.success.msg"));
            successMsg = environment.getProperty("user.update.success.msg");
        }
        logger.info("Updating user:: << " + successMsg + validationError);
        return objList;

   }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Map deleteUser(String userId,HttpServletRequest request) throws ParseException {
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



    private Map createServerResponse(String successMsg,String validationError,String invalidUserError,User user){
        HashMap serverResponse = new HashMap();
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        serverResponse.put("invalidUserError", invalidUserError);
        serverResponse.put("user",user);
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


}
