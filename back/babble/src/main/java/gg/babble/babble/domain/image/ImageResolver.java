package gg.babble.babble.domain.image;

import java.util.List;
import java.util.stream.Collectors;

public class ImageResolver {

    public static final String DEFAULT_EXTENSION = "jpg";

    private final byte[] imageData;

    public ImageResolver(final byte[] imageData) {
        this.imageData = imageData;
    }

    public List<byte[]> resizedImagesContaining(final List<Integer> maxPixels) {
        MutableImage mutableImage = MutableImage.of(imageData);
        ImageSize originalSize = ImageSize.of(mutableImage);

        return maxPixels.stream()
            .map(originalSize::calculateSizeContaining)
            .map(mutableImage::resize)
            .map(resizedImage -> resizedImage.compress(DEFAULT_EXTENSION))
            .collect(Collectors.toList());
    }
}
