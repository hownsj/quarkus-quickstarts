package com.quarkus.service.impl;

import com.quarkus.entity.PostEntity;
import com.quarkus.entity.UserEntity;
import com.quarkus.exception.BusinessLogicException;
import com.quarkus.exception.ErrorMessage;
import com.quarkus.model.Post;
import com.quarkus.repository.PostRepository;
import com.quarkus.repository.UserRepository;
import com.quarkus.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.quarkus.util.ModelMapper.mapPostEntitiesToPost;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public Long createPost(Post post, Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ErrorMessage.USER_NOT_FOUND));

        PostEntity postEntity = postRepository.save(PostEntity.builder()
                .likes(0)
                .tags(post.getTags())
                .title(post.getTitle())
                .text(post.getText())
                .userEntity(user)
                .date(new Date())
                .build());
        return postEntity.getId();
    }

    @Override
    public List<Post> getUserPosts(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new BusinessLogicException(ErrorMessage.USER_NOT_FOUND));
        List<PostEntity> postEntities = postRepository.findAllByUserEntity(userEntity);
        return mapPostEntitiesToPost(postEntities);
    }

    @Override
    public List<Post> getPostsByTitle(String title) {
        List<PostEntity> postEntities = postRepository.findAllByTitle(title);
        return mapPostEntitiesToPost(postEntities);
    }

    @Override
    public List<Post> getPostsByTags(String tags) {
        List<PostEntity> postEntities = postRepository.findAllByTags(tags);
        return mapPostEntitiesToPost(postEntities);
    }
}