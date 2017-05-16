package com.dreamchain.skeleton.web;

import com.dreamchain.skeleton.aoptest.BeforeAop;
import com.dreamchain.skeleton.model.User;
import com.dreamchain.skeleton.service.UserService;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/secret")
public class SecretController {

	@Autowired
	UserService userService;

	@RequestMapping(value = "/info", method = RequestMethod.GET, headers="Accept=application/json")
	public @ResponseBody
	List<String> listAllUsers() {
		BeforeAop boa=new BeforeAop();
		ProxyFactoryBean pfb=new ProxyFactoryBean();
		pfb.setTarget(userService);
		pfb.addAdvice(boa);

		UserService userService1=(UserService)pfb.getObject();
//		User user=userService1.get(301l);
//		System.out.println(user.getName());


		List<String> lst=new ArrayList<String>();
//		User s=new User();
//		s.setName("Babu");
//		s.setEmail("BD");
//
//		User s1=new User();
//		s1.setName("Babu1");
//		s1.setEmail("BD1");

		lst.add("ggg");
//		lst.add(s1);

		return lst;
	}

}
