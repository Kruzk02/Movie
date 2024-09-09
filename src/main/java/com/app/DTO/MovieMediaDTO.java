package com.app.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieMediaDTO {
    private Long movieId;
    private byte episodes;
    private LocalTime duration;
    private String quality;
    private String video;
}
