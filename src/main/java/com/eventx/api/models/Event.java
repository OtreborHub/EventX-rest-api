package com.eventx.api.models;

import java.math.BigDecimal;
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
@Document(collection = "events")
public class Event {

    @Id
    private String id;
    private String creationId;
    private String name;
    private String description;
    private String locationId;
    private LocalDateTime date;
    private Integer duration;
    private BigDecimal price;
    private Integer capacity;
    private Integer expectedPublic;
    private EventStatus status;
    @Builder.Default
    private List<String> reviews = new ArrayList<>();
    private LocalDateTime registrationDate;
}
