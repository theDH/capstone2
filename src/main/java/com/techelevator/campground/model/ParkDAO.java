package com.techelevator.campground.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;



public interface ParkDAO {

	
	public LinkedList<Park> getAllParks();
	
	
	
	public void displayParkInformation(String parkChoice);

}
