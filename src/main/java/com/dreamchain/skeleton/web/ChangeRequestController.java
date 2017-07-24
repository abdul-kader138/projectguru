package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.ApprovalStatus;
import com.dreamchain.skeleton.model.ChangeRequest;
import com.dreamchain.skeleton.service.ApprovalStatusService;
import com.dreamchain.skeleton.service.ChangeRequestService;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@PropertySource("classpath:config.properties")
public class ChangeRequestController {
    @Autowired
    ChangeRequestService changeRequestService;
    @Autowired
    Environment environment;
    @Autowired
    ApprovalStatusService approvalStatusService;

    static final Logger logger =
            LoggerFactory.getLogger(ChangeRequest.class.getName());

    @RequestMapping("/change_request")
    public ModelAndView main() {
        ModelAndView model = new ModelAndView();
        String pageName = "change_request";
        model.setViewName(pageName);
        return model;

    }

    @RequestMapping(value = "change_request/change_requestList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<ChangeRequest> loadChangeRequestList() {

        List<ChangeRequest> changeRequestList = new ArrayList();
        logger.info("Loading all change request info: >> ");
        changeRequestList = changeRequestService.findAll();
        logger.info("Loading all change request info: << total " + changeRequestList.size());
        return changeRequestList;
    }


    @RequestMapping(value = "change_request/save", method = RequestMethod.POST, produces = {"application/json"})
    public
    @ResponseBody
    Map saveCompany(MultipartHttpServletRequest request) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new change request: >>");
        objList = changeRequestService.save(request);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("request.save.success.msg"));
            successMsg = environment.getProperty("request.save.success.msg");
        }
        logger.info("creating new change request: << " + successMsg + validationError);
        return objList;
    }


    @RequestMapping(value = "/change_request_view")
    public ModelAndView main(@RequestParam("r_id") String requestId) {
        String pageName = "change_request_view";
        ModelAndView modelAndView = new ModelAndView(pageName);
        Long requestedId = Long.parseLong(requestId);
        ChangeRequest changeRequest = changeRequestService.get(requestedId);
        modelAndView.addObject("changeRequest", changeRequest);
        return modelAndView;

    }

    @RequestMapping(value = "/edit_request")
    public ModelAndView editRequestView(@RequestParam("a_id") String approvalId, @RequestParam("r_id") String requestId,
                                        @RequestParam("ver") String ver) {
        String pageName = "edit_request";
        ModelAndView modelAndView = new ModelAndView(pageName);
        Long approveId = Long.parseLong(approvalId);
        Long requestedId = Long.parseLong(requestId);
        Long version = Long.parseLong(ver);
        ChangeRequest changeRequest = changeRequestService.get(requestedId);
        ApprovalStatus approvalStatus = approvalStatusService.get(approveId);
        modelAndView.addObject("version", version);
        modelAndView.addObject("changeRequest", changeRequest);
        modelAndView.addObject("approvalStatus", approvalStatus);
        modelAndView.setViewName(pageName);
        return modelAndView;
    }

    @RequestMapping(value = "change_request/update", method = RequestMethod.POST, produces = {"application/json"})
    public
    @ResponseBody
    Map updateApproval(MultipartHttpServletRequest request) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("Updating change request: >>");
        objList = changeRequestService.update(request);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("request.update.success.msg"));
            successMsg = environment.getProperty("request.update.success.msg");
        }
        logger.info("Updating change request:  << " + successMsg + validationError);
        return objList;
    }

    @RequestMapping(value = "/change_request/pdf", method = RequestMethod.GET)
    public ModelAndView doSalesReportPDF(ModelAndView modelAndView)
    {
        logger.info("View pdf report for change request: >>");
        List<ChangeRequest> changeRequestList = changeRequestService.findAll();
        Map<String,Object> parameterMap = new HashMap<String,Object>();
        JRDataSource dataSource = new JRBeanCollectionDataSource(changeRequestList);
        parameterMap.put("datasource", dataSource);
        modelAndView = new ModelAndView("pdfReport", parameterMap);
        logger.info("View pdf report for change request: >>");
        return modelAndView;
    }

}
