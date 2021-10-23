package gg.babble.babble.controller;

import gg.babble.babble.dto.request.post.PostCreateRequest;
import gg.babble.babble.dto.request.post.PostDeleteRequest;
import gg.babble.babble.dto.request.post.PostUpdateRequest;
import gg.babble.babble.dto.response.PostResponse;
import gg.babble.babble.dto.response.PostBaseResponse;
import gg.babble.babble.service.PostService;
import java.net.URI;
import java.util.List;
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

    @GetMapping("/search/args")
    public ResponseEntity<PostBaseResponse> search(@RequestParam(value = "type") final String type, @RequestParam(value = "keyword") final String keyword) {
        PostBaseResponse response = postService.search(type, keyword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/args")
    public ResponseEntity<List<PostResponse>> searchByCategory(@RequestParam final String category) {
        PostBaseResponse response = postService.findByCategory(category);
        return ResponseEntity.ok(response.toPostResponses());
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> readAll() {
        PostBaseResponse response = postService.findAll();
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

        return ResponseEntity.created(URI.create(String.format("api/post/%s", postResponse.getId())))
            .body(response.toPostResponse());
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody final PostDeleteRequest request) {
        postService.delete(request);
        return ResponseEntity.noContent().build();
    }
}
