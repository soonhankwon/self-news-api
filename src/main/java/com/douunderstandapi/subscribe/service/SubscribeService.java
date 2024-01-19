package com.douunderstandapi.subscribe.service;

import com.douunderstandapi.common.enumtype.ErrorCode;
import com.douunderstandapi.common.exception.CustomException;
import com.douunderstandapi.post.domain.Post;
import com.douunderstandapi.post.dto.PostDTO;
import com.douunderstandapi.post.repository.PostRepository;
import com.douunderstandapi.subscribe.domain.Subscribe;
import com.douunderstandapi.subscribe.dto.response.SubscribeAddResponse;
import com.douunderstandapi.subscribe.dto.response.SubscribeCancelResponse;
import com.douunderstandapi.subscribe.dto.response.SubscribePostsGetResponse;
import com.douunderstandapi.subscribe.repository.SubscribeRepository;
import com.douunderstandapi.user.domain.User;
import com.douunderstandapi.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscribeService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final SubscribeRepository subscribeRepository;

    public SubscribePostsGetResponse getSubscribePosts(String email, int pageNumber) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Pageable pageable = PageRequest.of(pageNumber, 10);
        Page<Subscribe> page = subscribeRepository.findAllByUser(user, pageable);

        List<PostDTO> postDTOS = page.getContent()
                .stream()
                .map(Subscribe::getPost)
                .map(p -> PostDTO.of(p, true))
                .collect(Collectors.toList());

        int totalPages = page.getTotalPages();
        return new SubscribePostsGetResponse(totalPages, postDTOS);
    }

    @Transactional
    public SubscribeAddResponse addSubscribe(String email, Long postId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_POST_ID));

        Optional<Subscribe> optionalSubscribe = subscribeRepository.findByUserAndPost(user, post);
        if (optionalSubscribe.isPresent()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.DUPLICATED_SUBSCRIBE);
        } else {
            Subscribe subscribe = new Subscribe(user, post);
            subscribeRepository.save(subscribe);
        }
        return new SubscribeAddResponse(true);
    }

    @Transactional
    public SubscribeCancelResponse cancelSubscribe(String email, Long postId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_USER_EMAIL));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.NOT_EXIST_POST_ID));

        subscribeRepository.deleteByPostAndUser(post, user);
        return new SubscribeCancelResponse(true);
    }
}