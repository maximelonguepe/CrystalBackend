package com.crystalback.repositories;

import com.crystalback.documents.Location;
import org.springframework.data.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LocationRepository extends MongoRepository<Location,String> {
    List<Location> findByLocationNear(GeoJsonPoint location, Distance maxDistance);
}
