package module.movie.mapper;

import module.movie.dto.MovieDTO;
import module.movie.entity.Movie;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MovieMapper {

    MovieMapper INSTANCE = Mappers.getMapper(MovieMapper.class);

    @Mapping(source = "title",target = "title")
    @Mapping(source = "releaseYear",target = "releaseYear")
    @Mapping(source = "description",target = "description")
    @Mapping(source = "seasons",target = "seasons")
    @Mapping(source = "poster",target = "poster")
    Movie mapDtoToEntity(MovieDTO movieDTO);
}
