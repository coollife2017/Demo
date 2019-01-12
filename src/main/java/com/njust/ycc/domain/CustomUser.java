package com.njust.ycc.domain;

import java.util.List;

public class CustomUser {
	// 用户名
	public String username;
	// 密码
	public String password;
	// 角色
	public List<Role> Roles;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public List<Role> getRoles() {
		return Roles;
	}
	public void setRoles(List<Role> roles) {
		Roles = roles;
	}

	
}
