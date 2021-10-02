package com.mycom.springboot.thymeleafdemo.controller;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mycom.springboot.thymeleafdemo.entity.Activity;
import com.mycom.springboot.thymeleafdemo.entity.Employee;
import com.mycom.springboot.thymeleafdemo.entity.TimeFrame;
import com.mycom.springboot.thymeleafdemo.service.ActivityService;
import com.mycom.springboot.thymeleafdemo.service.EmployeeService;
import com.mycom.springboot.thymeleafdemo.service.TimeFrameService;

@Controller
@RequestMapping("/timesheet")
public class TimesheetsController {
	
	
	private TimeFrameService timeFrameService;
	private EmployeeService employeeService;
	private ActivityService activityService;
	
	@Autowired
	public TimesheetsController(EmployeeService theEmployeeService, TimeFrameService theTimeService, ActivityService theActivityService) {
		employeeService = theEmployeeService;
		timeFrameService = theTimeService;
		activityService = theActivityService;
	}
	
	@GetMapping("/")
	public String showMainPage(Model theModel) {
		
		Calendar calendar=Calendar.getInstance();
		
		int year = calendar.get(Calendar.YEAR);
		int week = calendar.get(Calendar.WEEK_OF_YEAR);
		
		
		return "redirect:/timesheet/" + year + "/" + week; 
	}
	
	@PostMapping("/updateWeekView")
	public String updateWeekView(@ModelAttribute("viewParameters") ViewParameters vp){
		
		System.out.println(">>> Updating view to year: " + vp.getYear() 
									+ ", week: " + vp.getWeek());
		
		return "redirect:/timesheet/" + vp.getYear() 
						+ "/" + vp.getWeek(); 
	}

	
	@PostMapping("/updateEmployeeWeekView")
	public String updateEmployeeWeekView(@ModelAttribute("viewParameters") ViewParameters vp){
		
		System.out.println(">>> Updating view to: year=" + vp.getYear() 
									+ ", week=" + vp.getWeek() 
									+ ", employeeId=" + vp.getEmployeeId());
		
		return "redirect:/timesheet/" + vp.getYear() 
						+ "/" + vp.getWeek() 
						+ "/" + vp.getEmployeeId();
	}
	
	@PostMapping("/updateEmployeeDayView")
	public String updateEmployeeDayView(@ModelAttribute("viewParameters") ViewParameters vp){
		
		System.out.println(">>> Updating view to: year=" + vp.getYear() 
									+ ", week=" + vp.getWeek() 
									+ ", employeeId=" + vp.getEmployeeId() 
									+ ", employeeId=" + vp.getDayOfWeek());
		
		return "redirect:/timesheet/" + vp.getYear() 
						+ "/" + vp.getWeek() 
						+ "/" + vp.getEmployeeId() 
						+ "/" + vp.getDayOfWeek(); 
	}
	
	@GetMapping("/{year}/{week}")
	public String listEmployeesInAWeek(@PathVariable int year, 
									   @PathVariable int week, 
									   @ModelAttribute("viewParameters") ViewParameters vp,
								       Model theModel) {
		//System.out.println(">>> Showing info relative to: Year " + year + ", Week: " + week);
		
		//Get employees from db
		List<Employee> theEmployees = timeFrameService.findEmployeesInAWeek(vp.getYear(),vp.getWeek());
		
		//Add to spring model
		theModel.addAttribute("employees", theEmployees);

		String[] temp = generateDatesOfWeek( year,  week);
		String displayMessage = temp[0].split(" ")[1] + " to " + temp[7].split(" ")[1];
		theModel.addAttribute("displayMessage", displayMessage);
		
		theModel.addAttribute("viewParameters", vp);
		
		
		return "timesheets/week-view";
	}
	
