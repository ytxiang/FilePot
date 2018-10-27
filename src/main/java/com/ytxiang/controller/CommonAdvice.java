package com.ytxiang.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.ytxiang.dto.UserDTO;
import com.ytxiang.service.FilepotService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class CommonAdvice {

	@Autowired
	FilepotService filepotService;

    @ExceptionHandler
    public void handleControllerException(HttpServletRequest request, HttpServletResponse response, Throwable ex) throws IOException {
	ex.printStackTrace();
	String ajax = request.getHeader("X-Requested-With");
	response.setCharacterEncoding("utf-8");
	if (StringUtils.isBlank(ajax)) {
	    response.sendRedirect("/error");
	} else {
	    response.getWriter().println("error:" + ex.getMessage());
	}

    }

    @ModelAttribute
    public void addCommonModel(Model model, HttpServletRequest request) {
	UserDTO user;
	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	if (auth == null || auth.getName().equals(""))
		return;
	user = filepotService.getUser(auth.getName());
	if (user != null) {
	    model.addAttribute("user", user);
	}
    }


}