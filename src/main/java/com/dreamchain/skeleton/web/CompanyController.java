package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.Company;
import com.dreamchain.skeleton.service.CompanyService;
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

@Controller
@PropertySource("classpath:config.properties")
public class CompanyController {


    @Autowired
    CompanyService companyService;
    @Autowired
    Environment environment;

    static final Logger logger =
            LoggerFactory.getLogger(CompanyController.class.getName());

    @RequestMapping("/company")
    public ModelAndView main()  {
        ModelAndView model = new ModelAndView();
        String pageName = "company_test";
        model.setViewName(pageName);
        return model;

    }

    @RequestMapping(value = "company/companyList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Company> loadCompanyList() {

        List<Company> companyList = new ArrayList();
        logger.info("Loading all company info: >> ");
        companyList = companyService.findAll();
        logger.info("Loading all company info: << total " + companyList.size());
        return companyList;
    }


    @RequestMapping(value = "company/save", method = RequestMethod.POST, produces = {"application/json"})
    public @ResponseBody Map saveCompany(MultipartHttpServletRequest request) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new company: >>");
        objList = companyService.save(request);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("company.save.success.msg"));
            successMsg = environment.getProperty("company.save.success.msg");
        }
        logger.info("creating new company: << " + successMsg + validationError);
        return objList;
    }


    @RequestMapping(value = "company/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Map deleteCompany(@RequestBody String companyId,HttpServletRequest request) throws ParseException {
        HashMap serverResponse = new HashMap();
        String successMsg = "";
        String validationError = "";
        logger.info("Delete Company:  >> ");
        validationError = companyService.delete(Long.parseLong(companyId), request);
        if (validationError.length() == 0) successMsg = environment.getProperty("company.delete.success.msg");
        logger.info("Delete Company:  << " + successMsg + validationError);
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        return serverResponse;
    }


    @RequestMapping(value = "company/update", method = RequestMethod.POST)
    public @ResponseBody Map updateCompany(MultipartHttpServletRequest request) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("Updating Company: >>");
        objList = companyService.update(request);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("company.update.success.msg"));
            successMsg = environment.getProperty("company.update.success.msg");
        }
        logger.info("Updating Company:: << " + successMsg + validationError);
        return objList;
    }



}