	@GetMapping("/{year}/{week}/{employeeId}")
	public String showWeekForEmployee(@PathVariable int employeeId, 
				  					  @ModelAttribute("viewParameters") ViewParameters vp,
			   						  Model theModel) {//System.out.println(">>> Searching week hours of Employee: " + theId + ", Year: " + theYear + ", Week: " + theWeek);
		
		//Get the employee object
		Employee employee = employeeService.findById(employeeId);
		
		//Get times for given employee and week from the service
		List<TimeFrame>[] theTimes = timeFrameService.findByEmployeeAndWeekSeparateByDays(employee, vp.getYear(), vp.getWeek());
		
		float[][] theStatistics = obtainWeekStatistics(theTimes);

		
		String[] theDays = generateDatesOfWeek(vp.getYear(),vp.getWeek()); //{"Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "TOTAL"};

		vp.setEmployeeId(employeeId);
		 
		List<Employee> activeEmployees = employeeService.findAll();
		
		theModel.addAttribute("employee", employee);
		theModel.addAttribute("statistics", theStatistics);	
		theModel.addAttribute("days", theDays);		
		theModel.addAttribute("viewParameters", vp);
		theModel.addAttribute("activeEmployees", activeEmployees);
		
		String[] temp = generateDatesOfWeek(vp.getYear(),  vp.getWeek());
		String displayMessage = temp[0].split(" ")[1] 
					 + " to " + temp[7].split(" ")[1]
					 + " - " + employee.getFirstName()
					 + " " + employee.getLastName();
		theModel.addAttribute("displayMessage", displayMessage);	
			
		return "timesheets/employee-week-view";
	}

