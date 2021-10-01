package com.mycom.springboot.thymeleafdemo.controller;

import java.util.Calendar;

import com.mycom.springboot.thymeleafdemo.entity.TimeFrame;

public class TimeParser {

	String start;
	String end;
	
	public TimeParser() {
		
	}
	
	
	
	public TimeParser(String startTime, String endTime) {
		this.start = startTime;
		this.end = endTime;
	}

	public TimeParser(TimeFrame timeFrame) {
		
		this.start = String.format("%02d", timeFrame.getStartTime().get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", timeFrame.getStartTime().get(Calendar.MINUTE));
		this.end = String.format("%02d", timeFrame.getEndTime().get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", timeFrame.getEndTime().get(Calendar.MINUTE));
	}


	public String getStart() {
		return start;
	}
	public void setStart(String startTime) {
		this.start = startTime;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String endTime) {
		this.end = endTime;
	}
	
	public int getStartHour() {
		
		return Integer.parseInt(start.split(":")[0]);
	}
	
	public int getStartMinute() {
		
		return Integer.parseInt(start.split(":")[1]);
	}
	
	public int getEndHour() {
		
		return Integer.parseInt(end.split(":")[0]);
	}
	
	public int getEndMinute() {
		
		return Integer.parseInt(end.split(":")[1]);
	}



	@Override
	public String toString() {
		return "TimeParser [startTime=" + start + ", endTime=" + end + "]";
	}
	
	
}
