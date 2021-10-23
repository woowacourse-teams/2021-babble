package gg.babble.babble.service;

import gg.babble.babble.domain.post.Category;
import gg.babble.babble.domain.post.Post;
import gg.babble.babble.domain.post.PostSearchType;
import gg.babble.babble.domain.repository.PostRepository;
import gg.babble.babble.dto.request.post.PostCreateRequest;
import gg.babble.babble.dto.request.post.PostDeleteRequest;
import gg.babble.babble.dto.request.post.PostUpdateRequest;
import gg.babble.babble.dto.response.PostBaseResponse;
import gg.babble.babble.exception.BabbleNotFoundException;
import java.util.List;
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
    public PostBaseResponse create(final PostCreateRequest request) {
        Post post = request.toEntity();
        postRepository.save(post);

        return PostBaseResponse.from(post);
    }

    @Transactional
    public PostBaseResponse like(final Long id) {
        Post post = find(id);
        post.addLike();

        return PostBaseResponse.from(post);
    }

    private Post find(final Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new BabbleNotFoundException(String.format("[%s]번 게시글은 존재하지 않습니다.", id)));
    }

    public PostBaseResponse search(final String type, final String keyword) {
        PostSearchType postSearchType = PostSearchType.getPostSearchTypeByName(type);

        List<Post> posts = postSearchType.compose(postRepository, keyword);

        return PostBaseResponse.of(posts, keyword, type);
    }

    @Transactional
    public PostBaseResponse findByIdAndIncreaseView(final Long id) {
        Post post = find(id);
        post.addView();

        return PostBaseResponse.from(post);
    }

    public PostBaseResponse findByCategory(final String name) {
        Category category = Category.getCategoryByName(name);
        List<Post> posts = postRepository.findByCategory(category);

        return PostBaseResponse.from(posts);
    }

    public PostBaseResponse findAll() {
        List<Post> posts = postRepository.findAll();

        return PostBaseResponse.from(posts);
    }

    @Transactional
    public PostBaseResponse update(final PostUpdateRequest request) {
        Post post = find(request.getId());
        post.update(request.getTitle(), request.getContent(), request.getCategory(), request.getPassword());

        return PostBaseResponse.from(post);
    }

    @Transactional
    public void delete(final PostDeleteRequest request) {
        Post post = find(request.getId());
        post.validatePassword(request.getPassword());
        postRepository.delete(post);
    }
}
