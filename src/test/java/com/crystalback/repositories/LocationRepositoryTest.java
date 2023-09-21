package com.crystalback.repositories;

import com.crystalback.controllers.LocationController;
import com.crystalback.documents.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;

@DataMongoTest
class LocationRepositoryTest {
    @Autowired
    private LocationRepository locationRepository;
    @BeforeEach
    public void init() {
        locationRepository.deleteAll();
    }
    @Test
    void findByLocationNear() {
        Location location = new Location();
        location.setName("Paris 1");
        location.setLocation(new GeoJsonPoint(48.856614,2.3522219));
        locationRepository.save(location);

        Location location1 = new Location();
        location1.setName("Paris 2");
        location1.setLocation(new GeoJsonPoint(48.872818,2.345014));
        locationRepository.save(location1);

        //don't be returned in list
        Location location2 = new Location();
        location2.setName("Pacific Ocean");
        location2.setLocation(new GeoJsonPoint(0,0));
        locationRepository.save(location2);
        //don't be returned in list
        Location location3 = new Location();
        location3.setName("18 km");
        location3.setLocation(new GeoJsonPoint(48.71165292330417, 2.327988251564761));
        locationRepository.save(location3);
        List<Location> list = locationRepository.findByLocationNear(new GeoJsonPoint(48.850410,2.341818),new Distance(LocationController.DISTANCE_KM, Metrics.KILOMETERS));

        assert list.size()==2;
        assert list.contains(location);
        assert list.contains(location1);

    }
}