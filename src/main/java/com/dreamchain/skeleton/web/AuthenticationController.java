package com.dreamchain.skeleton.web;

import org.apache.commons.logging.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.*;

import javax.management.modelmbean.ModelMBean;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

@Controller
public class AuthenticationController {

//	private Log


    @RequestMapping("/login")
    public ModelAndView login(@RequestParam(value = "failed", required = false) String failed,
                              @RequestParam(value = "error", required = false) String error,
                              @RequestParam(value = "logout", required = false) String logout) {

        ModelAndView model = new ModelAndView();
        String pageName = "main";
        if (error != null) {
            model.addObject("error", "Invalid username or password!");
        }
        return model;

    }

    @RequestMapping("/home")
    public ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException, TransformerException, ParserConfigurationException {
        ModelAndView model = new ModelAndView();
        String pageName = "main";
        String userName = "";
//		for(Cookie cookie:req.getCookies()){
//			if(cookie.getName().equals("userName")){
//				userName=cookie.getValue();
//				break;
//			}
//		}
//		if(userName.length()==0){
//			Cookie cookies =new Cookie("userName",userName);
//			resp.addCookie(cookies);
//		}

        createXmlFile(request);
//		readXmlFile(request);
        model.setViewName(pageName);
        return model;

    }

    private void createXmlFile(HttpServletRequest request) throws ParserConfigurationException, TransformerException, FileNotFoundException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
        Document doc = documentBuilder.newDocument();

        Element root = doc.createElement("student.information");
        Element parentElement = doc.createElement("students");
        Element studentElement = doc.createElement("student");
        Element name = doc.createElement("name");
        Element email = doc.createElement("email");
        Element age = doc.createElement("age");


        Text nameText = doc.createTextNode("Abdul Kader");
        Text ageText = doc.createTextNode("30");
        Text emailText = doc.createTextNode("babu@gmail.com");

        name.appendChild(nameText);
        age.appendChild(ageText);
        email.appendChild(emailText);


        studentElement.appendChild(name);
        studentElement.appendChild(age);
        studentElement.appendChild(email);


        parentElement.appendChild(studentElement);


        root.appendChild(parentElement);

        doc.appendChild(root);

        String path = request.getSession().getServletContext().getRealPath("/WEB-INF/");
        System.out.println(path);
        String mainPath = path + "\\studentInfo.xml";
        File file = new File(mainPath);
        if (file.exists()) {


            Transformer transform = TransformerFactory.newInstance().newTransformer();
            transform.transform(new DOMSource(doc), new StreamResult(file));

        }
//
    }


    private void readXmlFile(HttpServletRequest request) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            String path = null;
            try {
                path = request.getSession().getServletContext().getRealPath("/WEB-INF/");
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(path);
            String mainPath = path + "\\studentInfo.xml";
            File file = new File(mainPath);
            if (file.exists()) {
                Document doc = db.parse(file);
                Element docEle = doc.getDocumentElement();

                // Print root element of the document
                System.out.println("Root element of the document: "
                        + docEle.getNodeName());

                NodeList studentList = docEle.getElementsByTagName("student");

                // Print total student elements in document
                System.out
                        .println("Total students: " + studentList.getLength());

                if (studentList != null && studentList.getLength() > 0) {
                    for (int i = 0; i < studentList.getLength(); i++) {

                        Node node = studentList.item(i);

                        if (node.getNodeType() == Node.ELEMENT_NODE) {

                            System.out
                                    .println("=====================");

                            Element e = (Element) node;
                            NodeList nodeList = e.getElementsByTagName("name");
                            System.out.println("Name: "
                                    + nodeList.item(0).getChildNodes().item(0)
                                    .getNodeValue());

                            nodeList = e.getElementsByTagName("grade");
                            System.out.println("Grade: "
                                    + nodeList.item(0).getChildNodes().item(0)
                                    .getNodeValue());

                            nodeList = e.getElementsByTagName("age");
                            System.out.println("Age: "
                                    + nodeList.item(0).getChildNodes().item(0)
                                    .getNodeValue());
                        }
                    }
                } else {
                    System.exit(1);
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}

