package com.techelevator.campground.model.jdbc;

import java.util.LinkedList;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.Site;
import com.techelevator.campground.model.SiteDAO;

public class JDBCSiteDAO implements SiteDAO {
	private JdbcTemplate jdbcTemplate;

	public JDBCSiteDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public LinkedList<Site> searchForAvailableSites(Campground campground, String arrival,String departure){
		String sqlQueryForAvailableReservation = "SELECT s.site_id, max_occupancy, accessible, max_rv_length, " +
												"utilities, c.daily_fee, c.campground_id " + 
												"FROM reservation r " + 
												"JOIN site s ON r.site_id = s.site_id " + 
												"JOIN campground c ON c.campground_id = s.campground_id " + 
				"WHERE c.campground_id = ? AND (? <= from_date OR  ? >= to_date); ";
			SqlRowSet results = jdbcTemplate.queryForRowSet(sqlQueryForAvailableReservation, campground.getCampground_id(), departure, arrival );
			LinkedList<Site> listOfCampgrounds = new LinkedList<Site>();
			while(results.next()) {
				Site openSite = new Site();
				openSite = mapRowToSite(results);
				listOfCampgrounds.add(openSite);
			}		
				return listOfCampgrounds;
	}

	
	private Site mapRowToSite(SqlRowSet results) {
		Site theSite = new Site();
		
		theSite.setSite_id(results.getLong("side_id"));
		theSite.setCampground_id(results.getLong("campground_id"));
		theSite.setSite_number(results.getInt("site_number"));
		theSite.setMax_occupancy(results.getInt("max_occupancy"));
		theSite.setAccessible(results.getBoolean("accessible"));
		theSite.setMax_rv_length(results.getInt("max_rv_length"));
		theSite.setUtilities(results.getBoolean("utilities"));
		theSite.setDaily_fee(results.getDouble("daily_fee"));
	
		return theSite;
	}

}
