package com.mycom.springboot.thymeleafdemo.controller;

import java.util.List;

import com.mycom.springboot.thymeleafdemo.entity.Employee;

public class EmployeeList {

	private List<Employee> list;
	
	public EmployeeList() {
		
	}
	
	

	public EmployeeList(List<Employee> list) {
		this.list = list;
	}



	public List<Employee> getList() {
		return list;
	}

	public void setList(List<Employee> list) {
		this.list = list;
	}
	
	
}
