package com.crystalback.documents;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@Getter
@Setter

public class Location {
    @Id
    private String id;
    private String name;
    private GeoJsonPoint location;



}
