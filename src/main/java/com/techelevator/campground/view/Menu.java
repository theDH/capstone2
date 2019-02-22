package com.techelevator.campground.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import com.techelevator.campground.model.Campground;
import com.techelevator.campground.model.Park;

public class Menu {
	
	
	private PrintWriter out;
	private Scanner in;

	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while(choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for(int i = 0; i < options.length; i++) {
			int optionNum = i+1;
			out.println(optionNum+") "+options[i]);
		}
		out.print("\nPlease choose an option >>> ");
		out.flush();
	}	
	
	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if(selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch(NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if(choice == null) {
			out.println("\n*** "+userInput+" is not a valid option ***\n");
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
		System.out.println("\t\tName \t\t\t Open \t\t Close \t\t Daily Fee");
		int i =1;
		for (Campground campground: campgroundList) {
			System.out.print("#" + i + "\t" );
			System.out.format("%-35s", campground.getCampground_name());
			System.out.println( campground.getOpen_from_mm()
			+ "\t\t" + campground.getOpen_to_mm() + "\t\t" + campground.getDaily_fee());
			i++;
		}
	}

}
