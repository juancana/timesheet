package com.mycom.springboot.thymeleafdemo.service;

import java.util.Calendar;
import java.util.List;

import com.mycom.springboot.thymeleafdemo.entity.Employee;
import com.mycom.springboot.thymeleafdemo.entity.TimeFrame;

public interface TimeFrameService {
	public List<TimeFrame> findAll();
	
	public TimeFrame findById(int theId);
	
	public boolean save(TimeFrame theTimeFrame);
	
	public void deleteById(int theId);	
	
	public List<TimeFrame> findByEmployee(Employee theEmployee);

	public List<TimeFrame>  findBetweenTimes(Calendar startTime, Calendar endTime);
	 
	public List<TimeFrame>  findByDay(Calendar theDay);
	
	public List<Employee>  findEmployeesInADay(Calendar theDay);

	public List<Employee> findEmployeesInAWeek(int year,int week);

	public List<TimeFrame>  findByEmployeeAndDay(Employee employee, Calendar theDay);
	
	public List<TimeFrame> findByEmployeeAndWeek(Employee employee, int year, int week) ;
	
	public List<TimeFrame> findByEmployeeAndWeekOrderByStartTime(Employee employee, int year, int week) ;
	
	public List<TimeFrame>[] findByEmployeeAndWeekSeparateByDays(Employee employee, int year, int week) ;
	
	
}
