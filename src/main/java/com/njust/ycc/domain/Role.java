package com.njust.ycc.domain;

public class Role {
	// 角色id
	public Long id;
	// 角色权限
	public String authority;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getAuthority() {
		return authority;
	}
	public void setAuthority(String authority) {
		this.authority = authority;
	}
	

}
