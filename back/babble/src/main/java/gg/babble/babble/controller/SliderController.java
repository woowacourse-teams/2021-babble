package gg.babble.babble.controller;

import gg.babble.babble.dto.request.SliderOrderRequest;
import gg.babble.babble.dto.request.SliderRequest;
import gg.babble.babble.dto.response.SliderResponse;
import gg.babble.babble.service.SliderService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/api/sliders")
@RestController
public class SliderController {

    private final SliderService sliderService;

    public SliderController(SliderService sliderService) {
        this.sliderService = sliderService;
    }

    @GetMapping
    public ResponseEntity<List<SliderResponse>> findAllSliders() {
        List<SliderResponse> responses = sliderService.findAll();
        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<SliderResponse> insertSliders(final SliderRequest request) {
        SliderResponse response = sliderService.insert(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<List<SliderResponse>> updateOrder(final SliderOrderRequest request) {
        List<SliderResponse> sliderResponses = sliderService.updateOrder(request);
        return ResponseEntity.ok(sliderResponses);
    }

    @DeleteMapping(value = "/{sliderId}")
    public ResponseEntity<Void> delete(@PathVariable Long sliderId) {
        sliderService.delete(sliderId);
        return ResponseEntity.noContent().build();
    }
}