	@GetMapping("/{year}/{week}/{employeeId}/{dayOfWeek}")
	public String showDayForEmployee(@PathVariable int dayOfWeek,
				  					 @ModelAttribute("viewParameters") ViewParameters vp,
			   						 Model theModel) {
		System.out.println(">>> Searching day details of Employee: " + vp.getEmployeeId() + ", Year: " + vp.getYear() + ", Week: " + vp.getWeek() + ", dayOfWeek: " + dayOfWeek);
		
		int theWeek = vp.getWeek();
		int theYear = vp.getYear();
		if (dayOfWeek < 1) {
			theWeek = vp.getWeek() -1;
		}
		if (theWeek < 1) {
			theYear = vp.getYear() -1;
			theWeek = theWeek + 52;
		}
		
		//Get the employee object
		Employee employee = employeeService.findById(vp.getEmployeeId());
		
		Calendar theDay = Calendar.getInstance();
		theDay.set(Calendar.YEAR, theYear);
		theDay.set(Calendar.WEEK_OF_YEAR, theWeek);
		theDay.set(Calendar.DAY_OF_WEEK, dayOfWeek);
		
		//Get times for given employee and week from the service
		List<TimeFrame>  theTimes = timeFrameService.findByEmployeeAndDay(employee, theDay);
		
		float[] theStatistics = obtainDayStatistics(theTimes);

		String[] theDays = {"Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "TOTAL"};

		vp.setDayOfWeek(dayOfWeek);
		 
		List<Employee> activeEmployees = employeeService.findAll();
		
		int[] daysOfWeek = {-1, 0, 1, 2, 3, 4, 5, 6};
		
		
		theModel.addAttribute("employee", employee);
		theModel.addAttribute("statistics", theStatistics);	
		theModel.addAttribute("days", theDays);		
		theModel.addAttribute("viewParameters", vp);
		theModel.addAttribute("activeEmployees", activeEmployees);			
		theModel.addAttribute("daysOfWeek", daysOfWeek);
		theModel.addAttribute("theTimes", theTimes);	
				
		String[] temp = generateDatesOfWeek(vp.getYear(),  vp.getWeek());
		String displayMessage = temp[vp.getDayOfWeek() + 1]
					 + " - " + employee.getFirstName()
					 + " " + employee.getLastName();
		theModel.addAttribute("displayMessage", displayMessage);
		
		return "timesheets/employee-day-view";
	}

	
	
	@GetMapping("/addActivity")
	public String addActivity(@ModelAttribute("viewParameters") ViewParameters vp,
			 				  Model theModel){
		
		TimeFrame theTimeFrame = new TimeFrame();
		Employee theEmployee = employeeService.findById(vp.getEmployeeId());

		int theDayOfWeek = vp.getDayOfWeek();
		int theWeek = vp.getWeek();
		int theYear = vp.getYear();
		if (vp.getDayOfWeek() < 1) {
			theDayOfWeek = theDayOfWeek + 7;
			theWeek = vp.getWeek() -1;
		}
		if (theWeek < 1) {
			theYear = vp.getYear() -1;
			theWeek = theWeek + 52;
		}
		
		//Set default values for form
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, theYear);
		calendar.set(Calendar.WEEK_OF_YEAR, theWeek);
		calendar.set(Calendar.DAY_OF_WEEK, theDayOfWeek);
		calendar.set(Calendar.HOUR_OF_DAY, 7);
		calendar.set(Calendar.MINUTE, 0);
		theTimeFrame.setStartTime(calendar);
		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(calendar.getTime());
		calendar2.set(Calendar.HOUR_OF_DAY, 15);
		calendar2.set(Calendar.MINUTE, 0);
		theTimeFrame.setEndTime(calendar2);
		
		theTimeFrame.setEmployee(theEmployee);
		
		theTimeFrame.setActivity(activityService.findById(1));
		List<Activity> possibleActivities = activityService.findAll();
		
		//System.out.println("possibleActivities" + possibleActivities);
		
		
		TimeParser timeParser = new TimeParser(theTimeFrame);	

		theModel.addAttribute("timesParsed", timeParser);
		theModel.addAttribute("timeFrame", theTimeFrame);
		theModel.addAttribute("activities", possibleActivities);
		theModel.addAttribute("theEmployee", theEmployee);
		theModel.addAttribute("viewParameters", vp);
		theModel.addAttribute("activ", theTimeFrame.getActivity());
		
		String[] temp = generateDatesOfWeek(vp.getYear(),  vp.getWeek());
		String displayMessage = "Creating new acivity on "
					 + temp[vp.getDayOfWeek() + 1].split(" ")[1]
					 + "/" + vp.getYear()
					 + " for " + theEmployee.getFirstName()
					 + " " + theEmployee.getLastName();
		theModel.addAttribute("displayMessage", displayMessage);
		
		//For debugging
		//System.out.println(">>> Update Activity with: " + vp.toString());
		//System.out.println(">>> Created time frame: " + theTimeFrame.toString());
		
		return "timesheets/activity-form";
	}
	
	@GetMapping("/updateActivity")
	public String updateActivity(@RequestParam("timeFrameId") int theId,
			 					 //@RequestParam("viewWeek") int viewWeek,
			 					 @ModelAttribute("viewParameters") ViewParameters vp,
								 Model theModel){
		
		
		//Get time frame from the service
		TimeFrame theTimeFrame = timeFrameService.findById(theId);
		Employee theEmployee = theTimeFrame.getEmployee();
		
		int theYear= theTimeFrame.getStartTime().get(Calendar.YEAR);
		int dayOfWeek = theTimeFrame.getStartTime().get(Calendar.DAY_OF_WEEK);
		
		if(dayOfWeek > 6) {
			dayOfWeek = dayOfWeek -7;
		}
			
		//For debugging
		System.out.println(">>> Update Activity with: " + vp.toString());		
		
		TimeParser timeParser = new TimeParser(theTimeFrame);
		List<Activity> possibleActivities = activityService.findAll();
		
		Activity activ = theTimeFrame.getActivity();
		
		//Set model attributes to pre-populate form
		theModel.addAttribute("timesParsed", timeParser);
		theModel.addAttribute("timeFrame", theTimeFrame);
		theModel.addAttribute("activities", possibleActivities);
		theModel.addAttribute("theEmployee", theEmployee);
		theModel.addAttribute("viewParameters", vp);
		theModel.addAttribute("activ", activ.getActId());
		
		String[] temp = generateDatesOfWeek(vp.getYear(),  vp.getWeek());
		String displayMessage = "Updating acivity on "
					 + temp[vp.getDayOfWeek() + 1].split(" ")[1]
					 + "/" + vp.getYear()
					 + " for " + theEmployee.getFirstName()
					 + " " + theEmployee.getLastName();
		theModel.addAttribute("displayMessage", displayMessage);
		
		return "timesheets/activity-form";
	}
	
