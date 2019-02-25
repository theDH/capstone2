package com.techelevator.campground;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedList;

import com.techelevator.campground.model.Campground;

public class Util {
	// DO NOT INSTANTIATE
	private Util() {
	}

	public static final String MAIN_MENU_OPTION_PARKS = "Select a Park for Futher Details";
//	public static final String MAIN_MENU_OPTION_CAMPGROUND_LIST = "Display Campgrounds at Park";
	public static final String MAIN_MENU_OPTION_EXIT = "Exit";
	public static final String[] MAIN_MENU_OPTIONS = new String[] { MAIN_MENU_OPTION_PARKS, MAIN_MENU_OPTION_EXIT };

	public static final String CAMPGROUND_MENU_OPTION_VIEW_CAMPGROUND = "View Campgrounds";
	public static final String CAMPGROUND_MENU_OPTION_SEARCH_FOR_RESERVATION = "Search for Reservations(with ID)";
	public static final String CAMPGROUND_MENU_OPTION_RETURN_TO_PREVIOUS = "Return to Previous Screen";
	public static final String[] CAMPGROUND_MENU_OPTIONS = new String[] { CAMPGROUND_MENU_OPTION_VIEW_CAMPGROUND,
			CAMPGROUND_MENU_OPTION_SEARCH_FOR_RESERVATION, CAMPGROUND_MENU_OPTION_RETURN_TO_PREVIOUS };

	public static final String SEARCH_RESERVATION_MENU = "Search for available reservation";
	public static final String RETURN_TO_CAMPGROUND_MENU_OPTIONS = "Return to previous screen";
	public static final String[] RESERVATION_MENU_OPTIONS = new String[] { SEARCH_RESERVATION_MENU,
			RETURN_TO_CAMPGROUND_MENU_OPTIONS };

	public static LocalDate convertDateIntoLocalDate(Date date) {

		String pattern = "yyyy-MM-dd";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);

		String dateAsAString = simpleDateFormat.format(date);

		String[] parsedDateStrings = dateAsAString.split("-");
		int year = Integer.parseInt(parsedDateStrings[0]);
		int month = Integer.parseInt(parsedDateStrings[1]);
		int day = Integer.parseInt(parsedDateStrings[2]);
		LocalDate localDate = LocalDate.now();

		return localDate.of(year, month, day);

	}

	public static Date stringToDate(String dateAsAString) {
		Date date = new Date();
		try {
			date = new SimpleDateFormat("yyyy/MM/dd").parse(dateAsAString);
		} catch (ParseException e) {
			System.out.println("Invalid date, please enter again");
		}
		return date;
	}

	// ================DAN SUNDAY ADD SECTION START===============================

	public static boolean isReservationInSeason(LinkedList<Campground> campgroundList, int indexId, String arrivalDate,
			String departureDate) {

		int arrivalMonth = getMonth(arrivalDate);
		int departureMonth = getMonth(departureDate);

		int parkOpen = Integer.parseInt((campgroundList.get(indexId).getOpen_from_mm()));

		int parkClose = Integer.parseInt(campgroundList.get(indexId).getOpen_to_mm());

		if (arrivalMonth < parkOpen || departureMonth > parkClose) {
			return true;
		} else {
			return false;
		}
	}

	private static int getMonth(String yearMmDd) {

		int result = Integer.parseInt(yearMmDd.substring(5, 7));
		return result;
	}

	public static String convertMonthNumberIntoString(int number) {
		String[] monthString = { "January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December" };
		String month = "";
		for (int i = 1; i <= 12; i++) {
			if (number == i) {
				month = monthString[i - 1];

			}
		}
		return month;
	}

	public static String convertIsAccessible(boolean isAccessible) {
		String accessible = "";
		if (isAccessible == true) {
			accessible = "Yes";
		} else {
			accessible = "No";
		}
		return accessible;
	}

	public static String convertRvToString(int length) { // sunday update to display length
		if (length < 1) {
			return "N/A";
		} else {
			String maxRv = Integer.toString(length);
			return maxRv + " ft";
		}
	}

	public static String convertUtilityBool(boolean utility) { // sunday update to display ifUtility
		if (utility == true) {
			return "Yes";
		} else {
			return "No";
		}
	}
	//================DAN LOOK AT THIS AND LEARN IT=======================
	public static int getMaxOccupancy(int numberOfPeople) {
		if (numberOfPeople <= 0) {
			System.out.println("You can't reserve a site for less than 1 person!");
			System.out.println("The program is ending");
			System.exit(0);
		} else if (numberOfPeople >= 1 && numberOfPeople <= 6) {
			return 6;
		} else if (numberOfPeople == 7) {
			return 7;
		} else if (numberOfPeople > 7 && numberOfPeople <= 10) {
			return 10;
		} else if (numberOfPeople > 10 && numberOfPeople <= 35) {
			return 35;
		} else if (numberOfPeople > 35 && numberOfPeople <= 55) {
			return 55;
		} else {
			System.out.println("You can't reserve a site for less than 1 person!");
			System.out.println("The program is ending");
			System.exit(0);
		}
		return -1;
	}

	public static boolean checkForArrivalDateAfterDeparture(String arrival, String departure) {

		Date arrivalDate = Util.stringToDate(arrival);
		Date departureDate = Util.stringToDate(departure);
		
		boolean conflict = false;
		if (arrivalDate.after(departureDate)) {

			conflict = true;
		}
		return conflict;
	}

	public static boolean checkForArrivalDateInPast(String arrival, String departure) {
		
		Date arrivalDate = Util.stringToDate(arrival);
		Date departureDate = Util.stringToDate(departure);
		
		Date currentDate = new Date();
		boolean conflict = false;
		if (arrivalDate.before(currentDate)) {
			conflict = true;
		}
		return conflict;
	}
	// ================END===============================
}
