package com.openclassrooms.tourguide.repository;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class AttractionRepository {

    private static GpsUtil gpsUtil;
    public static List<Attraction> attractions = new ArrayList<>();

    public AttractionRepository(GpsUtil gpsUtil) {
        AttractionRepository.gpsUtil = gpsUtil;
    }

    public static List<Attraction> getAttractions() {
        attractions = gpsUtil.getAttractions();
        return attractions;
    }
}