	@PostMapping("/save")
	public String saveTimeFrame(@ModelAttribute("timeFrameId") int timeFrameId,
								@ModelAttribute("timesParsed") TimeParser timeParser,
								@ModelAttribute("viewParameters") ViewParameters vp,
								@ModelAttribute("activ") int activity,								
								Model theModel) {
		 
		TimeFrame theTimeFrame = new TimeFrame();
		if (timeFrameId ==0) {
			//Have to initialize the TimeFrame object since I can't get from the Model due to the Calendar objects inside it
			Calendar cal = Calendar.getInstance();
			Calendar cal2 = Calendar.getInstance();
			int theDayOfWeek = vp.getDayOfWeek();
			int theWeek = vp.getWeek();
			int theYear = vp.getYear();
			
			if (vp.getDayOfWeek() < 1) {
				theDayOfWeek = theDayOfWeek + 7;
				theWeek = vp.getWeek() -1;
			}
			if (theWeek < 1) {
				theYear = vp.getYear() -1;
				theWeek = theWeek + 52;
			}
			cal.set(Calendar.YEAR, theYear);
			cal.set(Calendar.WEEK_OF_YEAR, theWeek);
			cal.set(Calendar.DAY_OF_WEEK, theDayOfWeek);
			cal2.setTime(cal.getTime());
			theTimeFrame = new TimeFrame(timeFrameId,employeeService.findById(vp.getEmployeeId()), cal, cal2, activityService.findById(activity));
		}else {
			theTimeFrame = timeFrameService.findById(timeFrameId);
			theTimeFrame.setActivity(activityService.findById(activity));
		}			
		
		theTimeFrame.getStartTime().set(Calendar.HOUR_OF_DAY, timeParser.getStartHour());
		theTimeFrame.getStartTime().set(Calendar.MINUTE, timeParser.getStartMinute());
		theTimeFrame.getEndTime().set(Calendar.HOUR_OF_DAY, timeParser.getEndHour());
		theTimeFrame.getEndTime().set(Calendar.MINUTE, timeParser.getEndMinute());
		/*
		System.out.println(">>> Saving times: " + timeParser.getStart() + "---" + timeParser.getEnd() );
		System.out.println("    timeFrameId: " + timeFrameId);
		System.out.println("    activity: " + activity);
		System.out.println("    VP: " + vp.toString());
		System.out.println("    theTimeFrame: " + theTimeFrame.toString());	
		System.out.println(">>> TimeFrame generated: " + theTimeFrame.toString() );
		 */
		
		//Save the data
		if(timeFrameService.save(theTimeFrame)) {
			//Use redirect to prevent duplicate submissions	
			return "redirect:/timesheet/" + vp.getYear() 
			+ "/" + vp.getWeek() 
			+ "/" + vp.getEmployeeId() 
			+ "/" + vp.getDayOfWeek();
		}else {
			//Add an error message:
			theModel.addAttribute("error", true);
			
			//Set model attributes to pre-populate form
			theModel.addAttribute("timesParsed", timeParser);
			theModel.addAttribute("timeFrame", theTimeFrame);
			theModel.addAttribute("activities",  activityService.findAll());
			theModel.addAttribute("theEmployee", theTimeFrame.getEmployee());
			theModel.addAttribute("viewParameters", vp);
			theModel.addAttribute("activ", theTimeFrame.getActivity().getActId());
			return "timesheets/activity-form";	
		}			
		 
	}
	
