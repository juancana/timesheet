package com.mycom.springboot.thymeleafdemo.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mycom.springboot.thymeleafdemo.dao.TimeRepository;
import com.mycom.springboot.thymeleafdemo.entity.Employee;
import com.mycom.springboot.thymeleafdemo.entity.TimeFrame;

@Service
public class TimeFrameServiceImpl implements TimeFrameService {
	private TimeRepository timeRepository;
	
	@Autowired
	public TimeFrameServiceImpl(TimeRepository repository) {
		timeRepository = repository;
	}
	
	@Override
	public List<TimeFrame> findAll() {
		List<TimeFrame> times = timeRepository.findAll();
		return times;
	}

	@Override
	public TimeFrame findById(int theId) {
		Optional<TimeFrame> result = timeRepository.findById(theId);		
		TimeFrame theTimeFrame = null;
		if(result.isPresent()) {
			theTimeFrame = result.get();
		}else {
			//Didn't find the employee
			throw new RuntimeException("Did not find time-frame with id: " + theId);
		}		
		return theTimeFrame;
	}

	@Override
	public boolean save(TimeFrame theTimeFrame) {
		boolean success = false;
		
		//Need to check if the new time frame is already covered by that employee, 
		//excluding holidays, which can overlap with working on OT
		
		if(theTimeFrame.getId() == 0) {//It is a new timeFrame
			
			//Lookup other timeframes that overlap this one
			List<TimeFrame> check1 = timeRepository.findByEmployeeAndStartTimeBetween(theTimeFrame.getEmployee(),theTimeFrame.getStartTime(),theTimeFrame.getEndTime());
			List<TimeFrame> check2 = timeRepository.findByEmployeeAndEndTimeBetween(theTimeFrame.getEmployee(),theTimeFrame.getStartTime(),theTimeFrame.getEndTime());
						
			if((check1.isEmpty()) && (check2.isEmpty())) { //There are no overlaps
				success = true;
			}
			else {//There are overlapping time frames
				
				//Check for holidays 
				if(((checkForHolidays(check1)>0 || check1.isEmpty()) //All overlapping frames are holidays or empty list
							&& (checkForHolidays(check2)>0 || check2.isEmpty())  //And my frame is a WORK activity
							&& theTimeFrame.getActivity().getDescription().equals("WORK"))) {
					success = true;
				}else if((checkForHolidays(check1)<0 || check1.isEmpty()) //All overlapping frames are WORKING activities or empty list
							&& (checkForHolidays(check2)<0 || check2.isEmpty())//And my frame is a holiday
							&& theTimeFrame.getActivity().getDescription().equals("HOLIDAY")) {
					success = true;
				}
				else {
					success = false;
				}
				
			}
			
		}else { //It is not a new timeFrame, we are editing an old one
			success = true;
		}
		
		
		if(success) 
			timeRepository.save(theTimeFrame);
		else 
			System.out.println(">>> ERROR while saving time. Timeframe already covered");
		
		return success;
	}
	
	

	@Override
	public void deleteById(int theId) {
		timeRepository.deleteById(theId);

	}

	@Override
	public List<TimeFrame> findByEmployee(Employee theEmployee) {		
		return timeRepository.findByEmployee(theEmployee);
	}
	@Override
	public List<TimeFrame> findBetweenTimes(Calendar startTime, Calendar endTime) {		
		return timeRepository.findByStartTimeBetween(startTime, endTime);
	}
	
	@Override
	public List<TimeFrame> findByDay(Calendar theDay) {				
		List<Calendar> calendars =timeFrameOfCompleteDay(theDay);		
		return timeRepository.findByStartTimeBetween(calendars.get(0), calendars.get(1));
	}
	
	@Override
	public List<Employee> findEmployeesInADay(Calendar theDay) {
		List<Calendar> calendars =timeFrameOfCompleteDay(theDay);			
		return getEmployees(timeRepository.findByStartTimeBetween(calendars.get(0), calendars.get(1)));
	}
	
	@Override
	public List<Employee> findEmployeesInAWeek(int year, int week) {
		List<Calendar> calendars  =timeFrameOfCompleteWeek(year, week);		
		return getEmployees(timeRepository.findByStartTimeBetween(calendars.get(0), calendars.get(1)));
	}
			
	@Override
	public List<TimeFrame> findByEmployeeAndDay(Employee employee, Calendar theDay) {		
		List<Calendar> calendars =timeFrameOfCompleteDay(theDay);	
		return timeRepository.findByEmployeeAndStartTimeBetween(employee, calendars.get(0), calendars.get(1));
	}
	
	@Override
	public List<TimeFrame> findByEmployeeAndWeek(Employee employee, int year, int week) {		
		List<Calendar> calendars =timeFrameOfCompleteWeek(year, week);			
		return timeRepository.findByEmployeeAndStartTimeBetween(employee, calendars.get(0), calendars.get(1));
	}


	@Override
	public List<TimeFrame> findByEmployeeAndWeekOrderByStartTime(Employee employee, int year, int week) {		
			List<Calendar> calendars =timeFrameOfCompleteWeek(year, week);		
		return timeRepository.findByEmployeeAndStartTimeBetweenOrderByStartTime(employee, calendars.get(0), calendars.get(1));
	}

