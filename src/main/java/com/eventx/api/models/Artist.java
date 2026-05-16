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
@Document(collection = "artists")
public class Artist {

    /** L'ID corrisponde all'ID dell'utente (1:1) */
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String alias;
    private String piva;
    private String phone;
    @Builder.Default
    private List<String> activeEvents = new ArrayList<>();
    @Builder.Default
    private List<String> lastEvents = new ArrayList<>();
    @Builder.Default
    private List<String> reviews = new ArrayList<>();
    private LocalDateTime registrationDate;
}

