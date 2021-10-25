package gg.babble.babble.controller.beta;

import gg.babble.babble.dto.response.TagNameResponse;
import gg.babble.babble.service.TagService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/beta/tags")
@RestController
public class TagBetaController {

    private final TagService tagService;

    public TagBetaController(final TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/names")
    public ResponseEntity<List<TagNameResponse>> findTagNames(@RequestParam(defaultValue = "") final String keyword) {
        return ResponseEntity.ok(tagService.findTagNames(keyword));
    }
}
