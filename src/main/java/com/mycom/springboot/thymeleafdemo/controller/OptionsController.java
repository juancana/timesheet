package com.mycom.springboot.thymeleafdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/timesheet/options")
public class OptionsController {
	
	@GetMapping("/mainMenu")
	public String options() {
		
		
		return "options/main-menu";
	}
}
