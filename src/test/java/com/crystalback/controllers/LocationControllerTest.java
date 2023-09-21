package com.crystalback.controllers;


import com.crystalback.documents.Location;
import com.crystalback.repositories.LocationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)

public class LocationControllerTest {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @AfterEach
    public void deleteAfter() {
        locationRepository.deleteAll();
    }

    @Test
    public void testGetAllLocations() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/locations")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testSaveLocation() throws Exception {
        Location location = new Location();
        location.setName(" Paris Test ");
        location.setLocation(new GeoJsonPoint(48.8566, 2.3508));
        MvcResult mvcResult = mockMvc.perform(post("/api/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(location)))
                .andExpect(status().isCreated())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Location locationSaved = objectMapper.readValue(contentAsString, Location.class);
        assert locationSaved != null;
        assert locationSaved.getId() != null;
        assert locationSaved.getLocation().equals(new GeoJsonPoint(48.8566, 2.3508));
        assert locationSaved.getName().equals(" Paris Test ");
        locationRepository.delete(locationSaved);
    }

    @Test
    public void testNearLocationInferieur() throws Exception {
        Location location1 = new Location();
        location1.setName("Paris");
        location1.setLocation(new GeoJsonPoint(48.850410, 2.341818));

        Location location2 = new Location();
        location2.setName("Paris 2");
        location2.setLocation(new GeoJsonPoint(48.872818, 2.345014));

        List<Location> locations = new ArrayList<>();
        locations.add(location1);
        locations.add(location2);

        locations = locationRepository.saveAll(locations);
        MvcResult mvcResult = mockMvc.perform(get("/api/locations/near")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(locations)))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assert contentAsString.equals(LocationController.INFERIEUR);
        locationRepository.deleteAll(locations);
    }

    @Test
    public void testNearLocationSuperieur() throws Exception {
        Location location1 = new Location();
        location1.setName("Paris");
        location1.setLocation(new GeoJsonPoint(48.850410, 2.341818));

        Location location2 = new Location();
        location2.setName("Pacific ");
        location2.setLocation(new GeoJsonPoint(0, 0));

        List<Location> locations = new ArrayList<>();
        locations.add(location1);
        locations.add(location2);

        locations = locationRepository.saveAll(locations);
        MvcResult mvcResult = mockMvc.perform(get("/api/locations/near")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(locations)))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assert contentAsString.equals(LocationController.SUPERIEUR);
        locationRepository.deleteAll(locations);
    }
    @Test
    public void testNearKOSize() throws Exception {
        Location location1 = new Location();
        location1.setName("Paris");
        location1.setLocation(new GeoJsonPoint(48.850410, 2.341818));


        List<Location> locations = new ArrayList<>();
        locations.add(location1);


        locations = locationRepository.saveAll(locations);
        mockMvc.perform(get("/api/locations/near")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(locations)))
                .andExpect(status().isBadRequest());
        locationRepository.deleteAll(locations);
    }

    @Test
    public void testNearKONotList() throws Exception {
        Location location1 = new Location();
        location1.setName("Paris");
        location1.setLocation(new GeoJsonPoint(48.850410, 2.341818));

        location1 = locationRepository.save(location1);
        mockMvc.perform(get("/api/locations/near")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(location1)))
                .andExpect(status().isBadRequest());
        locationRepository.delete(location1);
    }

    @Test
    public void testNullElement() throws Exception {
        Location location1 = null;

        Location location2 = new Location();
        location2.setName("Paris");
        location2.setLocation(new GeoJsonPoint(48.850410, 2.341818));
        location2 = locationRepository.save(location2);

        List<Location> locations = new ArrayList<>();
        locations.add(location1);
        locations.add(location2);

        MvcResult mvcResult = mockMvc.perform(get("/api/locations/near")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(locations)))
                .andExpect(status().isBadRequest())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assert contentAsString.equals("Un des objets est nul");
        locationRepository.delete(location2);
    }

    private String asJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
