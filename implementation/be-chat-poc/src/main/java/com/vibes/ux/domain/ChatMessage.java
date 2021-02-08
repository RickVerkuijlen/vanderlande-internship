package com.vibes.ux.domain;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class ChatMessage {
    private String sender;
    private String message;
    private Timestamp timestamp;
}
