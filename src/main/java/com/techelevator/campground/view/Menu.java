package com.techelevator.campground.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.techelevator.campground.Util;
import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.Park;
import com.techelevator.campground.model.Reservation;
import com.techelevator.campground.model.Site;

public class Menu {

	private PrintWriter out;
	private Scanner in;

	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		out.print("\nPlease choose an option >>> ");
		out.flush();
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will
			// be null
		}
		if (choice == null) {
			out.println("\n*** " + userInput + " is not a valid option ***\n");
		}
		return choice;
	}

//====================Print To Console methods=======================//	
	public void printParkList(LinkedList<Park> listOfPark) {
		System.out.println();
		if (listOfPark.size() > 0) {
			int i = 1;
			for (Park park : listOfPark) {

				System.out.println(i + ") \t" + park.getPark_name());
				i++;
			}
			
		} else {
			System.out.println("\n **No result for park list query**");
		}
		
	}

	public void printParkInformation(Park parkName) {
		System.out.println(parkName.getPark_name());
		System.out.println("Location:\t\t" + parkName.getPark_location());
		System.out.println("Established:\t\t" + parkName.getPark_location());
		System.out.println("Area:\t\t\t" + parkName.getPark_area() + " sq km");
		System.out.println("Annual Visitors: \t" + parkName.getVisitors());
		System.out.println("\n" + parkName.getDescription() + "\n");

	}

	public void printCampgroundList(LinkedList<Campground> campgroundList) {
		System.out.println("\tName \t\t\t\t Open \t\t Close \t\t Daily Fee");
		int i = 1;
		for (Campground campground : campgroundList) {
			
				//System.out.format("%-2s" + i + " ");
				System.out.format("%-41s", i + "# " + campground.getCampground_name());
				
				System.out.format("%-16s", Util.convertMonthNumberIntoString(Integer.parseInt(campground.getOpen_from_mm())));  
						
				System.out.format("%-16s", Util.convertMonthNumberIntoString(Integer.parseInt(campground.getOpen_to_mm()))); 
				System.out.format("%-2s", "$" + campground.getDaily_fee() + "\n");
				i++; 
			
		}
	}

	public void printSiteList(LinkedList<Site> siteList, String arrival, String departure) {
		
		int durationOfStay = durationOfTheStay(arrival, departure);
		
		System.out.println("Site ID. \t Max. Occup. \t Accessible? \t Max RV Length \t\t Utility \tTotal Cost");
		int i = 1;
		Long siteId = Long.valueOf(0); // Used to removed the identical site id because the GROUP BY clause didn't worked
		for (Site site : siteList) {
			while(i<6 && site.getSite_id() != siteId ) {
				siteId = site.getSite_id();
			System.out.println(site.getSite_id() + "\t\t\t" + site.getMax_occupancy() + "\t  " + Util.convertIsAccessible(site.isAccessible())
					+ "\t\t\t" + Util.convertRvToString(site.getMax_rv_length()) + "\t\t " + Util.convertUtilityBool(site.isUtilities()) + "\t\t $" +  durationOfStay * site.getDaily_fee());
			i++;
		}
		}
	}

	private int durationOfTheStay(String arrival, String departure) {
		Date arrivalDate = Util.stringToDate(arrival);
		Date departureDate = Util.stringToDate(departure);
		LocalDate arrivalLocalDate = arrivalDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate departureLocalDate = departureDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		Period period = Period.between(arrivalLocalDate, departureLocalDate);
		int durationOfStay = (int)ChronoUnit.DAYS.between(arrivalLocalDate, departureLocalDate);
		// int durationOfStay = period.getDays();
		return durationOfStay;
		}
	public void displayReservation(Reservation existingReservation) {
		System.out.println("Existing Reservation: " + existingReservation.getReservation_id());
		System.out.println("Site: " + existingReservation.getSite_id() );
		System.out.println("Reservation Name: " + existingReservation.getReservation_name());
		System.out.println("Reservation arrival date: " + existingReservation.getFrom_date());
		System.out.println("Reservation departure date: " + existingReservation.getTo_date());
		System.out.println("Reservation creation date: "+ existingReservation.getCreate_date());
		
	}

}
