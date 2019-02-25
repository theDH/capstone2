package com.techelevator.campground.model;

import java.util.Date;
import java.util.LinkedList;

public interface SiteDAO {
	public LinkedList<Site> searchForAvailableSites(Campground campground, String fromDate,String toDate);

	public LinkedList<Site> advancedSearchForAvailableSites(Campground campground, String fromDate, String toDate, int maxOccupancy, boolean accessible,
           int rvLength, boolean utilities);
	
}