	@GetMapping("/deleteActivity")
	public String deleteActivity(@RequestParam("timeFrameId") int theId,
			 					 @RequestParam("viewWeek") int viewWeek,			 					
								 Model theModel){
		
		
		//Get time frame from the service
		TimeFrame theTimeFrame = timeFrameService.findById(theId); 
		
		int theYear= theTimeFrame.getStartTime().get(Calendar.YEAR);
		int dayOfWeek = theTimeFrame.getStartTime().get(Calendar.DAY_OF_WEEK);
		int employeeId = theTimeFrame.getEmployee().getId();
		
		if(dayOfWeek > 6) {
			dayOfWeek = dayOfWeek -7;
		}
		
		timeFrameService.deleteById(theId);
		
		ViewParameters vp = new ViewParameters(theYear, 
											viewWeek, 
											employeeId, 
											dayOfWeek );		 
		
		
		return "redirect:/timesheet/" + vp.getYear() 
								+ "/" + vp.getWeek() 
								+ "/" + vp.getEmployeeId() 
								+ "/" + vp.getDayOfWeek(); 
	}
	
	private float[] obtainDayStatistics(List<TimeFrame> theTimes) {
		
		float workedHours = 0;
		float regHours = 0;
		float otHours = 0;
		float sickHours = 0;
		float holidayHours = 0;
		float vacationsHours = 0;
		
		if (!theTimes.isEmpty()) {
			for(TimeFrame timeFrame:theTimes) {
				if(timeFrame.getActivity().equals(activityService.findById(1))) {
					workedHours = workedHours + timeFrame.duration();
				}else if(timeFrame.getActivity().equals(activityService.findById(2))) {
					vacationsHours = vacationsHours + timeFrame.duration();
				}else if(timeFrame.getActivity().equals(activityService.findById(3))) {
					sickHours = sickHours + timeFrame.duration();
				}else if(timeFrame.getActivity().equals(activityService.findById(4))) {
					holidayHours = holidayHours + timeFrame.duration();
				}
			}
			
			if(workedHours <= 8) {
				regHours = workedHours;
			}else {
				regHours = 8;
				otHours = workedHours - regHours;
			}
			
		}
		
		float[] stats = {regHours, otHours, sickHours, holidayHours, vacationsHours};
		return stats;
	}

	private float[][] obtainWeekStatistics(List<TimeFrame>[] theTimes){
		float[][] stats = {
			    {0,0,0,0,0},
			    {0,0,0,0,0},
			    {0,0,0,0,0},
			    {0,0,0,0,0},
			    {0,0,0,0,0},
			    {0,0,0,0,0},
			    {0,0,0,0,0},
			    {0,0,0,0,0},
			    {0,0,0,0,0}
			};
		int i=0;
		
		for (List<TimeFrame> oneDayTimes:theTimes) {
			stats[i] = obtainDayStatistics(oneDayTimes);
			for (int j=0; j< stats[i].length; j++) {
				stats[8][j] = stats[8][j] + stats[i][j];
			}
			i ++;
		}
		
		return stats;
	}
	
	private String[] generateDatesOfWeek(int year, int week) {
		String[] theDays = {"Fri", "Sat", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "TOTAL"};
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.WEEK_OF_YEAR, week);
		cal.set(Calendar.DAY_OF_WEEK, 1);
		
		int startDay = cal.get(Calendar.DAY_OF_YEAR)-2;
		int endDay = cal.get(Calendar.DAY_OF_YEAR)+5;
		int j=0;
		
		for (int i=startDay; i<=endDay; i++) {
			cal.set(Calendar.DAY_OF_YEAR, i);
			theDays[j] = theDays[j] 
							+ " " 
							+ (cal.get(Calendar.MONTH)+1) 
							+ "/" 
							+ (cal.get(Calendar.DAY_OF_MONTH)) ;
			j++;
		}	
		
		return theDays;
	}
	
}
