package com.eventx.api.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "locations")
public class Location {

    @Id
    private String id;
    private String userId;
    private String name;
    private String description;
    private String address;
    private String city;
    private String province;
    private String region;
    private Integer cap;
    private String gpsCoord;
    @Builder.Default
    private List<String> reviews = new ArrayList<>();
    private LocalDateTime registrationDate;
}
