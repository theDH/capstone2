package com.techelevator.campground.model;

import java.util.LinkedList;

public interface ReservationDAO {

	Long makeNewReservation(int siteIdToReserve, String nameOfReservation, String fromDate, String toDate);
	
	public Reservation handleExistingReservationSearch(Long valueOf); 

}
