package com.orangeblue.springbootaws.web.dto;

import com.orangeblue.springbootaws.domain.posts.Posts;

import lombok.Getter;

@Getter
public class PostsResponseDto {
    
    private Long id;
    private String title;
    private String content;
    private String author;
    

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getContent();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }

}
