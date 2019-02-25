package com.techelevator;

import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;

import javax.xml.crypto.Data;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.jdbc.JDBCParkDAO;
import com.techelevator.campground.view.Menu;

public class JDBCParkDAOIntegrationTest {

	private static SingleConnectionDataSource dataSource;
	private JDBCParkDAO dao;
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
		String sqlInsertPark ="INSERT INTO park (name, location, establish_date, area, visitors, description) "
				+ "VALUES ('Dummy Park', 'Pennsylvania' , ?, 18, 4, 'This park is beautiful');";
		@SuppressWarnings("deprecation")
		Date date = new Date(1981,03,05);
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		jdbcTemplate.update(sqlInsertPark, date);
		menu = new Menu(System.in, System.out);
		dao = new JDBCParkDAO(dataSource); 
		
	}
	
	@After
	public void rollback() throws SQLException{
		dataSource.getConnection().rollback();
	}
	
	@Test
	public void getAllParks_test() {
		LinkedList<Park> listOfParks = new LinkedList<Park>();
	
		listOfParks = dao.getAllParks();
		
		Assert.assertEquals(4,listOfParks.size());
		
		}
}
