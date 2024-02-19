package com.openclassrooms.tourguide.configuration;

import com.openclassrooms.tourguide.repository.AttractionRepository;
import com.openclassrooms.tourguide.service.GpsUtilService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import gpsUtil.GpsUtil;
import rewardCentral.RewardCentral;
import com.openclassrooms.tourguide.service.RewardsService;

@Configuration
public class TourGuideModule {

	public AttractionRepository attractionRepository;

	@Bean
	public GpsUtil getGpsUtil() {
		return new GpsUtil();
	}

	public GpsUtilService getGpsUtilService(){
		return new GpsUtilService(getGpsUtil(),attractionRepository);
	}
	
	@Bean
	public RewardsService getRewardsService() {
		return new RewardsService(getGpsUtilService(), getRewardCentral());
	}
	
	@Bean
	public RewardCentral getRewardCentral() {
		return new RewardCentral();
	}
	
}
