package com.openclassrooms.tourguide;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import com.openclassrooms.tourguide.DTO.AttractionDTO;
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
import tripPricer.Provider;

public class TourGuideServiceTest {

	private GpsUtilService gpsUtilService;

	@BeforeEach
	public void setUp() {
		GpsUtil gpsUtil = new GpsUtil();
		AttractionRepository attractionRepository = new AttractionRepository(gpsUtil);
		gpsUtilService = new GpsUtilService(gpsUtil, attractionRepository);
	}

	@Test
	public void getUserLocation() {
		RewardsService rewardsService = new RewardsService(gpsUtilService, new RewardCentral());
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);
		tourGuideService.tracker.stopTracking();
		assertTrue(visitedLocation.userId.equals(user.getUserId()));
	}

	@Test
	public void addUser() {
		RewardsService rewardsService = new RewardsService(gpsUtilService, new RewardCentral());
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);

		User retrivedUser = tourGuideService.getUser(user.getUserName());
		User retrivedUser2 = tourGuideService.getUser(user2.getUserName());

		tourGuideService.tracker.stopTracking();

		assertEquals(user, retrivedUser);
		assertEquals(user2, retrivedUser2);
	}

	@Test
	public void getAllUsers() {
		RewardsService rewardsService = new RewardsService(gpsUtilService, new RewardCentral());
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);

		List<User> allUsers = tourGuideService.getAllUsers();

		tourGuideService.tracker.stopTracking();

		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}

	@Test
	public void trackUser() {
		RewardsService rewardsService = new RewardsService(gpsUtilService, new RewardCentral());
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

		tourGuideService.tracker.stopTracking();

		assertEquals(user.getUserId(), visitedLocation.userId);
	}

	@Test
	public void testGetFiveNearbyAttractions() {
		RewardsService rewardsService = new RewardsService(gpsUtilService, new RewardCentral());
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
		tourGuideService.trackUserLocation(user);

		List<AttractionDTO> attractions = tourGuideService.getFiveNearByAttractions(user);

		tourGuideService.tracker.stopTracking();

		assertEquals(5, attractions.size());
	}

	public void getTripDeals() {
		RewardsService rewardsService = new RewardsService(gpsUtilService, new RewardCentral());
		InternalTestHelper.setInternalUserNumber(0);
		TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

		User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");

		List<Provider> providers = tourGuideService.getTripDeals(user);

		tourGuideService.tracker.stopTracking();

		assertEquals(10, providers.size());
	}

}
