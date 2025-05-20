package com.turbolessons.messageservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Msg {

    @Id
    private String id;

    private String sender;

    private String recipient;

    private String msg;

    private String timestamp;
}