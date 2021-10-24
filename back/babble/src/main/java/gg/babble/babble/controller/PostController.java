package gg.babble.babble.controller;

import gg.babble.babble.dto.request.post.PostCreateRequest;
import gg.babble.babble.dto.request.post.PostDeleteRequest;
import gg.babble.babble.dto.request.post.PostUpdateRequest;
import gg.babble.babble.dto.response.PostBaseResponse;
import gg.babble.babble.dto.response.PostResponse;
import gg.babble.babble.dto.response.PostSearchResponse;
import gg.babble.babble.service.PostService;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/post")
@RestController
public class PostController {

    private final PostService postService;

    public PostController(final PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponse> post(@RequestBody final PostCreateRequest request) {
        PostBaseResponse response = postService.create(request);
        PostResponse postResponse = response.toPostResponse();

        return ResponseEntity.created(URI.create(String.format("api/post/%s", postResponse.getId())))
            .body(postResponse);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> read(@PathVariable final Long postId) {
        PostBaseResponse response = postService.findByIdAndIncreaseView(postId);
        return ResponseEntity.ok(response.toPostResponse());
    }

    @GetMapping(path = "/search")
    public ResponseEntity<PostSearchResponse> search(@RequestParam(value = "type") final String type, @RequestParam(value = "keyword") final String keyword)
        throws UnsupportedEncodingException {
        PostBaseResponse response = postService.search(URLDecoder.decode(type, "UTF-8"), URLDecoder.decode(keyword, "UTF-8"));
        return ResponseEntity.ok(response.toPostSearchResponse());
    }

    @GetMapping(path = "/category")
    public ResponseEntity<List<PostResponse>> searchByCategory(@RequestParam(value = "value") final String category) throws UnsupportedEncodingException {
        PostBaseResponse response = postService.findByCategory(URLDecoder.decode(category, "UTF-8"));
        return ResponseEntity.ok(response.toPostResponses());
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> readAll(final Pageable pageable) {
        PostBaseResponse response = postService.findAllWithPagination(pageable);
        return ResponseEntity.ok(response.toPostResponses());
    }

    @PutMapping
    public ResponseEntity<PostResponse> update(@RequestBody final PostUpdateRequest request) {
        PostBaseResponse response = postService.update(request);
        return ResponseEntity.ok(response.toPostResponse());
    }

    @PatchMapping("/{postId}/like")
    public ResponseEntity<PostResponse> like(@PathVariable final Long postId) {
        PostBaseResponse response = postService.increaseLike(postId);
        PostResponse postResponse = response.toPostResponse();

        return ResponseEntity.ok(postResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody final PostDeleteRequest request) {
        postService.delete(request);
        return ResponseEntity.noContent().build();
    }
}
