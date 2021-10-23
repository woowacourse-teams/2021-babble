package gg.babble.babble.service;

import gg.babble.babble.domain.post.Post;
import gg.babble.babble.domain.post.PostSearchType;
import gg.babble.babble.domain.post.Category;
import gg.babble.babble.domain.repository.PostRepository;
import gg.babble.babble.dto.request.post.PostCreateRequest;
import gg.babble.babble.dto.request.post.PostDeleteRequest;
import gg.babble.babble.dto.request.post.PostUpdateRequest;
import gg.babble.babble.dto.response.PostResponse;
import gg.babble.babble.dto.response.PostSearchResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @Transactional
    public PostResponse create(final PostCreateRequest request) {
        Post post = request.toEntity();
        postRepository.save(post);

        return PostResponse.from(post);
    }

    @Transactional
    public PostResponse like(final Long id) {
        Post post = find(id);
        post.addLike();

        return PostResponse.from(post);
    }

    private Post find(final Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new BabbleNotFoundException(String.format("[%s]번 게시글은 존재하지 않습니다.", id)));
    }

    public PostSearchResponse search(final String type, final String keyword) {
        PostSearchType postSearchType = PostSearchType.from(type);

        List<Post> posts = postSearchType.compose(postRepository, keyword);

        List<PostResponse> result = posts.stream()
            .map(PostResponse::from)
            .collect(Collectors.toList());

        return new PostSearchResponse(result, keyword, type);
    }

    @Transactional
    public PostResponse findById(final Long id) {
        Post post = find(id);
        post.addView();

        return PostResponse.from(post);
    }

    public List<PostResponse> findByCategory(final String name) {
        Category category = Category.from(name);
        List<Post> posts = postRepository.findByCategory(category);

        return posts.stream()
            .map(PostResponse::from)
            .collect(Collectors.toList());
    }

    public List<PostResponse> findAll() {
        List<Post> posts = postRepository.findAll();

        return posts.stream()
            .map(PostResponse::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse update(final PostUpdateRequest request) {
        Post post = find(request.getId());
        post.update(request.getTitle(), request.getContent(), request.getCategory(), request.getPassword());

        return PostResponse.from(post);
    }

    @Transactional
    public void delete(final PostDeleteRequest request) {
        Post post = find(request.getId());
        post.delete(request.getPassword());
        postRepository.delete(post);
    }
}
