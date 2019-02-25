package com.techelevator.campground;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.CampgroundDAO;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.ParkDAO;
import com.techelevator.campground.model.Reservation;
import com.techelevator.campground.model.ReservationDAO;
import com.techelevator.campground.model.Site;
import com.techelevator.campground.model.SiteDAO;
import com.techelevator.campground.model.jdbc.JDBCCampgroundDAO;
import com.techelevator.campground.model.jdbc.JDBCParkDAO;
import com.techelevator.campground.model.jdbc.JDBCSiteDAO;
import com.techelevator.campground.model.jdbc.JDBCReservationDAO;
import com.techelevator.campground.view.Menu;

public class CampgroundCLI {

	private Menu menu;
	private ParkDAO parkDAO;
	private CampgroundDAO campgroundDAO;
	private SiteDAO siteDAO;
	private ReservationDAO reservationDAO;

	public CampgroundCLI(DataSource datasource) {
		parkDAO = new JDBCParkDAO(datasource);
		campgroundDAO = new JDBCCampgroundDAO(datasource);
		siteDAO = new JDBCSiteDAO(datasource);
		reservationDAO = new JDBCReservationDAO(datasource);
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
		int indexOfPark = -1;
		for (Park park : listOfParks) {
			if (parkChoice == i) {
				indexOfPark = i - 1;

			}
			i++;
		}
		if (indexOfPark == -1) {
			System.out.println("No park matches that Selection, Exiting the program");
			System.exit(0);
		}

		Park thePark = new Park();
		thePark = listOfParks.get(indexOfPark);
		menu.printParkInformation(thePark);
		handleCampgroundMenu(thePark);
	}

	private void handleCampgroundMenu(Park thePark) {
		boolean campgroundMenu = true;
		while (campgroundMenu) {
			printHeading("Select an Option");
			String userChoice = (String) menu.getChoiceFromOptions(Util.CAMPGROUND_MENU_OPTIONS);
			if (userChoice.equals(Util.CAMPGROUND_MENU_OPTION_VIEW_CAMPGROUND)) {
				handleViewCampground(thePark);
//			
			} else if (userChoice.contentEquals(Util.CAMPGROUND_MENU_OPTION_SEARCH_FOR_RESERVATION)) {
				int resevationNumber = getUserInputAsAnInt("Please Enter Existing Reservation ID Number ");
				Reservation existingReservation = reservationDAO
						.handleExistingReservationSearch(Long.valueOf(resevationNumber));
				menu.displayReservation(existingReservation);
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

		if (campgroundChoice == 0) {
			// Go back to previous menu
		} else {

			String nameOfCampground = "";
			int i = 1;
			int indexOfCampground = 0;
			for (Campground campground : campgroundList) {
				if (campgroundChoice == i) {
					nameOfCampground = campground.getCampground_name(); // why do we do this and then not pass it on?
					indexOfCampground = i - 1;

				}
				i++;
			}

			handleNewReservation(campgroundList, indexOfCampground); // Starting entering dates, so switch to
																		// Reservation objects

		}
	}

//=========================START SECTION DAN ADDED SUNDAY===================
	private void handleNewReservation(LinkedList<Campground> campgroundList, int indexOfCampground) {
		LinkedList<Site> listOfsite = new LinkedList<Site>();
		String fromDate = getUserInput("What is the arrival date? (yyyy/MM/dd)");
		String toDate = getUserInput("What is the departure date? (yyyy/MM/dd)");
		int siteIdToReserve = -1;

		boolean conflict1 = Util.checkForArrivalDateAfterDeparture(fromDate, toDate);

		boolean conflict2 = Util.checkForArrivalDateInPast(fromDate, toDate);

		if (conflict1) {
			System.out.println("\n Selected arrival date is after selected departure date, please correct  \n Bye Bye");
			// NEED TO MAKE METHOD TO RETURN TO PRIOR MENUE

			System.exit(0);

		} else if (conflict2) {
			System.out.println("\n Selected arrival date is in the past, please correct \n Bye Bye");
			// NEED TO MAKE METHOD TO RETURN TO PRIOR MENUE

			System.exit(0);
		}

		boolean outOfSeason = Util.isReservationInSeason(campgroundList, indexOfCampground, fromDate, toDate);
		if (outOfSeason) {
			System.out.println(
					"Your reservation includes dates when the park campground is closed, please select alternative date");
		}

		String advancedSearchString = getUserInput("Do you wiant to do an advanced search? (Y?N)");

		if (advancedSearchString.contentEquals("Y")) {
			siteIdToReserve = handleAdvancedReservation(campgroundList.get(indexOfCampground), fromDate, toDate);
		} else {
			listOfsite = siteDAO.searchForAvailableSites(campgroundList.get(indexOfCampground), fromDate, toDate);
			System.out.println("Results matching your criteria");
			menu.printSiteList(listOfsite, fromDate, toDate);
			siteIdToReserve = getUserInputAsAnInt("Which site should be reserved (enter 0 to cancel)?)");

		}
		// ==================END===========================

		String nameOfReservation = getUserInput("What name should the reservation be made under?");

		Long reservationId = reservationDAO.makeNewReservation(siteIdToReserve, nameOfReservation, fromDate, toDate);
		System.out.println("The reservation has been confirmed and the reservation id is " + reservationId);
	}

//================DAN LOOK AT THIS AND LEARN IT=======================
	private int handleAdvancedReservation(Campground campground, String fromDate, String toDate) {
		int rvLength = 0;
		int numberOfPeople = getUserInputAsAnInt("How many people are coming?");
		int maxOccupancy = Util.getMaxOccupancy(numberOfPeople);
		boolean accessible = getUserInputAsABoolean("Do you need the site to be wheelchair accessible? (Y/N)");
		boolean rv = getUserInputAsABoolean("Do you need the site to be RV accessible (Y/N)");
		if (rv) {
			rvLength = getUserInputAsAnInt("What length do you need? (enter 20 or 35 (in ft))");
		}

		boolean utilities = getUserInputAsABoolean("Do you need the site to have utilies? (Y/N)");

		LinkedList<Site> listOfsite = new LinkedList<Site>();
		listOfsite = siteDAO.advancedSearchForAvailableSites(campground, fromDate, toDate, maxOccupancy, accessible,
				rvLength, utilities);
		System.out.println("Avanced Search - Results matching your criteria");

		menu.printSiteList(listOfsite, fromDate, toDate);

		int siteIdToReserve = getUserInputAsAnInt("Which site should be reserved (enter 0 to cancel)?)");

		return siteIdToReserve;
	}

	// ==============end section to learn====================
	private boolean getUserInputAsABoolean(String prompt) {
		System.out.print(prompt + " >>> ");
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		String answerString = scanner.nextLine();
		if (answerString.contentEquals("Y")) {
			return true;
		} else {
			return false;

		}

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
