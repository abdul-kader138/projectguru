package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.ApprovalStatus;
import com.dreamchain.skeleton.service.ApprovalStatusService;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@PropertySource("classpath:config.properties")
public class ApprovalStatusController {

    @Autowired
    ApprovalStatusService approvalStatusService;
    @Autowired
    Environment environment;

    static final Logger logger =
            LoggerFactory.getLogger(ApprovalStatusController.class.getName());


    @RequestMapping("/approval_details")
    public ModelAndView main()  {
        ModelAndView model = new ModelAndView();
        String pageName = "approval_details";
        model.setViewName(pageName);
        return model;
    }

    @RequestMapping(value = "approval_details/approval_detailsList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ApprovalStatus> loadApprovalList(HttpServletRequest request) {
        List<ApprovalStatus> approvalList = new ArrayList();
        logger.info("Loading all approval list info: >> ");
        approvalList = approvalStatusService.findByUserId(request);
        logger.info("Loading all approval list info: << total " + approvalList.size());
        return approvalList;
    }

    @RequestMapping(value = "approval_details/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map updateApproval(@RequestBody Map<String, Object> approvalObj,HttpServletRequest request) throws ParseException {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("Updating approval list: >>");
        objList = approvalStatusService.update(approvalObj,request);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("approval.update.success.msg"));
            successMsg = environment.getProperty("approval.update.success.msg");
        }
        logger.info("Updating approval list:  << " + successMsg + validationError);
        return objList;
    }


    @RequestMapping(value = "approval_details/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Map deleteCategory(@RequestBody Map<String, Object> approvalObj,HttpServletRequest request) throws ParseException {
        HashMap serverResponse = new HashMap();
        String successMsg = "";
        String validationError = "";
        logger.info("Delete approval list:  >> ");
        Integer approvedId = (Integer) approvalObj.get("id");
        Integer ChangeRequestId = (Integer) approvalObj.get("requestId");
        long id=approvedId.longValue();
        long requestId=ChangeRequestId.longValue();
        validationError = approvalStatusService.delete(requestId,id,request);
        if (validationError.length() == 0) successMsg = environment.getProperty("approval.delete.success.msg");
        logger.info("Delete approval list:  << " + successMsg + validationError);
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        return serverResponse;
    }



}
