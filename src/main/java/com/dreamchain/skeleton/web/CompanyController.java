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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
//@RequestMapping(CompanyController.URL)
@PropertySource("classpath:config.properties")
public class CompanyController {


    static final String URL = "/company";
    @Autowired
    CompanyService companyService;
    @Autowired
    Environment environment;

     static final Logger logger =
            LoggerFactory.getLogger(CompanyController.class.getName());

    @RequestMapping("/company")
    public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, TransformerException, ParserConfigurationException {
        ModelAndView model = new ModelAndView();
        String pageName = "company_test";
        model.setViewName(pageName);
        return model;

    }

    @RequestMapping(value = "/companyList1", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Company> loadCompanyList(HttpSession httpSession) {

        List<Company> companyList=new ArrayList();
        logger.info("Loading all company info: >> ");
        companyList = companyService.findAll();
        logger.info("Loading all company info: << total " + companyList.size());
        return companyList;
    }



    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    Map saveUser(@RequestBody String name,HttpSession httpSession) throws Exception {
        String successMsg = "";
        String validationError="";
        String invalidUserError="";
        logger.info("creating new company: >>");
        validationError = companyService.save(name);
        if (validationError.length() == 0) successMsg = environment.getProperty("company.save.success.msg");
        logger.info("creating new company: << " + successMsg + invalidUserError + invalidUserError);
        return createServerResponse(successMsg,validationError,invalidUserError,null);
    }


    private Map createServerResponse(String successMsg,String validationError,String invalidUserError,Company company){
        HashMap serverResponse = new HashMap();
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        serverResponse.put("invalidUserError", invalidUserError);
        serverResponse.put("company",company);
        return serverResponse;

    }

    @RequestMapping(value = "/companyList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Company> loadCompanyList1(HttpSession httpSession) {

        logger.info("Loading all company info: >> ");

        List companyList = new ArrayList();

        Company company=new Company();
        company.setId(1);
        company.setName("Paragon Poultry Limited");


        Company company1=new Company();
        company1.setId(2);
        company1.setName("Paragon Poultry-1 Limited");


        Company company2=new Company();
        company2.setId(3);
        company2.setName("Paragon Poultry-2 Limited");

        Company company3=new Company();
        company3.setId(4);
        company3.setName("Paragon Poultry-3 Limited");

        companyList.add(company);
        companyList.add(company1);
        companyList.add(company2);
        companyList.add(company3);


//        boolean isLoggedUserInvalid = checkLoggedInUserExistence(httpSession);
//        if (!isLoggedUserInvalid)
//            userList = userService.findAll(email);
        logger.info("Loading all company info: << total " + companyList.size());
        return companyList;
    }
}
