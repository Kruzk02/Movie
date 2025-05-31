package com.app.module.comment.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Table("comments")
public class Comment implements Serializable {
    @Id
    private Long id;
    private Long userId;
    private Long movieId;
    private String content;
    private LocalDateTime createAt;
}
