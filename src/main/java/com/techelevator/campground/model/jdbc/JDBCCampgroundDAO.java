package com.techelevator.campground.model.jdbc;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;

public class JDBCCampgroundDAO implements CampgroundDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCCampgroundDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	@Override
	public LinkedList<Campground> handleViewCampground(Park thePark) {
		LinkedList<Campground> listOfCampgrounds = new LinkedList<>();
		String sqlFindCampgrounds = "SELECT * " +
									"FROM campground WHERE park_id = ?; ";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlFindCampgrounds, thePark.getPark_id());
		while(results.next()) {
			Campground theCampground = mapRowToCampground(results);
			listOfCampgrounds.add(theCampground);
		}
		return listOfCampgrounds;
	
	}

	private Campground mapRowToCampground(SqlRowSet results) {
	Campground theCampground = new Campground ();
	theCampground.setCampground_id(results.getLong("campground_id"));
	theCampground.setPark_id(results.getLong("park_id"));
	theCampground.setCampground_name(results.getString("name"));
	theCampground.setOpen_from_mm(results.getString("open_from_mm"));
	theCampground.setOpen_to_mm(results.getString("open_to_mm"));
	theCampground.setDaily_fee(results.getDouble("daily_fee"));
	return theCampground;

	}
}
