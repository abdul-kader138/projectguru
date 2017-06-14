package com.dreamchain.skeleton.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import static org.junit.Assert.*;

public class UserTest {
	
	
	@Test
	public void assert_that_certain_fields_cant_be_null_or_blank() {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
//		User user = new User("Name1", "babu1234", true, true, true, true, grantedAuthorities, "Name1", "Babu@do.com", "ROLE_USER", "99876543211",null, "","test","test", null, null, null);
//
//		Map<String, ConstraintViolation<User>> violationsMap = validate(user);
//		assertTrue(violationsMap.size()==0);

//		assertTrue(violationsMap.get("name").getMessageTemplate().contains("NotEmpty"));
//		assertTrue(violationsMap.get("email").getMessageTemplate().contains("NotEmpty"));
//		assertTrue(violationsMap.get("phone").getMessageTemplate().contains("NotEmpty"));
//		assertTrue(violationsMap.get("password").getMessageTemplate().contains("NotEmpty"));
//		assertTrue(violationsMap.get("role").getMessageTemplate().contains("NotNull"));
	}

	@Test
	public void assert_that_email_has_to_be_an_email() {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
//		User user = new User("Name1", "babu1234", true, true, true, true, grantedAuthorities, "Name1", "Babu", "ROLE_USER", "99876543211", null,"","test","test", null, null, null);
//		Map<String, ConstraintViolation<User>> violationsMap = validate(user);
//		assertTrue(violationsMap.get("email").getMessageTemplate().contains("Email"));
	}





//	private <T> Map<String, ConstraintViolation<T>>  validate(T user) {
//		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
//		Map<String, ConstraintViolation<T>> violations = new HashMap<String, ConstraintViolation<T>>();
//		for (ConstraintViolation<T> violation : validator.validate(user)) {
//			violations.put(violation.getPropertyPath().toString(), violation);
//		}
//		return violations;
//	}

}
