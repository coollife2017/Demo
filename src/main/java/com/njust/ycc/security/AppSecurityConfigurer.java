package com.njust.ycc.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

/**
 * 自定义 Spring Security 认证处理类
 * @author YuChenChen
 *
 */
@Configuration
@EnableWebSecurity
public class AppSecurityConfigurer extends WebSecurityConfigurerAdapter {
	
	// 密码接口, 注意声明在authenticationProvider前面
	@Autowired
	private PasswordEncoder passwordEncoder;
	// 用户服务接口
	@Autowired
	private UserDetailsService userDetailsService;
    // 用户认证接口
	@Autowired
	private AuthenticationProvider authenticationProvider;	
	// 验证成功处理器
	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;
		
	/**
	 * 使用Spring Security 自带的密码编辑器
	 * @return
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/**
	 * 使用Spring Security自带的DaoAuthenticationProvider
	 * @return
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		//不要隐藏“用户未找到异常”
		authenticationProvider.setHideUserNotFoundExceptions(false);
		// 设置用户服务接口
		authenticationProvider.setUserDetailsService(userDetailsService);
		// 设置密码接口
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		return authenticationProvider;
	}
	
	/**
	 * 使用Spring Security 自带的SimpleUrlAuthenticationSuccessHandler，并重写handle方法
	 * @return
	 */
	@Bean
	public AuthenticationSuccessHandler authenticationSuccessHandler() {
		return new SimpleUrlAuthenticationSuccessHandler() {
			private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
			@Override
			protected void handle(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				// 根据角色权限获取目标路径
				String targetUrl = determinTargetUrl(authentication);
				// 重定向到目标路径
				redirectStrategy.sendRedirect(request, response, targetUrl);
			}
			
			/**
			 * 根据用户角色决定目标路径
			 * @param authentication
			 * @return
			 */
			private String determinTargetUrl(Authentication authentication) {
			    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
				List<String> roleNameList = new ArrayList<>();
				for(GrantedAuthority grantedAuthority : authorities) {
					roleNameList.add(grantedAuthority.getAuthority());
				}
				if(roleNameList.contains("ROLE_USER")) {
					 return "/home";
				}
				// 拒绝访问
			    return "/accessDenied";
			}
		};
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
		.antMatchers("/login1", "/css/**", "/js/**", "/img/**").permitAll()
		.antMatchers("/", "/home").hasRole("USER")  //角色Spring Security 会自动加上前缀ROLE_
		.anyRequest().authenticated()//所有请求要认证
		.and()
		.formLogin().loginPage("/login1")
		.successHandler(authenticationSuccessHandler)
		.usernameParameter("loginname").passwordParameter("password")
		.and()
		.logout().permitAll().invalidateHttpSession(true)
		.deleteCookies("JSESSIONID")
		.and()
		.exceptionHandling().accessDeniedPage("/accessDenied");
		http.csrf().disable();
	}
}
