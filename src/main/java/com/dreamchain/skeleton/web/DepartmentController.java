package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.model.Department;
import com.dreamchain.skeleton.service.DepartmentService;
import org.apache.commons.io.IOUtils;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.ParseException;
import java.util.*;

@Controller
@PropertySource("classpath:config.properties")
public class DepartmentController {

    @Autowired
    DepartmentService departmentService;
    @Autowired
    Environment environment;

    static final Logger logger =
            LoggerFactory.getLogger(DepartmentController.class.getName());

    @RequestMapping("/department")
    public ModelAndView main()  {
        ModelAndView model = new ModelAndView();
        String pageName = "department";
        model.setViewName(pageName);
        return model;

    }

    @RequestMapping(value = "department/departmentList", method = RequestMethod.GET)
    public
    @ResponseBody
    List<Department> loadDepartmentList() {

        List<Department> departmentList = new ArrayList();
        logger.info("Loading all department info: >> ");
        departmentList = departmentService.findAll();
        logger.info("Loading all department info: << total " + departmentList.size());
        return departmentList;
    }


    @RequestMapping(value = "department/save", method = RequestMethod.POST, consumes = "application/json", headers = "content-type=application/x-www-form-urlencoded")
    public
    @ResponseBody
    Map saveDepartment(@RequestBody Map<String, String> departmentInfo) throws Exception {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("creating new department: >>");
        objList = departmentService.save(departmentInfo.get("name"));
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("department.save.success.msg"));
            successMsg = environment.getProperty("department.save.success.msg");
        }
        logger.info("creating new department: << " + successMsg + validationError);
        return objList;
    }


    @RequestMapping(value = "department/delete", method = RequestMethod.POST)
    public
    @ResponseBody
    Map deleteDepartment(@RequestBody Map<String, String> departmentInfo) throws ParseException {
        HashMap serverResponse = new HashMap();
        String successMsg = "";
        String validationError = "";
        logger.info("department Company:  >> ");
        validationError = departmentService.delete(Long.parseLong(departmentInfo.get("id")));
        if (validationError.length() == 0) successMsg = environment.getProperty("department.delete.success.msg");
        logger.info("department Company:  << " + successMsg + validationError);
        serverResponse.put("successMsg", successMsg);
        serverResponse.put("validationError", validationError);
        return serverResponse;
    }


    @RequestMapping(value = "department/update", method = RequestMethod.POST)
    public
    @ResponseBody
    Map updateDepartment(@RequestBody Map<String, String> departmentObj) throws ParseException {
        Map<String, Object> objList = new HashMap<>();
        String successMsg = "";
        String validationError = "";
        logger.info("Updating department: >>");
        objList = departmentService.update(departmentObj);
        validationError = (String) objList.get("validationError");
        if (validationError.length() == 0) {
            objList.put("successMsg", environment.getProperty("department.update.success.msg"));
            successMsg = environment.getProperty("department.update.success.msg");
        }
        logger.info("Updating department:  << " + successMsg + validationError);
        return objList;
    }

    @RequestMapping(value = "department/echofile", method = RequestMethod.POST, produces = {"application/json"})
    public @ResponseBody HashMap<String, Object> echoFile(MultipartHttpServletRequest request,HttpServletRequest request1,
                                                          HttpServletResponse response) throws Exception {


        InputStream inputStream = null;
        OutputStream outputStream = null;

        MultipartFile multipartFile = request.getFile("file");
        String fileName = multipartFile.getOriginalFilename();
        Long size = multipartFile.getSize();



        try {
            String filepath = "/resources/" + "images/" +  "/babu/"
                     + fileName;
            String realPathFetch = request.getRealPath(
                    "/");
            inputStream = multipartFile.getInputStream();
            File newFile = new File(realPathFetch+filepath);
            if (!newFile.exists()) {
                newFile.createNewFile();
            }
            outputStream = new FileOutputStream(newFile);
            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("size", size);
        map.put("name", fileName);

        return map;

    }

}
