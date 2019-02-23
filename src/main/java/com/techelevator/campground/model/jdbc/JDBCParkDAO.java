package com.techelevator.campground.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.Util;
import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;

public class JDBCParkDAO implements ParkDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCParkDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public LinkedList<Park> getAllParks() {
		LinkedList<Park> listOfParks = new LinkedList<>();
		String sqlFindParks = "SELECT * " + "FROM park ORDER BY name; ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindParks);
		while (results.next()) {
			Park thePark = mapRowToPark(results);
			listOfParks.add(thePark);
		}
		return listOfParks;
	}
	
	
	@Override
	public void displayParkInformation(String parkChoice) {
		
		String sqlFindParkInformation = "SELECT * FROM park WHERE name = ?;"; 
		SqlRowSet parkInformation = jdbcTemplate.queryForRowSet(sqlFindParkInformation, parkChoice);
		Park park = new Park();
		parkInformation.next(); 
		
		park = mapRowToPark(parkInformation);
		
		
	}
	

//	
//	


//=================HELPER METHODS=========================//



	private Park mapRowToPark(SqlRowSet results) {

		Park thePark = new Park();
		
		thePark.setPark_id(results.getLong("park_id"));
		thePark.setPark_name(results.getString("name"));
		thePark.setPark_location(results.getString("location"));
		thePark.setEstablish_date(Util.convertDateIntoLocalDate(results.getDate("establish_date")));
		thePark.setPark_area(results.getInt("area")); // area size in sq kilometer
		thePark.setVisitors(results.getInt("visitors"));
		thePark.setDescription(results.getString("description"));
		return thePark;
	}

}
