package com.ytxiang.security;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ytxiang.model.User;
import com.ytxiang.dao.UserDAOImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    protected UserDAOImpl userDao;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	User user = userDao.getUserByUserName(username);

	if (user == null)
	    throw new UsernameNotFoundException("no user");

	return new org.springframework.security.core.userdetails.User(username,
			passwordEncoder.encode(user.getPassword()),
			true, true, true, true, grantedAuthorities());
    }

    Collection<GrantedAuthority> grantedAuthorities(){
	Collection<GrantedAuthority> authorities=new HashSet<>();

	authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
	return authorities;
    }

}
