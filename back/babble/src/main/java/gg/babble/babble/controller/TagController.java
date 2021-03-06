package gg.babble.babble.controller;

import gg.babble.babble.dto.request.TagCreateRequest;
import gg.babble.babble.dto.request.TagUpdateRequest;
import gg.babble.babble.dto.response.TagResponse;
import gg.babble.babble.service.TagService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/tags")
@RestController
public class TagController {

    private final TagService tagService;

    public TagController(final TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        return ResponseEntity.ok(tagService.findAll());
    }

    @GetMapping("/{tagId}")
    public ResponseEntity<TagResponse> getTag(@PathVariable final Long tagId) {
        return ResponseEntity.ok(tagService.findById(tagId));
    }

    @PostMapping
    public ResponseEntity<TagResponse> createTag(@Valid @RequestBody final TagCreateRequest request) {
        TagResponse response = tagService.createTag(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "/{tagId}")
    public ResponseEntity<TagResponse> updateTag(@PathVariable final Long tagId,
                                                 @Valid @RequestBody final TagUpdateRequest request) {
        TagResponse response = tagService.updateTag(tagId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable final Long tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.noContent().build();
    }
}
