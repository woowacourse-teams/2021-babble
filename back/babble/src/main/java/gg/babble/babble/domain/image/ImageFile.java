package gg.babble.babble.domain.image;

import gg.babble.babble.domain.FileName;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ImageFile {

    private final FileName name;
    private final byte[] data;

}
