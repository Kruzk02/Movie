package com.app.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MovieEvent {

    private Long movieId;
    private EventType eventType;
    private Instant createAt;
}
