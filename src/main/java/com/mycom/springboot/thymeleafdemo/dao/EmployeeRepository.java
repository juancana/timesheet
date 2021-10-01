package com.mycom.springboot.thymeleafdemo.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycom.springboot.thymeleafdemo.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
/*															^		  ^
 * 															|		  |
 * 												Entity type-|		  |
 * 																	  |
 * 														Primary key---|
 * 
 * 	That's it! No need to write any code. This gives all basic CRUD methods
 */
	
	public List<Employee> findAllByOrderByLastNameAsc();  //Query methods
	
}
