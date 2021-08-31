package com.orangeblue.springbootaws.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.orangeblue.springbootaws.domain.posts.Posts;
import com.orangeblue.springbootaws.domain.posts.PostsRepository;
import com.orangeblue.springbootaws.web.dto.PostsSaveRequestDto;
import com.orangeblue.springbootaws.web.dto.PostsUpdateRequestDto;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostsApiControllerTest {
    
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PostsRepository postsRepository;

    @AfterEach
    public void tearDown() throws Exception {
        postsRepository.deleteAll();
    }


    @Test
    public void Posts_등록된다() throws Exception {

        // GIVEN
        String title = "test title";
        String content = "test content";
        String author = "test author";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder().title(title).content(content).author(author)
                .build();

        String url = "http://localhost:" + port + "/api/v1/posts";

        // WHEN

        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, requestDto, Long.class);

        // THEN

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> all = postsRepository.findAll();
        assertThat(all.get(0).getTitle()).isEqualTo(title);
        assertThat(all.get(0).getContent()).isEqualTo(content);
        assertThat(all.get(0).getAuthor()).isEqualTo(author);

    }
    

    @Test
    public void posts_수정된다() {
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

        ResponseEntity<Long> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Long.class);
                

        // THEN

        assertThat(responseEntity.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> findAll = postsRepository.findAll();

        assertThat(findAll.get(0).getTitle()).isEqualTo(expectedTitle);
        assertThat(findAll.get(0).getContent()).isEqualTo(expectedContent);

    }

}
    