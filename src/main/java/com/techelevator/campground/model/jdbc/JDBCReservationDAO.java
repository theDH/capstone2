package com.techelevator.campground.model.jdbc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.LinkedList;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.campground.Util;
import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.Reservation;
import com.techelevator.campground.model.ReservationDAO;
import com.techelevator.campground.model.Site;

public class JDBCReservationDAO implements ReservationDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCReservationDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public Long makeNewReservation(int siteIdToReserve, String nameOfReservation, String fromDateString,
			String toDateString) {

		Date fromDate = Util.stringToDate(fromDateString);
		Date toDate = Util.stringToDate(toDateString);
		LocalDate arrivalLocalDate = fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate departureLocalDate = toDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		Reservation newReservation = new Reservation();
//		newReservation.setReservation_id(getNextReservationId());
		newReservation.setReservation_name(nameOfReservation);
		newReservation.setFrom_date(arrivalLocalDate);
		newReservation.setTo_date(departureLocalDate);
		newReservation.setCreate_date(LocalDate.now());
		newReservation.setSite_id(Long.valueOf(siteIdToReserve));

		String sqlCreateNewReservation = "INSERT INTO reservation (site_id, name, from_date, to_date, create_date) "
				+ "Values (?, ?, ?, ?, ?); ";

		jdbcTemplate.update(sqlCreateNewReservation, newReservation.getSite_id(), newReservation.getReservation_name(),
				newReservation.getFrom_date(), newReservation.getTo_date(), newReservation.getCreate_date());

		String sqlGetIdOfNewReservation = "SELECT reservation_id FROM reservation "
				+ "WHERE site_id = ? AND name = ? AND from_date = ? AND to_date = ? AND create_date = ?";

		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetIdOfNewReservation, newReservation.getSite_id(),
				newReservation.getReservation_name(), newReservation.getFrom_date(), newReservation.getTo_date(),
				newReservation.getCreate_date());
		results.next();
		Long reservation_id = results.getLong(1);

		newReservation.setReservation_id(reservation_id);
		return newReservation.getReservation_id();

	}

//	private Long getNextReservationId() {
//		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval(reservation.seq_reservation_id);");
//		if (nextIdResult.next()) {
//			return nextIdResult.getLong(1);
//		} else {
//			throw new RuntimeException("Something went wrong while getting an id for the new reservation");
//		}
//	}

	public Reservation handleExistingReservationSearch(Long valueOf) {

		String sqlSearchForReservation = "SELECT * FROM reservation WHERE reservation_id = ?;";

		SqlRowSet result = jdbcTemplate.queryForRowSet(sqlSearchForReservation, valueOf);
		result.next();

		Reservation reservation = mapToRowToReservation(result);

		return reservation;

	}
//	public LinkedList<Site> searchForReservation(Campground campground, String arrival,String departure){
//		String sqlQueryForAvailableReservation = "SELECT s.site_id, max_occupancy, accessible, max_rv_length, " +
//												"utilities, c.daily_fee, c.campground_id" + 
//												"FROM reservation r" + 
//												"JOIN site s ON r.site_id = s.site_id" + 
//												"JOIN campground c ON c.campground_id = s.campground_id" + 
//				"WHERE c.campground_id = 1 AND (? <= from_date OR  ? >= to_date); ";
//			SqlRowSet results = jdbcTemplate.queryForRowSet(sqlQueryForAvailableReservation, departure,arrival );
//			LinkedList<Site> listOfCampgrounds = new LinkedList<Site>();
//			while(results.next()) {
//				Site openSite = new Site();
//				openSite = mapRowToSite(results);
//				listOfCampgrounds.add(openSite);
//			}		
//				return listOfCampgrounds;
//	}
//
//	
//	private Site mapRowToSite(SqlRowSet results) {
//		Site theSite = new Site();
//		
//		theSite.setSite_id(results.getLong("side_id"));
//		theSite.setCampground_id(results.getLong("campground_id"));
//		theSite.setSite_number(results.getInt("site_number"));
//		theSite.setMax_occupancy(results.getInt("max_occupancy"));
//		theSite.setAccessible(results.getBoolean("accessible"));
//		theSite.setMax_rv_length(results.getInt("max_rv_length"));
//		theSite.setUtilities(results.getBoolean("utilities"));
//		theSite.setDaily_fee(results.getDouble("daily_fee"));
//	
//		return theSite;
//	}

	private Reservation mapToRowToReservation(SqlRowSet results) {
		Reservation theReservation = new Reservation();

		theReservation.setReservation_id(results.getLong("reservation_id"));
		theReservation.setSite_id(results.getLong("site_id"));
		theReservation.setReservation_name(results.getString("name"));
		theReservation.setFrom_date(Util.convertDateIntoLocalDate(results.getDate("from_date")));
		theReservation.setTo_date(Util.convertDateIntoLocalDate(results.getDate("to_date")));
		theReservation.setCreate_date(Util.convertDateIntoLocalDate(results.getDate("create_date")));

		return theReservation;
	}

}