	@Override
	public List<TimeFrame>[] findByEmployeeAndWeekSeparateByDays(Employee employee, int year, int week) {		
		List<TimeFrame>[] times = separateByDaysOfWeek(findByEmployeeAndWeekOrderByStartTime(employee, year, week));		
		return times;
	}
	
	private int checkForHolidays(List<TimeFrame> thelist) {
		//Returns 1 if all are holidays
		//Returns 0 if mixed or empty
		//Returns -1 if there are no holidays at all
		int check = 0;
		boolean allHolidays = true, allWork = true;
		
		if (!thelist.isEmpty()) {
			for (TimeFrame timeFrame : thelist) {
				if (timeFrame.getActivity().getDescription().equals("HOLIDAY")) {
					allHolidays = allHolidays && true; 
					allWork = allWork && false;
				}else if (timeFrame.getActivity().getDescription().equals("WORK")){
					allHolidays = allHolidays && false; 
					allWork = allWork && true;
				}else {
					allHolidays = allHolidays && false; 
					allWork = allWork && false;
				}
			}
			
			if(allHolidays && !allWork) { //All are holidays
				check = 1;
			}else if(!allHolidays && allWork) { //All are non holidays
				check = -1;
			}else if(!allHolidays &&!allWork) { //Mixed
				check = 0;
			}
		}		
		
		return check;
	}
	
	
	private List<Calendar> timeFrameOfCompleteDay(Calendar theDay) {
		
		List<Calendar> calendars = new ArrayList<Calendar>();		
		calendars.add(Calendar.getInstance());
		calendars.add(Calendar.getInstance());

		calendars.get(0).setTime(theDay.getTime());			
		calendars.get(0).set(Calendar.HOUR_OF_DAY, 0);
		calendars.get(0).set(Calendar.MINUTE, 0);
		calendars.get(0).set(Calendar.SECOND, 0);
		calendars.get(0).set(Calendar.MILLISECOND, 0);		

		calendars.get(1).setTime(theDay.getTime());	
		calendars.get(1).set(Calendar.HOUR_OF_DAY, 23);
		calendars.get(1).set(Calendar.MINUTE, 59);
		calendars.get(1).set(Calendar.SECOND, 59);

		return calendars;
	}
	
	private List<Calendar> timeFrameOfCompleteWeek(int year, int week) {
		
		List<Calendar> calendars = new ArrayList<Calendar>();
		
		calendars.add(Calendar.getInstance());
		calendars.add(Calendar.getInstance());
		
		//Starts previous Friday at 15:00
		calendars.get(0).set(Calendar.YEAR, year);
		calendars.get(0).set(Calendar.WEEK_OF_YEAR, week-1);
		calendars.get(0).set(Calendar.DAY_OF_WEEK,6);
		calendars.get(0).set(Calendar.HOUR_OF_DAY, 15);
		calendars.get(0).set(Calendar.MINUTE, 0);
		calendars.get(0).set(Calendar.SECOND, 0);
		calendars.get(0).set(Calendar.MILLISECOND, 0);
		
		//Ends  Friday at 15:00	
		calendars.get(1).setTime(calendars.get(0).getTime());
		calendars.get(1).set(Calendar.DAY_OF_YEAR, 7 + calendars.get(0).get(Calendar.DAY_OF_YEAR));
		calendars.get(1).set(Calendar.HOUR_OF_DAY, 15);
		calendars.get(1).set(Calendar.MINUTE, 0);
		calendars.get(1).set(Calendar.SECOND, 1);
		
		return calendars;
	}
	
	private List<TimeFrame>[] separateByDaysOfWeek(List<TimeFrame> timeList){
		/*
		 * This method assumes we get an ordered list of time frames between 
		 * Friday 3pm of previous week and Friday 3pm of this week 
		 */
		List<TimeFrame>[] timesSeparated = new ArrayList[8];
		for (int i = 0; i < 8; i++) {
			timesSeparated[i] = new ArrayList<TimeFrame>();
        }
		
		int dayOfWeek, hour;
		
		for (int i=0; i< timeList.size(); i++) {			
			dayOfWeek = timeList.get(i).getStartTime().get(Calendar.DAY_OF_WEEK);
			
			if(dayOfWeek ==6) { //FRIDAY (Need to distinguish between Friday previous week and Friday this week)			
				hour = timeList.get(i).getStartTime().get(Calendar.HOUR);
				if(hour >= 15) {//If it is Friday after 3pm, we assume it is  Friday previous week
					timesSeparated[0].add(timeList.get(i));
				} else  { //If it is Friday before 3pm, we assume it is  Friday this week
					timesSeparated[dayOfWeek+1].add(timeList.get(i));
				}
			} else if(dayOfWeek ==7) {//SATURDAY
				timesSeparated[1].add(timeList.get(i));
			} else {// Rest of the weekdays
				timesSeparated[dayOfWeek+1].add(timeList.get(i));
			}
		}
		return timesSeparated;
	}
	
	private List<Employee> getEmployees(List<TimeFrame> timeFrames){
		List<Employee>  employees = new ArrayList<Employee>();
		
		for(TimeFrame tempTimeFrame:timeFrames) {
			if(!employees.contains(tempTimeFrame.getEmployee())) {
				employees.add(tempTimeFrame.getEmployee());
			}
		}
		return employees;
	}

}
