package com.njust.ycc.serviceImp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.njust.ycc.domain.Role;
import com.njust.ycc.domain.CustomUser;

/**
 * 实现 UserDetailService接口，登录的时候Spring Security 会 调用UserDetailSevice的loadUserByUsername
 * 来加载用户信息，传入的参数是loginname登录用户名字
 * @author YuChenChen
 *
 */
@Service
public class UserServiceImp implements UserDetailsService {

	/**
	 * 从数据库加载用户信息
	 */
	@Override
	public UserDetails loadUserByUsername(String loginname) throws UsernameNotFoundException {
	    // 模拟从数据库获取用户信息
		CustomUser user = new CustomUser();
		user.setUsername("yuchenchen");
		user.setPassword(new BCryptPasswordEncoder().encode("123"));
		List<Role> roles = new ArrayList<>();
		Role role = new Role();
		role.setId(10000L);
		role.setAuthority("ROLE_USER");
		roles.add(role);
		user.setRoles(roles);
		
		// 将自定义的角色转换为security user 对象需要的grantedAuthority
		List<GrantedAuthority> authorities = new ArrayList<>();
		for(Role tempRole : roles) {
			authorities.add(new SimpleGrantedAuthority(tempRole.getAuthority()));
		}
		//构建security的User对象
        return new User(user.getUsername(), user.getPassword(), authorities);
	}

}
