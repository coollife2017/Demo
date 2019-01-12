package com.njust.ycc.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class UserController {

	@RequestMapping(value = "/login1")
	public String login() {
	   return "login.html";
	}
	
	@ResponseBody
	@RequestMapping(value = "/home")
	public String home() {
		Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
		User user =  (User)authentication.getPrincipal();
	   return "welcome " + user.getUsername();
	}
	
	@ResponseBody
	@RequestMapping(value = "/accessDenied")
	public String accessDenied() {
	   return "accessDenied";
	}
	
	
}
