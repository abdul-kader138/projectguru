package com.dreamchain.skeleton.web;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.permission.CheckLoggedInUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.dreamchain.skeleton.service.UserService;

import java.text.ParseException;
import java.util.*;

@Controller
@RequestMapping(UserController.URL)
@PropertySource("classpath:config.properties")

public class UserController {

    private static final Logger logger =
            LoggerFactory.getLogger(UserController.class.getName());


    static final String URL = "/user";

    @Autowired
    UserService userService;
    @Autowired
    Environment environment;

    


    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    Map saveUser(@RequestBody User user,HttpSession httpSession) throws ParseException {
        String successMsg = "";
        String validationError="";
        String invalidUserError="";
        logger.info("creating new user: >>");
        boolean isLoggedUserInvalid=checkLoggedInUserExistence(httpSession);
        if(isLoggedUserInvalid) invalidUserError= environment.getProperty("user.invalid.error.msg");
        if(!isLoggedUserInvalid)validationError = userService.save(user);
        if (validationError.length() == 0 && !isLoggedUserInvalid) successMsg = environment.getProperty("user.save.success.msg");
        logger.info("creating new user: << " + successMsg + invalidUserError + invalidUserError);
        return createServerResponse(successMsg,validationError,invalidUserError,null);
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



    @RequestMapping(value = "/changePassword", method = RequestMethod.POST, headers = {"Content-type=application/json"})
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





    @RequestMapping(value = "/userList", method = RequestMethod.GET)
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


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map updateUser(@RequestBody User user,HttpSession httpSession) throws ParseException {
        String successMsg = "";
        String validationError="";
        String invalidUserError="";
        logger.info("Updating user: >>");
        boolean isLoggedUserInvalid=checkLoggedInUserExistence(httpSession);
        if(isLoggedUserInvalid) invalidUserError= environment.getProperty("user.invalid.error.msg");
        if(!isLoggedUserInvalid) validationError = userService.updateUser(user);
        if (validationError.length() == 0 && !isLoggedUserInvalid) successMsg = environment.getProperty("user.update.success.msg");
        logger.info("Updating user:  << "+successMsg+invalidUserError+invalidUserError);
        return createServerResponse(successMsg,validationError,invalidUserError,userService.get(user.getId()));

   }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Map deleteUser(@RequestBody String id,HttpSession httpSession) throws ParseException {
        String successMsg = "";
        String validationError = "";
        String invalidUserError="";
        logger.info("Delete user:  >> ");
        boolean isLoggedUserInvalid=checkLoggedInUserExistence(httpSession);
        if(isLoggedUserInvalid) invalidUserError= environment.getProperty("user.invalid.error.msg");
        if(!isLoggedUserInvalid) validationError = userService.delete(Long.parseLong(id));
        if (validationError.length() == 0 && !isLoggedUserInvalid) successMsg = environment.getProperty("user.delete.success.msg");
        logger.info("Delete user:  << "+successMsg+invalidUserError+invalidUserError);
        return createServerResponse(successMsg,validationError,invalidUserError,null);
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
