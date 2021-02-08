package com.vibes.ux.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Data
@Builder
public class User {
    @Builder.Default
    @Nullable
    private UUID id = UUID.randomUUID();
    private String name;
}
