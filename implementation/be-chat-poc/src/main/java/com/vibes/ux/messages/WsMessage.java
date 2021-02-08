package com.vibes.ux.messages;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Builder
public class WsMessage {
    private String content;
    private MessageType messageType;
    @Builder.Default private Timestamp timestamp = new Timestamp(System.currentTimeMillis());
}
