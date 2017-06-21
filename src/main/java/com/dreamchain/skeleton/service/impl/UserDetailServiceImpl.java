package com.dreamchain.skeleton.service.impl;

import java.util.ArrayList;
import java.util.List;


import com.dreamchain.skeleton.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dreamchain.skeleton.dao.UserDao;

@Service("userDetailsService") 
public class UserDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserDao userDao;


	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.findByUserName(username);
		if (user == null) {
			throw new UsernameNotFoundException("Not able to find user");
		}
		String name=user.getRole();
		List<GrantedAuthority> grantedAuthorities=new ArrayList<GrantedAuthority>();
		for (String userName: name.split(",")) {
			GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(userName);
			grantedAuthorities.add(grantedAuthority);
		}
		return new User(user.getName(),user.getPassword(),true,true,true,true,grantedAuthorities,
				user.getName(),user.getEmail(),user.getRole(),user.getPhone(),user.getImagePath(),
				user.getDesignation(),user.getRoleRightsId(),user.getRoleRight(),user.getCompanyId(),
				user.getCompany(),user.getUserType(),user.getClientId(),user.getCreatedBy(),user.getUpdatedBy(),
				user.getCreatedOn(),user.getUpdatedOn());
	}
	
}