package com.mycom.springboot.thymeleafdemo.dao;

import java.util.Calendar;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycom.springboot.thymeleafdemo.entity.Employee;
import com.mycom.springboot.thymeleafdemo.entity.TimeFrame;

public interface TimeRepository extends JpaRepository<TimeFrame, Integer> {
	/*														^		  ^
	 * 														|		  |
	 * 											Entity type-|		  |
	 * 																  |
	 * 													Primary key---|
	 * 
	 * 	That's it! No need to write any code. This gives all basic CRUD methods
	 */

	public List<TimeFrame> findByEmployee(Employee theEmployee); 
	
	public List<TimeFrame> findByStartTimeBetween (Calendar firstDay, Calendar lastDay);

	public List<TimeFrame> findByEmployeeAndStartTimeBetween(Employee employee, Calendar startTime, Calendar endTime);	

	public List<TimeFrame> findByEmployeeAndEndTimeBetween(Employee employee, Calendar startTime, Calendar endTime);
	
	public List<TimeFrame> findByEmployeeAndStartTimeBetweenOrderByStartTime(Employee employee, Calendar startTime, Calendar endTime);
	
	public List<Employee> findEmployeeByStartTimeBetween(Calendar startTime,Calendar endTime);
	
 
}
