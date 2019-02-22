package com.techelevator.campground.model;

import java.time.LocalDate;

public class Park {
	private Long park_id;
	private String park_name;
	private String park_location;
	private LocalDate establish_date;
	private int park_area; 		// sq kilometer
	private int visitors;  		// annual number of visitors
	private String description;  // description of park
	
	
	public Long getPark_id() {
		return park_id;
	}
	public void setPark_id(Long park_id) {
		this.park_id = park_id;
	}
	public String getPark_name() {
		return park_name;
	}
	public void setPark_name(String park_name) {
		this.park_name = park_name;
	}
	public String getPark_location() {
		return park_location;
	}
	public void setPark_location(String park_location) {
		this.park_location = park_location;
	}
	public LocalDate getEstablish_date() {
		return establish_date;
	}
	public void setEstablish_date(LocalDate establish_date) {
		this.establish_date = establish_date;
	}
	public int getPark_area() {
		return park_area;
	}
	public void setPark_area(int park_area) {
		this.park_area = park_area;
	}
	public int getVisitors() {
		return visitors;
	}
	public void setVisitors(int visitors) {
		this.visitors = visitors;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	
	
	

}
