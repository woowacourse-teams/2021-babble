package gg.babble.babble.domain.image;

import java.awt.image.BufferedImage;

public class ImageSize {

    private final int width;
    private final int height;

    public ImageSize(final int width, final int height) {
        this.width = width;
        this.height = height;
    }

    public static ImageSize of(final BufferedImage image) {
        return new ImageSize(image.getWidth(), image.getHeight());
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ImageSize multiply(final double ratio) {
        return new ImageSize((int) ((double) width * ratio), (int) ((double) height * ratio));
    }
}
