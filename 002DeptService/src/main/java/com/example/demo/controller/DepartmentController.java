package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/api/depts")
public class DepartmentController {
	@Value(value = "${test.port}")
	private String port;
	
	@GetMapping("/info")
	public String getInfo() {
		return "Department API "+port;
	}
}
