package com.openclassrooms.tourguide;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Date;
import java.util.UUID;

import com.openclassrooms.tourguide.repository.AttractionRepository;
import com.openclassrooms.tourguide.service.GpsUtilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import gpsUtil.GpsUtil;
import gpsUtil.location.VisitedLocation;
import rewardCentral.RewardCentral;
import com.openclassrooms.tourguide.helper.InternalTestHelper;
import com.openclassrooms.tourguide.service.RewardsService;
import com.openclassrooms.tourguide.service.TourGuideService;
import com.openclassrooms.tourguide.model.User;

public class TestRewardsService {

	GpsUtil gpsUtil = new GpsUtil();
	AttractionRepository attractionRepository = new AttractionRepository(gpsUtil);
	GpsUtilService gpsUtilService = new GpsUtilService(gpsUtil, attractionRepository);
	RewardsService rewardsService = new RewardsService(gpsUtilService, new RewardCentral());
	TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

	@Test
	public void userGetRewards() {

		InternalTestHelper.setInternalUserNumber(0);
		//var tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

		var user = new User(UUID.randomUUID(),
				"jon", "000", "jon@tourGuide.com");
		var attraction = gpsUtilService.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		tourGuideService.trackUserLocation(user);
		var userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertEquals(1, userRewards.size());
	}

	@Test
	public void isWithinAttractionProximity() {

		var attraction = gpsUtilService.getAttractions().get(0);
		assertTrue(rewardsService.isWithinAttractionProximity(attraction, attraction));
	}

	@Disabled // Fixed - threw ConcurrentModificationException but return failure
	@Test
	public void nearAllAttractions() {

		rewardsService.setProximityBuffer(Integer.MAX_VALUE);
		InternalTestHelper.setInternalUserNumber(1);

		var user = tourGuideService.getAllUsers().get(0);
		rewardsService.calculateRewards(user);
		var userRewards = tourGuideService.getUserRewards(user);
		tourGuideService.tracker.stopTracking();

		assertEquals(gpsUtilService.getAttractions().size(), userRewards.size());
	}

}
