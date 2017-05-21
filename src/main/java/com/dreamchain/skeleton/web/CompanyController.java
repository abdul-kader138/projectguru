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
import java.text.ParseException;
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

    @RequestMapping(value = "/companyList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Company> loadCompanyList(HttpSession httpSession) {

        List<Company> companyList = new ArrayList();
        logger.info("Loading all company info: >> ");
        companyList = companyService.findAll();
        logger.info("Loading all company info: << total " + companyList.size());
        return companyList;
    }


    @RequestMapping(value = "/save", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    Map saveCompany(@RequestBody Map<String, String> companyInfo, HttpSession httpSession) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        String invalidUserError = "";
        Company company = new Company();
        logger.info("creating new company: >>");
        objList = companyService.save(companyInfo.get("name"), companyInfo.get("address"));
        validationError = (String) objList.get("validationMsg");
        if (validationError.length() == 0) {
            company = (Company) objList.get("company");
            successMsg = environment.getProperty("company.save.success.msg");
        }
        logger.info("creating new company: << " + successMsg + invalidUserError + invalidUserError);
        return createServerResponse(successMsg, validationError, invalidUserError, company);
    }


    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Map deleteCompany(@RequestBody String id, HttpSession httpSession) throws ParseException {
        String successMsg = "";
        String validationError = "";
        String invalidUserError = "";
        logger.info("Delete Company:  >> ");
        validationError = companyService.delete(Long.parseLong(id));
        if (validationError.length() == 0) successMsg = environment.getProperty("company.delete.success.msg");
        logger.info("Delete Company:  << " + successMsg + validationError);
        return createServerResponse(successMsg, validationError, invalidUserError, null);
    }


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map updateCompany(@RequestBody Company companyObj, HttpSession httpSession) throws ParseException {
        String successMsg = "";
        String validationError = "";
        String invalidUserError = "";
        Map<String, Object> objList = new HashMap<>();
        Company company = new Company();
        logger.info("Updating Company: >>");
        objList = companyService.update(companyObj);
        validationError = (String) objList.get("validationMsg");
        if (validationError.length() == 0) successMsg = environment.getProperty("company.update.success.msg");
        company = (Company) objList.get("company");
        logger.info("Updating Company:  << " + successMsg + validationError + invalidUserError);
        return createServerResponse(successMsg, validationError, invalidUserError, company);
    }


    private Map createServerResponse(String successMsg, String validationError, String invalidUserError, Company company) {
        HashMap serverResponse = new HashMap();
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        serverResponse.put("invalidUserError", invalidUserError);
        serverResponse.put("company", company);
        return serverResponse;

    }

    @RequestMapping(value = "/companyList1", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Company> loadCompanyList1(HttpSession httpSession) {

        logger.info("Loading all company info: >> ");

        List companyList = new ArrayList();

        Company company = new Company();
        company.setId(1);
        company.setName("Paragon Poultry Limited");


        Company company1 = new Company();
        company1.setId(2);
        company1.setName("Paragon Poultry-1 Limited");


        Company company2 = new Company();
        company2.setId(3);
        company2.setName("Paragon Poultry-2 Limited");

        Company company3 = new Company();
        company3.setId(4);
        company3.setName("Paragon Poultry-3 Limited");

        Company company4 = new Company();
        company4.setId(4);
        company4.setName("Paragon Poultry-4 Limited");

        Company company5 = new Company();
        company5.setId(4);
        company5.setName("Paragon Poultry-5 Limited");

        Company company6 = new Company();
        company6.setId(4);
        company6.setName("Paragon Poultry-6 Limited");

        Company company7 = new Company();
        company7.setId(4);
        company7.setName("Paragon Poultry-7 Limited");

        Company company8 = new Company();
        company8.setId(4);
        company8.setName("Paragon Poultry-8 Limited");

        Company company9 = new Company();
        company9.setId(4);
        company9.setName("Paragon Poultry-9 Limited");

        Company company10 = new Company();
        company10.setId(4);
        company10.setName("Paragon Poultry-3 Limited");

        companyList.add(company);
        companyList.add(company1);
        companyList.add(company2);
        companyList.add(company4);
        companyList.add(company5);
        companyList.add(company6);
        companyList.add(company7);
        companyList.add(company8);
        companyList.add(company9);
        companyList.add(company10);


//        boolean isLoggedUserInvalid = checkLoggedInUserExistence(httpSession);
//        if (!isLoggedUserInvalid)
//            userList = userService.findAll(email);
        logger.info("Loading all company info: << total " + companyList.size());
        return companyList;
    }
}
