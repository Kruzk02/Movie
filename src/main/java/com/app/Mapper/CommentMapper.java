package com.app.Mapper;

import com.app.DTO.CommentDTO;
import com.app.Entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommentMapper {
    CommentMapper INSTANCE = Mappers.getMapper(CommentMapper.class);

    @Mapping(source = "content",target = "content")
    @Mapping(source = "userId",target = "userId")
    @Mapping(source = "movieId",target = "movieId")
    Comment mapCommentDTOToComment(CommentDTO commentDTO);
}
