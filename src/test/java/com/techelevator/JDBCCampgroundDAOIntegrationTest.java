package com.techelevator;

	import java.sql.SQLException;
	import java.util.Date;
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
	import com.techelevator.campground.model.jdbc.JDBCCampgroundDAO;
	import com.techelevator.campground.model.jdbc.JDBCParkDAO;
	import com.techelevator.campground.view.Menu;

	public class JDBCCampgroundDAOIntegrationTest {

		
		private static SingleConnectionDataSource dataSource;
		private JDBCCampgroundDAO daoCampground;
		private JDBCParkDAO daoPark;
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
			
		}
		
		@After
		public void rollback() throws SQLException{
			dataSource.getConnection().rollback();
		}
		
		@Test 
		public void getAListOfCampgroundFromAPark(){
			
			LinkedList<Park> listOfParks = daoPark.getAllParks();
			
			LinkedList<Campground> listOfCampgrounds = new LinkedList<Campground>();
			
			listOfCampgrounds = daoCampground.handleViewCampground(listOfParks.get(2));
			
			Assert.assertEquals(2, listOfCampgrounds.size());
			Assert.assertEquals("Dummy Campground",(listOfCampgrounds.get(1)).getCampground_name() );
			
		}

		
	
	
}
