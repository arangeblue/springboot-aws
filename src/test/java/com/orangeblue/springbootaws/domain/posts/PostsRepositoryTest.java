package com.orangeblue.springbootaws.domain.posts;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;    
    
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @AfterEach
    public void cleanup() {
        postsRepository.deleteAll();
    }


    @Test
    public void 게시글저장_불러오기() {

        //GIVEN
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postsRepository.save(Posts.builder().title(title).content(content).author("wkb1848@gmail.com").build());

        // WHEN
        List<Posts> postsList = postsRepository.findAll();

        // THEN

        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);

    }
    
    @Test
    public void BaseTimeEntity_등록() {
        // GIVEN

        LocalDateTime now = LocalDateTime.of(2019, 6, 4, 0, 0, 0);
    
        postsRepository.save(Posts.builder()
                                    .title("title")
                                    .content("content")
                                    .author("author")
                .build());

        // WHEN

        List<Posts> postsList = postsRepository.findAll();


        // THEN

        Posts posts = postsList.get(0);


        System.out.println(">>>>>>>>>>>>>>>>> createDate = " + posts.getCreatedDate() + ", modifiedDate = "
                + posts.getModifiedDate());
        
        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);


    
    }
}
    