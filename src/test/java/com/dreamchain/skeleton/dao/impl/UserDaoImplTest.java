package com.dreamchain.skeleton.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;

import com.dreamchain.skeleton.model.User;

@Transactional
public class UserDaoImplTest extends DaoTest {
	
	@Autowired UserDaoImpl userDaoImpl;
	
	@Test
	public void all_fields_are_persisted() {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
//		User user = new User("Name1", "babu1234", true, true, true, true, grantedAuthorities, "Name1", "Babu@do.com", "ROLE_USER", "99876543211", null,"","test","test", null, null, null);
//
//		userDaoImpl.save(user);
		User newUser=userDaoImpl.findByUserName("Babu@do.com");


		Assert.assertEquals("Name1", newUser.getName());
		Assert.assertEquals("Babu@do.com", newUser.getEmail());
		Assert.assertEquals("ROLE_USER", newUser.getRole());
		Assert.assertEquals("99876543211", newUser.getPhone());

		User obj=newUser;
		obj.setName("Name2");
		obj.setEmail("Babu1@do.com");

		userDaoImpl.remove(newUser);
		userDaoImpl.update(obj);

		newUser=userDaoImpl.findByUserName("Babu1@do.com");

		Assert.assertEquals("Name2", newUser.getName());
	}


	@Test
	public void loadUserLIst(){
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
//		User user = new User("Name1", "babu1234", true, true, true, true, grantedAuthorities, "Name1", "Babu@do.com", "ROLE_USER", "99876543211",null, "","test","test", null, null, null);
//		userDaoImpl.save(user);
		List<User> lstUser=userDaoImpl.findAll("tt");
		Assert.assertNotNull(lstUser);
	}

}
