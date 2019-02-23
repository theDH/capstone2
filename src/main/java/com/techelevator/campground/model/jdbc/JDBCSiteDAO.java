package com.techelevator.campground.model.jdbc;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
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

	public LinkedList<Site> searchForAvailableSites(Campground campground, String arrival, String departure) {
	

		///////// Collection conflictingRervation
		
		String sqlQueryForConflictingReservation = "SELECT s.site_id " + "FROM reservation r "
				+ "JOIN site s ON r.site_id = s.site_id " + "JOIN campground c ON c.campground_id = s.campground_id "
				+ "WHERE c.campground_id = ? AND ((? > from_date AND ? < to_date) OR (?  > from_date AND ? <= to_date) "
				+ "OR (? >= from_date AND ? < to_date) OR (? < from_date AND ? > to_date)) ORDER BY s.site_id; ";

		Date arrivalDate = new Date();
		Date departureDate = new Date();
		try {
			arrivalDate = new SimpleDateFormat("yyyy/MM/dd").parse(arrival);
			departureDate = new SimpleDateFormat("yyyy/MM/dd").parse(departure);
		} catch (ParseException e) {
			System.out.println("Invalid date, please enter again");
		}
		

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlQueryForConflictingReservation, campground.getCampground_id(),
				arrivalDate, departureDate, arrivalDate, arrivalDate, departureDate, departureDate, arrivalDate,
				departureDate);

		LinkedList<Long> unavailableSiteIdList = new LinkedList<Long>();

		while (results.next()) {
			Long unavailableSiteId = mapRowToSitesId(results);
			unavailableSiteIdList.add(unavailableSiteId);
		}

		//String unavailablesiteIdList = convertSiteIdListToSqlReadyString(unavailableSiteIdList);

		
///////// Calculating the duration of the stay
		LocalDate arrivalLocalDate = arrivalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate departureLocalDate = departureDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Period period = Period.between(arrivalLocalDate, departureLocalDate);
		int durationOfStay = period.getDays();
		System.out.println("\n Duration of stay " + durationOfStay + " day(s)");
		
			
///////// Initializing the list of available sites to a list with all the sites
		LinkedList<Long> listOfAllSitesIdAvailable = new LinkedList<Long>();
		String sqlQueryForAllSites = "SELECT s.site_id " + 
				"FROM reservation r " + 
				"JOIN site s ON r.site_id = s.site_id " + 
				"JOIN campground c ON c.campground_id = s.campground_id " + 
				"WHERE c.campground_id = ?";
		SqlRowSet results2 = jdbcTemplate.queryForRowSet(sqlQueryForAllSites, campground.getCampground_id());
		
		while (results2.next()) {
			Long SiteId = mapRowToSitesId(results2);
			listOfAllSitesIdAvailable.add(SiteId);
		}
		
		
////////// Remove the unvailable sites from the list of available sites
		listOfAllSitesIdAvailable.removeAll(unavailableSiteIdList);
		
		LinkedList<Site> listOfAvailableCampgrounds = new LinkedList<Site>();

		
	for (Long siteId :listOfAllSitesIdAvailable) {	
		String sqlQueryForAvailableReservation = "SELECT s.site_id, s.campground_id, site_number, max_occupancy, accessible, max_rv_length, " +
		"utilities, c.daily_fee * ? AS  daily_fee " + 
		"FROM reservation r " + 
		"JOIN site s ON r.site_id = s.site_id " + 
		"JOIN campground c ON c.campground_id = s.campground_id " + 
		"WHERE c.campground_id = ? AND s.site_id = ?  LIMIT 5; ";
		SqlRowSet results3 = jdbcTemplate.queryForRowSet(sqlQueryForAvailableReservation, durationOfStay, campground.getCampground_id(), siteId );
		
		while (results3.next()) {
			 Site openSite = new Site();
			 openSite = mapRowToSite(results3);
			 listOfAvailableCampgrounds.add(openSite);
		}
		
	}
	
		
		if(listOfAvailableCampgrounds.isEmpty()) {
			System.out.println("No campsites available for those dates, press any key to continue and try alternate dates");
		}
		return listOfAvailableCampgrounds;
	}
	
	
	
	//////////////////////////// Helper methods

//	private String convertSiteIdListToSqlReadyString(LinkedList<Long> unavailableSiteIdList) {
//		String sqlString = "";
//
//		int i = 0;
//		for (Long id : unavailableSiteIdList) {
//			if (i < unavailableSiteIdList.size() - 1) {
//				sqlString += unavailableSiteIdList.get(i) + ",";
//				i++;
//			}
//			else {sqlString += unavailableSiteIdList.get(i);
//					}
//
//		}
//
//		return sqlString;
//
//	}

	private Long mapRowToSitesId(SqlRowSet results) {
		Long siteId = results.getLong(1);

		return siteId;
	}

	private Site mapRowToSite(SqlRowSet results) {
		Site theSite = new Site();

		theSite.setSite_id(results.getLong("site_id"));
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
