package com.openclassrooms.tourguide;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;

import com.openclassrooms.tourguide.DTO.AttractionDTO;
import com.openclassrooms.tourguide.repository.AttractionRepository;
import com.openclassrooms.tourguide.service.GpsUtilService;
import org.junit.jupiter.api.BeforeEach;
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

	private final GpsUtil gpsUtil = new GpsUtil();
	private final AttractionRepository attractionRepository = new AttractionRepository(gpsUtil);
	private final GpsUtilService gpsUtilService = new GpsUtilService(gpsUtil, attractionRepository);
	private final RewardsService rewardsService = new RewardsService(gpsUtilService, new RewardCentral());
	private final TourGuideService tourGuideService = new TourGuideService(gpsUtilService, rewardsService);

	User user = new User(UUID.randomUUID(), "jon", "000", "jon@tourGuide.com");
	User user2 = new User(UUID.randomUUID(), "jon2", "000", "jon2@tourGuide.com");

	@BeforeEach
	public void setUp() {
		InternalTestHelper.setInternalUserNumber(1);
		tourGuideService.addUser(user);
		tourGuideService.addUser(user2);
	}

	@Test
	public void test_getUserLocation() {

		VisitedLocation visitedLocation = tourGuideService.trackUserLocationSync(user);

		tourGuideService.tracker.stopTracking();

		assertEquals(visitedLocation.userId, user.getUserId());
	}

	@Test
	public void test_addUser() {

		User retrivedUser = tourGuideService.getUser(user.getUserName());
		User retrivedUser2 = tourGuideService.getUser(user2.getUserName());

		tourGuideService.tracker.stopTracking();

		assertEquals(user, retrivedUser);
		assertEquals(user2, retrivedUser2);
	}

	@Test
	public void test_getAllUsers() {

		List<User> allUsers = tourGuideService.getAllUsers();

		tourGuideService.tracker.stopTracking();

		assertTrue(allUsers.contains(user));
		assertTrue(allUsers.contains(user2));
	}

	@Test
	public void test_trackUser() {

		VisitedLocation visitedLocation = tourGuideService.trackUserLocationSync(user);

		tourGuideService.tracker.stopTracking();

		assertEquals(user.getUserId(), visitedLocation.userId);
	}

	@Test
	public void test_getFiveNearbyAttractions() {

		tourGuideService.trackUserLocation(user);

		List<AttractionDTO> attractions = tourGuideService.getFiveNearByAttractions(user);

		tourGuideService.tracker.stopTracking();

		assertEquals(5, attractions.size());
	}

	@Test
	public void test_getTripDeals() {

		List<Provider> providers = tourGuideService.getTripDeals(user);

		tourGuideService.tracker.stopTracking();

		assertEquals(5, providers.size());
	}

}
