package com.mycom.springboot.thymeleafdemo.controller;

public class ViewParameters {

	private int year;
	
	private int week;

	private int employeeId;
	
	private int dayOfWeek;
	
	public ViewParameters() {

	}

	public ViewParameters(int year, int week) {
		this.year = year;
		this.week = week;
	}
	
	public ViewParameters(int year, int week, int employeeId) {
		this.year = year;
		this.week = week;
		this.employeeId = employeeId;
	}
	
	public ViewParameters(int year, int week, int employeeId, int dayOfWeek) {
		this.year = year;
		this.week = week;
		this.employeeId = employeeId;
		this.dayOfWeek = dayOfWeek;
	}

	
	
	
	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public int getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(int dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	@Override
	public String toString() {
		return "ViewParameters [year=" + year + ", week=" + week + ", employeeId=" + employeeId + ", dayOfWeek="
				+ dayOfWeek + "]";
	}
	
	
}
