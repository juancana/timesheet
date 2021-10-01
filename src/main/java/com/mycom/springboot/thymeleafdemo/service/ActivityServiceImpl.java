package com.mycom.springboot.thymeleafdemo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycom.springboot.thymeleafdemo.dao.ActivityRepository;
import com.mycom.springboot.thymeleafdemo.entity.Activity;

@Service
public class ActivityServiceImpl implements ActivityService {

	private ActivityRepository activityRepository;
	
	@Autowired
	public ActivityServiceImpl(ActivityRepository repository) {
		activityRepository = repository;
	}
	
	@Override
	public List<Activity> findAll() {
		List<Activity> activities = activityRepository.findAll();
		return activities;
	}

	@Override
	public Activity findById(int theId) {
		Optional<Activity> result = activityRepository.findById(theId);
		
		Activity theActivity = null;
		if(result.isPresent()) {
			theActivity = result.get();
		}else {
			//Didn't find the employee
			throw new RuntimeException("Did not find time-frame with id: " + theId);
		}
		
		return theActivity;
	}

}
