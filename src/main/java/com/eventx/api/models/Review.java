package com.eventx.api.models;

import java.time.LocalDateTime;
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
@Document(collection = "reviews")
public class Review {

    @Id
    private String id;
    private String userId;
    private String locationId;
    private String eventId;
    private String artistId;
    private Integer vote;
    private String title;
    private String description;
    private LocalDateTime registrationDate;
}
