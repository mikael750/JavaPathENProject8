package com.openclassrooms.tourguide.DTO;

import gpsUtil.location.Location;
import lombok.Data;

@Data
public class AttractionDTO {

    // Name of Tourist attraction
    private final String attractionName;
    // Tourist attractions lat/long
    private final Location attractionLocation;
    // The model's (the user) location lat/long
    private final Location userLocation;
    // The distance in miles between the model's location and each of the attractions
    private final double distance;
    // The reward points for visiting each Attraction
    private final int rewardPoint;

}
