package com.example.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserBean {
	
	private String username;
	private String password;
	private String fname;
	private String lname;
	private String phno;
	private String email;
	private List<String> roles;

	
	

}
