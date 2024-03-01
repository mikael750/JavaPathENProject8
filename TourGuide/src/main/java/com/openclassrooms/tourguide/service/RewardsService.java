package com.openclassrooms.tourguide.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.model.UserReward;

@Service
public class RewardsService {
	private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
	// proximity in miles
	private final int defaultProximityBuffer = 10;
	private int proximityBuffer = defaultProximityBuffer;

	private final GpsUtilService gpsUtilService;
	private final RewardCentral rewardsCentral;

	public RewardsService(GpsUtilService gpsUtilService, RewardCentral rewardCentral) {
		this.gpsUtilService = gpsUtilService;
		this.rewardsCentral = rewardCentral;
	}

	public void setProximityBuffer(int proximityBuffer) {
		this.proximityBuffer = proximityBuffer;
	}

	public void setDefaultProximityBuffer() {
		proximityBuffer = defaultProximityBuffer;
	}

	public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
		int attractionProximityRange = 200;
		return !(getDistance(attraction, location) > attractionProximityRange);
	}

	public boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
		return !(getDistance(attraction, visitedLocation.location) > proximityBuffer);
	}

	/**
	 * Calculate rewards for a given user if the user is near an unvisited attraction,
	 * The method adds the rewards to the user,
	 * It uses synchronized() and irate userLocations as to not throw ConcurrentModificationException.
	 *
	 * @param user the user we want to calculate the rewards of
	 */
	public void calculateRewards(User user) {
		List<VisitedLocation> userLocations = new ArrayList<>(user.getVisitedLocations());
		var attractions = gpsUtilService.getAttractions();

		synchronized (user.getUserRewards()) {
			for (VisitedLocation visitedLocation : userLocations) {
				for (Attraction attraction : attractions) {
					if (user.getUserRewards().stream().noneMatch(r ->
							r.attraction.attractionName.equals(attraction.attractionName))) {
						if (nearAttraction(visitedLocation, attraction)) {
							user.addUserReward(new UserReward(
									visitedLocation, attraction, getRewardPoints(attraction, user)));
						}
					}
				}
			}
		}
	}

	public int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}

	public double getDistance(Location loc1, Location loc2) {
		double lat1 = Math.toRadians(loc1.latitude);
		double lon1 = Math.toRadians(loc1.longitude);
		double lat2 = Math.toRadians(loc2.latitude);
		double lon2 = Math.toRadians(loc2.longitude);

		double angle = Math.acos(Math.sin(lat1) * Math.sin(lat2)
				+ Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

		double nauticalMiles = 60 * Math.toDegrees(angle);
		return STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
	}
}
