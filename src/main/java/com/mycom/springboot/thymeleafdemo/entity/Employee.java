package com.mycom.springboot.thymeleafdemo.entity;

import java.util.ArrayList;
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
@Table(name="employee")
public class Employee {

	//Define fields
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="first_name")
	private String firstName;
	
	@Column(name="last_name")
	private String lastName;
	
	@Column(name="email")
	private String email;

	@Column(name="password")
	private String password;
	
	@Column(name="active")
	private boolean active;
	
	@Column(name="hourly_rate")
	private float hourlyRate;
	
	@OneToMany(mappedBy="employee", 
			   cascade= {CascadeType.DETACH, CascadeType.MERGE,
					   	 CascadeType.PERSIST, CascadeType.REFRESH})
	private List<TimeFrame> timeFrames;
	
	
	//Define constructors
	public Employee() {
		
	}

			// (id will be automatically generated)

	public Employee(String firstName, String lastName, String email, String password, boolean active,
			float hourlyRate, List<TimeFrame> timeFrames) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.email = email;
			this.password = password;
			this.active = active;
			this.hourlyRate = hourlyRate;
			this.timeFrames = timeFrames;
		}

	
	public Employee(int id, String firstName, String lastName, String email, String password, boolean active,
		float hourlyRate, List<TimeFrame> timeFrames) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.active = active;
		this.hourlyRate = hourlyRate;
		this.timeFrames = timeFrames;
	}

	//Define getter/setter methods
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public float getHourlyRate() {
		return hourlyRate;
	}

	public void setHourlyRate(float hourlyRate) {
		this.hourlyRate = hourlyRate;
	}
	
	public List<TimeFrame> getTimeFrames() {
		return timeFrames;
	}

	public void setTimeFrames(List<TimeFrame> timeFrames) {
		this.timeFrames = timeFrames;
	}

	

	//Define toString method	

	@Override
	public String toString() {
		return "Employee [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", password=" + password + ", active=" + active + ", hourlyRate=" + hourlyRate + "]";
	}
	
	
	//Add convenience method for bi-directional relationship
	
	public void add(TimeFrame tempTimeFrame) {
		
		if (timeFrames == null) {
			timeFrames = new ArrayList<TimeFrame>();
		}
		
		timeFrames.add(tempTimeFrame);
		
		tempTimeFrame.setEmployeeId(this);
	}
}
