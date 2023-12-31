package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.UserEntity;
import com.example.model.LoginBean;
import com.example.model.UserBean;
import com.example.repo.UserRepository;
import com.example.service.UserService;
import com.example.util.JwtTokenUtil;

@RestController
@RequestMapping("/api/v1/auth/")	
public class AuthController {
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	 private  AuthenticationManager authenticationManager;
	
	@GetMapping("/test")
	public UserBean Test() {
		UserBean userBean = new UserBean("balaji", "balaji1218", "Balaji", "Goddu", "balajig8086@gmail.com", "998989098", List.of("USER","ADMIN"));
		return userBean;
	}

	@PostMapping("/reg")
	public String registration(@RequestBody UserBean userBean) {
		UserEntity user = UserEntity.builder().uname(userBean.getUsername()).password(passwordEncoder.encode(userBean.getPassword())).build();
		userRepository.saveAndFlush(user);
		return userBean.toString();
	}

	@PostMapping("/login")
	public String login(@RequestBody LoginBean loginBean) {
		
		//Authentication
		
	  authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginBean.getUname(), loginBean.getPassword()));
		
	  UserDetails userDetails =userService.userDetailsService().loadUserByUsername(loginBean.getUname());
		
		return jwtTokenUtil.generateToken(userDetails);
	}

}
