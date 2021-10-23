package gg.babble.babble.controller;

import gg.babble.babble.dto.request.post.PostCreateRequest;
import gg.babble.babble.dto.request.post.PostDeleteRequest;
import gg.babble.babble.dto.request.post.PostUpdateRequest;
import gg.babble.babble.dto.response.PostResponse;
import gg.babble.babble.dto.response.PostSearchResponse;
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
        PostResponse response = postService.create(request);
        return ResponseEntity.created(URI.create(String.format("api/post/%s", response.getId())))
            .body(response);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> read(@PathVariable final Long postId) {
        PostResponse response = postService.findById(postId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search/args")
    public ResponseEntity<PostSearchResponse> search(@RequestParam(value = "type") final String type, @RequestParam(value = "keyword") final String keyword) {
        PostSearchResponse response = postService.search(type, keyword);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/args")
    public ResponseEntity<List<PostResponse>> searchByCategory(@RequestParam final String category) {
        List<PostResponse> response = postService.findByCategory(category);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<PostResponse>> readAll() {
        List<PostResponse> responses = postService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PutMapping
    public ResponseEntity<PostResponse> update(@RequestBody final PostUpdateRequest request) {
        PostResponse response = postService.update(request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{postId}/like")
    public ResponseEntity<PostResponse> like(@PathVariable final Long postId) {
        PostResponse response = postService.like(postId);
        return ResponseEntity.created(URI.create(String.format("api/post/%s", response.getId())))
            .body(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody final PostDeleteRequest request) {
        postService.delete(request);
        return ResponseEntity.noContent().build();
    }
}
