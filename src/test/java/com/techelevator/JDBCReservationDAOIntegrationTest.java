package com.techelevator;

import java.sql.SQLException;
import org.junit.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.campground.model.Reservation;
import com.techelevator.campground.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.campground.model.jdbc.JDBCParkDAO;
import com.techelevator.campground.model.jdbc.JDBCReservationDAO;
import com.techelevator.campground.model.jdbc.JDBCSiteDAO;
import com.techelevator.campground.view.Menu;



public class JDBCReservationDAOIntegrationTest {
	
	private static SingleConnectionDataSource dataSource;
	private JDBCCampgroundDAO daoCampground;
	private JDBCParkDAO daoPark;
	private JDBCSiteDAO daoSite;
	private JDBCReservationDAO daoReservation;
	private Menu menu;
	
	@BeforeClass
	public static void setupDataSource() {
		dataSource = new SingleConnectionDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");
		dataSource.setAutoCommit(false);
	}
	
	@AfterClass
	public static void closeDataSource() throws SQLException {
		dataSource.destroy();
	}
	
	@Before
	public void setup() {
		String sqlInsertPark ="INSERT INTO campground (park_id, name, open_from_mm, open_to_mm, daily_fee) "
				+ "VALUES ('3', 'Dummy Campground' , 03, 10, '100');";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertPark);
		menu = new Menu(System.in, System.out);
		daoCampground = new JDBCCampgroundDAO(dataSource); 
		daoPark = new JDBCParkDAO(dataSource);
		daoReservation = new JDBCReservationDAO(dataSource);
		
	}
	
	@After
	public void rollback() throws SQLException{
		dataSource.getConnection().rollback();
	}

	////// Test 1 to test if new reservation was properly inserted and that the long correctly references that reservation 
	//////  Also tests the handleExisitingReservation Method
	@Test 
	public void check_That_New_Reservation_was_made(){
		int siteId = 1;
		String nameOfReservation = "TEST RESERVATION";
		String from_date = "2020/06/01";
		String to_date = "2020/06/07";
		Long testReservation;
		testReservation = daoReservation.makeNewReservation(siteId, nameOfReservation, from_date, to_date );
		Reservation check = daoReservation.handleExistingReservationSearch(testReservation);
		
		String reservationName = check.getReservation_name();
		
		Assert.assertTrue(nameOfReservation.equals(reservationName)); 
	}
	
}
