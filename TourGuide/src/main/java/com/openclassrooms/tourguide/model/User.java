package com.openclassrooms.tourguide.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import gpsUtil.location.VisitedLocation;
import lombok.Data;
import lombok.NoArgsConstructor;
import tripPricer.Provider;

@Data
@NoArgsConstructor
public class User {
	private UUID userId;
	private String userName;
	private String phoneNumber;
	private String emailAddress;
	private Date latestLocationTimestamp;
	private List<VisitedLocation> visitedLocations = new ArrayList<>();
	private List<UserReward> userRewards = new ArrayList<>();
	private UserPreferences userPreferences = new UserPreferences();
	private List<Provider> tripDeals = new ArrayList<>();
	public User(UUID userId, String userName, String phoneNumber, String emailAddress) {
		this.userId = userId;
		this.userName = userName;
		this.phoneNumber = phoneNumber;
		this.emailAddress = emailAddress;
	}

	public void addToVisitedLocations(VisitedLocation visitedLocation) {
		visitedLocations.add(visitedLocation);
	}

	public List<VisitedLocation> getVisitedLocations() {
		return visitedLocations;
	}

	public void clearVisitedLocations() {
		visitedLocations.clear();
	}

	public void addUserReward(UserReward userReward) {
			userRewards.add(userReward);
	}

	public VisitedLocation getLastVisitedLocation() {
		return visitedLocations.get(visitedLocations.size() - 1);
	}

}
