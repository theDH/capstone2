package com.techelevator.campground;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

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
}
