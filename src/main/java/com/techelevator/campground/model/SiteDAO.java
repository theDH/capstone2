package com.techelevator.campground.model;

import java.util.LinkedList;

public interface SiteDAO {
	public LinkedList<Site> searchForAvailableSites(Campground campground, String fromDate,String toDate);

}
