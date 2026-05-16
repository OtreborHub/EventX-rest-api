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
@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String username;
    private String email;
    private String passwordHash;
    private String firstName;
    private String lastName;
    private String phone;
    private String paypalEmail;
    private boolean isArtist;
    private boolean isLocation;
    @Builder.Default
    private List<String> activeTickets = new ArrayList<>();
    @Builder.Default
    private List<String> lastTickets = new ArrayList<>();
    @Builder.Default
    private List<String> reviews = new ArrayList<>();
    private LocalDateTime registrationDate;
}
