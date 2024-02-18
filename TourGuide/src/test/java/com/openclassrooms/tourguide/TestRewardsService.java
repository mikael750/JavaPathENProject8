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

	private RewardsService rewardsService;
	private GpsUtil gpsUtil;
	private GpsUtilService gpsUtilService;

	@BeforeEach
	public void setUp() {
		gpsUtil = new GpsUtil();
		AttractionRepository attractionRepository = new AttractionRepository(gpsUtil);
		gpsUtilService = new GpsUtilService(gpsUtil, attractionRepository);
		rewardsService = new RewardsService(gpsUtil, new RewardCentral());

	}

	@Test
	public void userGetRewards() {

		InternalTestHelper.setInternalUserNumber(0);
		var tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

		var user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		var attraction = gpsUtil.getAttractions().get(0);
		user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
		tourGuideService.trackUserLocation(user);
		var userRewards = user.getUserRewards();
		tourGuideService.tracker.stopTracking();
		assertEquals(1, userRewards.size());
	}

	@Test
	public void isWithinAttractionProximity() {

		var attraction = gpsUtil.getAttractions().get(0);
		assertTrue(gpsUtilService.isWithinAttractionProximity(attraction, attraction));
	}

	@Disabled // Needs fixed - can throw ConcurrentModificationException
	@Test
	public void nearAllAttractions() {

		gpsUtilService.setProximityBuffer(Integer.MAX_VALUE);

		InternalTestHelper.setInternalUserNumber(1);
		var tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

		rewardsService.calculateRewards(tourGuideService.getAllUsers().get(0));
		var userRewards = tourGuideService.getUserRewards(tourGuideService.getAllUsers().get(0));
		tourGuideService.tracker.stopTracking();

		assertEquals(gpsUtil.getAttractions().size(), userRewards.size());
	}

}
