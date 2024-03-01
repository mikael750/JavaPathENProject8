package com.openclassrooms.tourguide.controller;

import java.util.List;

import com.openclassrooms.tourguide.DTO.AttractionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gpsUtil.location.VisitedLocation;

import com.openclassrooms.tourguide.service.TourGuideService;
import com.openclassrooms.tourguide.model.User;
import com.openclassrooms.tourguide.model.UserReward;

import tripPricer.Provider;

@RestController
public class TourGuideController {

	@Autowired
	TourGuideService tourGuideService;

    /**
     * Return a greeting when calling the default url
     *
     * @return String
     */
    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    /**
     * Get location of a given user
     *
     * @param userName userName
     * @return VisitedLocation
     */
    @RequestMapping("/getLocation")
    public VisitedLocation getLocation(@RequestParam String userName) {
    	return tourGuideService.getUserLocation(getUser(userName));
    }

    /**
     * Get 5 closest attractions for a given user
     *
     * @param userName userName
     * @return JSON containing a list of five nearby Attractions
     */
    @RequestMapping("/getNearbyAttractions") 
    public List<AttractionDTO> getNearbyAttractions(@RequestParam String userName) {
        User user = tourGuideService.getUser(userName);
        return tourGuideService.getFiveNearByAttractions(user);
    }

    /**
     * Get rewards for a given user
     *
     * @param userName userName
     * @return List of UserReward
     */
    @RequestMapping("/getRewards")
    public List<UserReward> getRewards(@RequestParam String userName) {
    	return tourGuideService.getUserRewards(getUser(userName));
    }

    /**
     * Get Trip Providers based on the user preferences
     *
     * @param userName userName
     * @return List of Provider
     */
    @RequestMapping("/getTripDeals")
    public List<Provider> getTripDeals(@RequestParam String userName) {
    	return tourGuideService.getTripDeals(getUser(userName));
    }

    /**
     * Utility method to get user from service
     *
     * @param userName userName
     * @return User
     */
    private User getUser(String userName) {
    	return tourGuideService.getUser(userName);
    }

}