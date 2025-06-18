package module.movie.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table("movie")
public class Movie implements Serializable {
    @Id
    private Long id;
    @Column("title")
    private String title;
    @Column("release_year")
    private LocalDate releaseYear;
    @Column("description")
    private String description;
    @Column("seasons")
    private byte seasons;
    @Column("poster")
    private String poster;
}
