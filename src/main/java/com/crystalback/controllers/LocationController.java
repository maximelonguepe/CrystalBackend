package com.crystalback.controllers;

import com.crystalback.documents.Location;
import com.crystalback.repositories.LocationRepository;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/locations")
@CrossOrigin(origins = "*")
public class LocationController {

    public final static double DISTANCE_KM = 10;
    public final static String INFERIEUR = "<=";
    public final static String SUPERIEUR = ">";
    @Autowired
    private LocationRepository locationRepository;
    @GetMapping
    public ResponseEntity<List<Location>> findAll(){
        return new ResponseEntity<>(locationRepository.findAll(),HttpStatus.OK);
    }

    @PostMapping("near")
    public ResponseEntity<Map<String,String>> near(@RequestBody List<Location> locations){
        Map<String, String> response = new HashMap<>();
        if(locations.size()!=2){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(locations.get(0)==null || locations.get(1)==null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        List<Location> nearFirstPoint = locationRepository.findByLocationNear(locations.get(0).getLocation(),new Distance(DISTANCE_KM, Metrics.KILOMETERS));
        String result = nearFirstPoint.contains(locations.get(1)) ? INFERIEUR : SUPERIEUR;
        response.put("result",result);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Location> save(@RequestBody Location location){
        if(location.getId()!=null){
            if(location.getId().isEmpty())location.setId(null);
        }
        return new ResponseEntity<>(locationRepository.save(location),HttpStatus.CREATED);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@PathParam("id") String id){

        locationRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }



}
