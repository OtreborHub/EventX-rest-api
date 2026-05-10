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
@Document(collection = "tickets")
public class Ticket {

    @Id
    private String id;
    private String position;
    private String userId;
    private String eventId;
    private boolean isBlockchain;
    private boolean isWinner;
    private String hash;
    private LocalDateTime registrationDate;
}
