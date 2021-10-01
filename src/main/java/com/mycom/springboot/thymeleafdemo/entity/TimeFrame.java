package com.mycom.springboot.thymeleafdemo.entity;

import java.util.Calendar;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="time")
public class TimeFrame {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@ManyToOne(fetch=FetchType.LAZY,
			cascade= {CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="employee_id")	
	private Employee employee;

	
	@Column(name="start_time")
	private Calendar startTime;
	
	@Column(name="end_time")
	private Calendar endTime;
	
	@ManyToOne(fetch=FetchType.LAZY,
			cascade= {CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinColumn(name="activity")	
	private Activity activity;
	
	public TimeFrame() {
		
	}
	
public TimeFrame(int id, Employee employee, Activity activity) {
		this.id = id;
		this.employee = employee;
		this.activity = activity;
	}


	public TimeFrame(Employee employee, Calendar startTime, Calendar endTime, Activity activity) {
		this.employee = employee;
		this.startTime = startTime;
		this.endTime = endTime;
		this.activity = activity;
	}

	public TimeFrame(int id, Employee employee, Calendar startTime, Calendar endTime, Activity activity) {
		this.id = id;
		this.employee = employee;
		this.startTime = startTime;
		this.endTime = endTime;
		this.activity = activity;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployeeId(Employee employee) {
		this.employee = employee;
	}

	public Calendar getStartTime() {
		return startTime;
	}

	public void setStartTime(Calendar startTime) {
		this.startTime = startTime;
	}

	public Calendar getEndTime() {
		return endTime;
	}

	public void setEndTime(Calendar endTime) {
		this.endTime = endTime;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public String toString() {
		return "TimeFrame [id=" + id + ", employeeID=" + employee.getId() 
				+ ", startTime=" + parseCalendar(startTime) + ", endTime="
				+ parseCalendar(endTime) + ", activity=" + activity + "]";
	}
	
	public String parseCalendar(Calendar cal) {
		
		return cal.get(Calendar.YEAR) 
				+ "/" + cal.get(Calendar.MONTH) 
				+ "/" + cal.get(Calendar.DAY_OF_MONTH)
				+ " @ " + cal.get(Calendar.HOUR_OF_DAY)
				+ ":" + cal.get(Calendar.MINUTE);
	}
	
	public float duration() {
		//Assuming activity starts and finishes on the same day
		
		float duration = (endTime.get(Calendar.HOUR_OF_DAY) - startTime.get(Calendar.HOUR_OF_DAY)) 
				 + ((endTime.get(Calendar.MINUTE) - startTime.get(Calendar.MINUTE))/60f);
		
		return duration;
	}
	
	
}
