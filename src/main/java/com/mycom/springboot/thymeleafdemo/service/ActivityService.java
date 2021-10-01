package com.mycom.springboot.thymeleafdemo.service;

import java.util.List;

import com.mycom.springboot.thymeleafdemo.entity.Activity;

public interface ActivityService {
	public List<Activity> findAll();
	
	public Activity findById(int theId);

}
