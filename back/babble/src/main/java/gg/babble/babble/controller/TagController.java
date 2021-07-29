package gg.babble.babble.controller;

import gg.babble.babble.dto.response.TagResponse;
import gg.babble.babble.service.TagService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
        return ResponseEntity.ok(tagService.getAllTags());
    }
}