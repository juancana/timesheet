package com.mycom.springboot.thymeleafdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mycom.springboot.thymeleafdemo.entity.Employee;
import com.mycom.springboot.thymeleafdemo.service.EmployeeService;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

	private EmployeeService employeeService;
	
	@Autowired
	public EmployeeController(EmployeeService theEmployeeService) {
		employeeService = theEmployeeService;
	}
	
	//Add mapping for "/list"
	@GetMapping("/list")
	public String listEmployees(Model theModel) {
		
		//Get employees from db
		List<Employee> theEmployees = employeeService.findAll();
		
		//Add to spring model
		theModel.addAttribute("employees", theEmployees);
		
		return "employees/list-employees";
	}

	@GetMapping("/showFormForAdd")
	public String showFormForAdd(Model theModel) {
		
		//Create the model attribute to bind form data
		Employee theEmployee = new Employee();
		
		theModel.addAttribute("employee", theEmployee);
			
		return "employees/employee-form";
	}
	
	@GetMapping("/showForForUpdate")
	public String showForForUpdate(@RequestParam("employeeId") int theId,
									Model theModel) {
		
		//Get employee from the service
		Employee theEmployee = employeeService.findById(theId);
		
		//Set employee as a model attribute to pre-populate form
		theModel.addAttribute("employee", theEmployee);
		
		//Send over to our form		
		return "employees/employee-form";
	}

	
	
	@PostMapping("/save")
	public String saveEmployee(@ModelAttribute("employee") Employee theEmployee) {
		
		//Save the employee
		employeeService.save(theEmployee);
		
		//Use redirect to prevent duplicate submissions		
		return "redirect:/employees/list";
	}
	
	@GetMapping("/delete")
	public String delete(@RequestParam("employeeId") int theId) {
		
		//Delete the employee
		employeeService.deleteById(theId);
		
		//redirect to /employees/list
		
		return "redirect:/employees/list";
	}
	
	
}
