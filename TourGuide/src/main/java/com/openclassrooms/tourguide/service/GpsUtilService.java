package com.openclassrooms.tourguide.service;

import com.openclassrooms.tourguide.repository.AttractionRepository;
import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GpsUtilService {

    private static GpsUtil gpsUtil;
    private final AttractionRepository attractionRepository;

    public GpsUtilService(GpsUtil gpsUtil, AttractionRepository attractionRepository) {
        GpsUtilService.gpsUtil = gpsUtil;
        this.attractionRepository = attractionRepository;
    }

    /**
     * Return the list of attraction contained in gpsUtil
     *
     * @return List of Attraction
     */
    public List<Attraction> getAttractions() {
        return attractionRepository.getAttractions();
    }

    /**
     * Get the user location
     *
     * @param userId userId
     * @return VisitedLocation
     */
    public VisitedLocation getUserLocation(UUID userId) {
        return gpsUtil.getUserLocation(userId);
    }

}
