package gg.babble.babble.controller;

import gg.babble.babble.service.ImageService;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/api/images")
@RestController
public class ImageController {

    private final ImageService imageService;

    public ImageController(final ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<Set<String>> findAllImages() {
        return ResponseEntity.ok(imageService.findAllImages());
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Set<String>> saveImage(@RequestParam("file") final MultipartFile file, @RequestParam("fileName") String fileName) {
        return ResponseEntity.ok(imageService.saveImage(file, fileName));
    }
}
