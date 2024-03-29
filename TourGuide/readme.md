[![forthebadge](https://forthebadge.com/images/badges/made-with-java.svg)](https://forthebadge.com)

# Technologies

> Java 17  
> Spring Boot 3.X  
> JUnit 5  

# Prerequisites 

How to have gpsUtil, rewardCentral and tripPricer dependencies available ?

> Run : 
- mvn install:install-file -Dfile=/libs/gpsUtil.jar -DgroupId=gpsUtil -DartifactId=gpsUtil -Dversion=1.0.0 -Dpackaging=jar  
- mvn install:install-file -Dfile=/libs/RewardCentral.jar -DgroupId=rewardCentral -DartifactId=rewardCentral -Dversion=1.0.0 -Dpackaging=jar  
- mvn install:install-file -Dfile=/libs/TripPricer.jar -DgroupId=tripPricer -DartifactId=tripPricer -Dversion=1.0.0 -Dpackaging=jar

# Start application

* copy/clone this project.
* run the command ``mvn clean install`` where the pom.xml is.
* run ``java -jar tourguide-0.0.1-SNAPSHOT`` in the target
* Access endpoints on localhost:8080/

# EndPoints

* **GET** : `` "/" `` 

return: "Greetings from TourGuide!"


* **GET** : ``"/getLocation"``

parameter: userName
return: Location


* **GET** : ``"/getNearbyAttractions"``

parameter: userName
return: 5 closest attractions for a given user


* **GET** : ``"/getAllCurrentLocations"``

parameter: no_parameter
return: Map of UserId : Location


* **GET** : ``"/getRewards"``

parameter: userName
return: List of UserReward


* **GET** : ``"/getTripDeals"``

parameter: userName
return: List of Provider


## Authors

Created by the developers of TourGuide