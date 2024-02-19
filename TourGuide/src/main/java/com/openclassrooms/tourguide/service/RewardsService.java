package com.openclassrooms.tourguide.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.openclassrooms.tourguide.repository.AttractionRepository;
import org.springframework.stereotype.Service;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.model.UserReward;

@Service
public class RewardsService {
    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;

    //private final GpsUtil gpsUtil;
	private final GpsUtilService gpsUtilService;
	private final RewardCentral rewardsCentral;



	public RewardsService(GpsUtilService gpsUtilService, RewardCentral rewardCentral) {
		this.gpsUtilService = gpsUtilService;
		//AttractionRepository attractionRepository = new AttractionRepository(gpsUtil);
		//gpsUtilService = new GpsUtilService(gpsUtil, attractionRepository);


		this.rewardsCentral = rewardCentral;
	}


	/**
	 * @param user
	 */
	public void calculateRewards(User user) {
		CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList(user.getVisitedLocations());
		var attractions = gpsUtilService.getAttractions();//gpsUtil.getAttractions();
		List<UserReward> rewardsToAdd = new ArrayList<>();

		//synchronized (user.getUserRewards()) {
		for(VisitedLocation visitedLocation : userLocations) {
			for(Attraction attraction : attractions) {
				if(user.getUserRewards().stream().noneMatch(r ->
						r.attraction.attractionName.equals(attraction.attractionName))) {
					if(gpsUtilService.nearAttraction(visitedLocation, attraction)) {
						rewardsToAdd.add(new UserReward(visitedLocation, attraction,
								getRewardPoints(attraction, user)));
					}
				}
			}
		}
		user.addUserRewards(rewardsToAdd);
		//}
	}

	private int getRewardPoints(Attraction attraction, User user) {
		return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
	}
}
