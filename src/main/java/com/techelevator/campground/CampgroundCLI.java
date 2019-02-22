package com.techelevator.campground;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;
import com.techelevator.campground.model.Site;
import com.techelevator.campground.model.SiteDAO;
import com.techelevator.campground.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.campground.model.jdbc.JDBCParkDAO;
import com.techelevator.campground.model.jdbc.JDBCSiteDAO;
import com.techelevator.campground.view.Menu;

public class CampgroundCLI {

	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private SiteDAO siteDAO;

	public CampgroundCLI(DataSource datasource) {
		parkDAO = new JDBCParkDAO(datasource);
		campgroundDAO = new JDBCCampgroundDAO(datasource);
		siteDAO = new JDBCSiteDAO(datasource);
		this.menu = new Menu(System.in, System.out);

	}

	public static void main(String[] args) {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setUrl("jdbc:postgresql://localhost:5432/campground");
		dataSource.setUsername("postgres");
		dataSource.setPassword("postgres1");

		CampgroundCLI application = new CampgroundCLI(dataSource);
		application.run();

	}

//	public CampgroundCLI(DataSource datasource) {
//		parkDAO = new JDBCParkDAO(datasource);
//	
//		
//	}

	public void run() {
		while (true) {
			printHeading("Main Menu");
			String userChoice = (String) menu.getChoiceFromOptions(Util.MAIN_MENU_OPTIONS);
			if (userChoice.equals(Util.MAIN_MENU_OPTION_PARKS)) {
				handleParkChoice();
//				

			} else if (userChoice.contentEquals(Util.MAIN_MENU_OPTION_EXIT)) {
				System.out.println("Bye Bye");
				System.exit(0);
			}

		}

	}

	private void handleParkChoice() {
		LinkedList<Park> listOfParks = new LinkedList<Park>();
		listOfParks = parkDAO.getAllParks(); // does a query on db park and returns list
		menu.printParkList(listOfParks);

//		String userChoice = getUserInput("\n Please Select Park"); // asks user to input
//		int parkChoice = Integer.parseInt(userChoice);

		int parkChoice = getUserInputAsAnInt("\n Please Select Park");
		
		int i = 1;
		int indexOfPark = 0;
		for (Park park : listOfParks) {
			if (parkChoice == i) {
				indexOfPark = i - 1;

			}
			i++;
		}

		Park thePark = new Park();
		thePark = listOfParks.get(indexOfPark);
		menu.printParkInformation(thePark);
		handleCampgroundMenu(thePark);
	}

	private void handleCampgroundMenu(Park thePark) {
		boolean  campgroundMenu =true;
		while (campgroundMenu) {
			printHeading("Select an Option");
			String userChoice = (String) menu.getChoiceFromOptions(Util.CAMPGROUND_MENU_OPTIONS);
			if (userChoice.equals(Util.CAMPGROUND_MENU_OPTION_VIEW_CAMPGROUND)) {
				handleViewCampground(thePark);
//			
			} else if (userChoice.contentEquals(Util.CAMPGROUND_MENU_OPTION_SEARCH_FOR_RESERVATION)) {
				System.out.println("Search for reservation");
//			System.exit(0);
			} else if (userChoice.contentEquals(Util.CAMPGROUND_MENU_OPTION_RETURN_TO_PREVIOUS)) {
				campgroundMenu = false;
			}
		}

	}

	private void handleViewCampground(Park thePark) {
		LinkedList<Campground> campgroundList = new LinkedList<Campground>();
		campgroundList = campgroundDAO.handleViewCampground(thePark);
		printHeading(thePark.getPark_name() + " Campgrounds\n");
		menu.printCampgroundList(campgroundList);
		handleCampgroundOptions(campgroundList);
		

	}

	private void handleCampgroundOptions(LinkedList<Campground> campgroundList) {

		printHeading("Select a command");
		String userChoice = (String) menu.getChoiceFromOptions(Util.RESERVATION_MENU_OPTIONS);
		if (userChoice.equals(Util.SEARCH_RESERVATION_MENU)) {
			handleCampgroundReservation(campgroundList);
		} else if (userChoice.contentEquals(Util.RETURN_TO_CAMPGROUND_MENU_OPTIONS)) {
		}
		
	}

	private void handleCampgroundReservation(LinkedList<Campground> campgroundList) {
		menu.printCampgroundList(campgroundList);
		
		int campgroundChoice = getUserInputAsAnInt("\n Which campground (enter 0 to cancel) ?");
		String nameOfCampground = "";
		int i = 1;
		int indexOfCampground = 0;
		for (Campground campground : campgroundList) {
			if (campgroundChoice == i) {
				nameOfCampground = campground.getCampground_name();
				indexOfCampground = i - 1;

			}
			i++;
		}
		String fromDate = getUserInput("What is the arrival date? (yyyy-MM-dd)");
		String toDate = getUserInput("What is the departure date? (yyyy-MM-dd)");
		LinkedList<Site> listOfsite = new LinkedList<Site>();
		listOfsite = siteDAO.searchForAvailableSites(campgroundList.get(indexOfCampground),fromDate,toDate);
		menu.printSiteList(listOfsite);
		//System.out.println("You selected "+nameOfCampground);
		
	}

	private void printHeading(String headingText) {
		System.out.println("\n" + headingText);
		for (int i = 0; i < headingText.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
	}

	@SuppressWarnings("resource")
	private String getUserInput(String prompt) {
		System.out.print(prompt + " >>> ");
		return new Scanner(System.in).nextLine();
	}

	@SuppressWarnings("resource")
	private int getUserInputAsAnInt(String prompt) {
		System.out.print(prompt + " >>> ");
		String temp = new Scanner(System.in).nextLine();
		return Integer.parseInt(temp);
	}
}
