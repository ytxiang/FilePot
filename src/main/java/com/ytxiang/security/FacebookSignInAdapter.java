package com.ytxiang.security;

import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class FacebookSignInAdapter implements SignInAdapter {

    private final UserDetailsService userDetailsService;

    public FacebookSignInAdapter(UserDetailsService userDetailsService) {
	this.userDetailsService = userDetailsService;
    }

    @Override
    public String signIn(String userId, Connection<?> connection, NativeWebRequest request) {
	final UserDetails user = userDetailsService.loadUserByUsername(userId);

	Authentication token = new UsernamePasswordAuthenticationToken(user, null, Arrays.asList(new SimpleGrantedAuthority("FACEBOOK_USER")));

        SecurityContextHolder.getContext().setAuthentication(token);
	request.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
	    SecurityContextHolder.getContext(), NativeWebRequest.SCOPE_SESSION);

        return null;
    }
}
