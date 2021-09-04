package com.orangeblue.springbootaws.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orangeblue.springbootaws.domain.posts.Posts;
import com.orangeblue.springbootaws.domain.posts.PostsRepository;
import com.orangeblue.springbootaws.web.dto.PostsSaveRequestDto;
import com.orangeblue.springbootaws.web.dto.PostsUpdateRequestDto;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @AfterEach
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }


    @Test
    @WithMockUser(roles = "USER")
    public void Posts_등록된다() throws Exception {

        // GIVEN
        String title = "test title";
        String content = "test content";
        String author = "test author";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder().title(title).content(content).author(author)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        // WHEN

        mvc.perform(post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(requestDto)))
                    .andExpect(status().isOk());

        
        
               

        // THEN
        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
        assertThat(all.get(0).getAuthor()).isEqualTo(author);

    }
    

    @Test
    @WithMockUser(roles = "USER")
    public void posts_수정된다() throws JsonProcessingException, Exception {
        // GIVEN 

        String title = "test title";
        String content = "test content";
        String author = "test author";

        Posts savedPost = postsRepository.save(Posts.builder()
                                    .title(title)
                                    .content(content)
                                    .author(author)
                .build());


        Long updateId = savedPost.getId();
        String expectedTitle = "update title";
        String expectedContent = "update content";

        PostsUpdateRequestDto requestDto = PostsUpdateRequestDto.builder()
                                .title(expectedTitle)
                                .content(expectedContent)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts/" + updateId;

        HttpEntity<PostsUpdateRequestDto> requestEntity = new HttpEntity<>(requestDto);

        // WHEN

        mvc.perform(MockMvcRequestBuilders.put(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());
                

        // THEN

        List<Posts> findAll = postsRepository.findAll();
        assertThat(findAll.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(findAll.get(0).getContent()).isEqualTo(expectedContent);

    }

}
    