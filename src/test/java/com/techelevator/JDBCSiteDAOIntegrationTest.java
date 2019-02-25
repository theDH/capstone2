package com.techelevator;

import java.sql.SQLException;
import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.Site;
import com.techelevator.campground.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.campground.model.jdbc.JDBCParkDAO;
import com.techelevator.campground.model.jdbc.JDBCSiteDAO;
import com.techelevator.campground.view.Menu;

public class JDBCSiteDAOIntegrationTest {
	private static SingleConnectionDataSource dataSource;
	private JDBCCampgroundDAO daoCampground;
	private JDBCParkDAO daoPark;
	private JDBCSiteDAO daoSite;
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
		daoSite = new JDBCSiteDAO(dataSource);
		
	}
	
	@After
	public void rollback() throws SQLException{
		dataSource.getConnection().rollback();
	}
	

	
	@Test
	public void searchForAvailableSitesWhenNoReservation() {
		
		LinkedList<Park> parks = daoPark.getAllParks() ;
		Park park3 =parks.get(2);
		
		Assert.assertEquals("Cuyahoga Valley", park3.getPark_name());
		
		LinkedList<Campground> campgroundInPark3 = daoCampground.handleViewCampground(park3);
		
		Campground campground = campgroundInPark3.get(0);
		
		Assert.assertEquals("The Unnamed Primitive Campsites", campground.getCampground_name());
		String arrival = "2020/02/10";
		String departure ="2020/02/12";
		LinkedList<Site> availableSites = daoSite.searchForAvailableSites(campground, arrival, departure );
		menu.printSiteList(availableSites, arrival, departure);
		Assert.assertEquals(5, availableSites.size());
		
	}
	
	
	///// Testing case 1: potential reservation is within the dates another site has been reserved
	@Test
	public void searchForAvailableSitesWhenAReservationDateIsInTheDate() {
		String sqlInsert = "INSERT INTO reservation(site_id, name, from_date, to_date, create_date) "
				+ "VALUES (618, 'Lise Bilhaut', '2020/02/10', '2020/02/14', '2019/02/24');";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsert);
		
		
		LinkedList<Park> parks = daoPark.getAllParks() ;
		Park park3 =parks.get(2);
		LinkedList<Campground> campgroundInPark3 = daoCampground.handleViewCampground(park3);	
		Campground campground = campgroundInPark3.get(0);
		
		String arrival = "2020/02/11";
		String departure ="2020/02/12";
		LinkedList<Site> availableSites = daoSite.searchForAvailableSites(campground, arrival, departure);
		System.out.println("Testing case 1 - Site 618 shouldn't be available");
		menu.printSiteList(availableSites, arrival, departure);
		
		Assert.assertEquals(4, availableSites.size());
	} 

	///// Testing case 2: potential reservation starts when a site has been reserved
	@Test
	public void searchForAvailableSitesWhenAReservationArrivalDateIsInTheDate() {
		String sqlInsert = "INSERT INTO reservation(site_id, name, from_date, to_date, create_date) "
				+ "VALUES (619, 'Lise Bilhaut', '2020/02/10', '2020/02/14', '2019/02/24');";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsert);
		
		
		LinkedList<Park> parks = daoPark.getAllParks() ;
		Park park3 =parks.get(2);
		LinkedList<Campground> campgroundInPark3 = daoCampground.handleViewCampground(park3);	
		Campground campground = campgroundInPark3.get(0);
		String arrival = "2020/02/12";
		String departure ="2020/02/20";
		LinkedList<Site> availableSites = daoSite.searchForAvailableSites(campground, arrival, departure);
		System.out.println("Testing case 2 - Site 619 shouldn't be available");
		
		menu.printSiteList(availableSites, arrival, departure);
		
		Assert.assertEquals(4, availableSites.size());
	}
	
	///// Testing case 3: potential reservation ends when a site has been reserved
	@Test
	public void searchForAvailableSitesWhenAReservationDepartureDateIsInTheDate() {
		String sqlInsert = "INSERT INTO reservation(site_id, name, from_date, to_date, create_date) "
				+ "VALUES (620, 'Lise Bilhaut', '2020/02/10', '2020/02/14', '2019/02/24');";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsert);
		
		
		LinkedList<Park> parks = daoPark.getAllParks() ;
		Park park3 =parks.get(2);
		LinkedList<Campground> campgroundInPark3 = daoCampground.handleViewCampground(park3);	
		Campground campground = campgroundInPark3.get(0);
		String arrival = "2020/02/05";
		String departure ="2020/02/12";
		LinkedList<Site> availableSites = daoSite.searchForAvailableSites(campground, arrival, departure);
		System.out.println("Testing case 3 - Site 620 shouldn't be available");

		menu.printSiteList(availableSites, arrival, departure);
		
		Assert.assertEquals(4, availableSites.size());
	}
	
	
	///// Testing case 4: potential reservation is longer than a reservation which has already been made
	@Test
	public void searchForAvailableSitesWhenAReservationDateIsLongerThanTheDates() {
		String sqlInsert = "INSERT INTO reservation(site_id, name, from_date, to_date, create_date) "
				+ "VALUES (621, 'Lise Bilhaut', '2020/02/10', '2020/02/14', '2019/02/24');";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsert);
		
		
		LinkedList<Park> parks = daoPark.getAllParks() ;
		Park park3 =parks.get(2);
		LinkedList<Campground> campgroundInPark3 = daoCampground.handleViewCampground(park3);	
		Campground campground = campgroundInPark3.get(0);
		
		String arrival = "2020/02/05";
		String departure ="2020/02/20";
		LinkedList<Site> availableSites = daoSite.searchForAvailableSites(campground, arrival, departure);
		System.out.println("Testing case 4 - Site 621 shouldn't be available");

		menu.printSiteList(availableSites, arrival, departure);
		
		Assert.assertEquals(4, availableSites.size());
	}
	///// Test case 5: potential reservation is the same dates as another existing reservation 
	@Test
	public void searchForAvailabeSitesWhenReservationDatesMatchAnotherReservation() {
		String sqlInsert = "INSERT INTO reservation(site_id, name, from_date, to_date, create_date) "+
				"VALUES (618, 'Dan Herold','2020/02/10', '2020/02/14', '2019/02/24');";
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsert);
		
		LinkedList<Park> parks = daoPark.getAllParks();
		Park park3 = parks.get(2);
		LinkedList<Campground> campgroundInPark3 = daoCampground.handleViewCampground(park3);
		Campground campground = campgroundInPark3.get(0);
		
		String arrival = "2020/02/11";
		String departure ="2020/02/12";
		LinkedList<Site> availableSites = daoSite.searchForAvailableSites(campground, arrival, departure);
		System.out.println("Testing case 5 - Site 618 shouldn't be available");
		menu.printSiteList(availableSites, arrival, departure);
		
		Assert.assertEquals(4, availableSites.size());
	}
	
}
