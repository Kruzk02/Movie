package module.movie.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("movie_media")
public class MovieMedia implements Serializable {
    @Id
    private Long id;
    @Column("movie_id")
    private Long movieId;
    @Column("file_path")
    private String filePath;
    @Column("episodes")
    private byte episodes;
    @Column("duration")
    private LocalTime duration;
    @Column("quality")
    private String quality;
}
