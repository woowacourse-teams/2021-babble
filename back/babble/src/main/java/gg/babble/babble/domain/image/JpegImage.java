package gg.babble.babble.domain.image;

import gg.babble.babble.exception.BabbleIOException;
import gg.babble.babble.exception.BabbleIllegalArgumentException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageOutputStream;

public class JpegImage {

    public static final String EXTENSION = "jpg";

    private static final float COMPRESSION_QUALITY = 1.0f;
    private static final IIOMetadata EMPTY_STREAM_METADATA = null;
    private static final List<? extends BufferedImage> EMPTY_THUMBNAILS = null;
    private static final IIOMetadata EMPTY_METADATA = null;

    private final BufferedImage bufferedImage;

    private JpegImage(final BufferedImage bufferedImage) {
        this.bufferedImage = bufferedImage;
    }

    public static JpegImage of(final byte[] image) {
        try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(image);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(byteArrayInputStream)) {
            BufferedImage targetImage = ImageIO.read(bufferedInputStream);
            validateBufferedImage(targetImage);

            return redrawForJpeg(targetImage.getWidth(), targetImage.getHeight(), targetImage);
        } catch (IOException ioException) {
            throw new BabbleIOException("이미지 입출력 중에 오류가 발생했습니다.");
        }
    }

    private static JpegImage redrawForJpeg(final int width, final int height, final Image image) {
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(image, 0, 0, null);

        return new JpegImage(outputImage);
    }

    private static void validateBufferedImage(final BufferedImage bufferedImage) {
        if (Objects.isNull(bufferedImage)) {
            throw new BabbleIllegalArgumentException("이미지 형식의 파일이 아닙니다.");
        }
    }

    public JpegImage resize(final ImageSize imageSize) {
        Image resultingImage = bufferedImage.getScaledInstance(imageSize.getWidth(), imageSize.getHeight(), Image.SCALE_SMOOTH);
        return redrawForJpeg(imageSize.getWidth(), imageSize.getHeight(), resultingImage);
    }

    public byte[] compress() {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(byteArrayOutputStream)) {

            ImageWriter writer = getImageWriter(imageOutputStream);
            ImageWriteParam param = getCompressionParam(writer);

            writer.write(EMPTY_STREAM_METADATA, new IIOImage(bufferedImage, EMPTY_THUMBNAILS, EMPTY_METADATA), param);
            writer.dispose();

            return byteArrayOutputStream.toByteArray();
        } catch (IOException ioException) {
            throw new BabbleIOException("이미지 입출력 중에 오류가 발생했습니다.");
        }
    }

    private ImageWriter getImageWriter(final ImageOutputStream imageOutputStream) {
        Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(EXTENSION);
        if (!writers.hasNext()) {
            throw new BabbleIllegalArgumentException(String.format("No writers found for (%s)", EXTENSION));
        }
        ImageWriter writer = writers.next();
        writer.setOutput(imageOutputStream);
        return writer;
    }

    private ImageWriteParam getCompressionParam(final ImageWriter writer) {
        ImageWriteParam param = writer.getDefaultWriteParam();

        if (param.canWriteCompressed()) {
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(COMPRESSION_QUALITY);
            param.setProgressiveMode(ImageWriteParam.MODE_DEFAULT);
        }
        return param;
    }

    public int getWidth() {
        return bufferedImage.getWidth();
    }

    public int getHeight() {
        return bufferedImage.getHeight();
    }
}
