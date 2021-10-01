package com.mycom.springboot.thymeleafdemo.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="activity")
public class Activity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int actId;
	
	@Column(name="description")
	private String description;
	
	@OneToMany(mappedBy="activity", 
			   cascade= {CascadeType.DETACH, CascadeType.MERGE,
					   	 CascadeType.PERSIST, CascadeType.REFRESH})
	private List<TimeFrame> timeFrames;

	public Activity() {

	}
	
	public Activity(int id, String description) {
		this.actId = id;
		this.description = description;
	}

	public int getActId() {
		return actId;
	}

	public void setActId(int id) {
		this.actId = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public List<TimeFrame> getTimeFrames() {
		return timeFrames;
	}

	public void setTimeFrames(List<TimeFrame> timeFrames) {
		this.timeFrames = timeFrames;
	}
	
	
	@Override
	public String toString() {
		return "Activity [id=" + actId + ", description=" + description + "]";
	}
	
	
	
}
