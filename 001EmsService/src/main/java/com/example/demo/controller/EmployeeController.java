package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emps")
public class EmployeeController {
	@Value(value = "${test.port}")
	private String port;
	@Value(value = "${test.user}")
	private String user;

	@GetMapping("/info")
	public String getInfo() {
		return "Employee API : Port :: " + port + " Username :: " + user;
	}

}
