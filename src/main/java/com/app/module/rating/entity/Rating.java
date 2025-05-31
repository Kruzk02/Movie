package com.app.module.rating.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("ratings")
public class Rating {
    @Id
    private Long id;
    @Column("user_id")
    private Long userId;
    @Column("movie_id")
    private Long movieId;
    @Column("rating")
    private Double rating;
}
