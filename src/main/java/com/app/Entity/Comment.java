package com.app.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Comment implements Serializable {
    private Long id;
    private Long userId;
    private Long movieId;
    private String content;
}
