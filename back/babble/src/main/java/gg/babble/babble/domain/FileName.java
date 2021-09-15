package gg.babble.babble.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileName {

    private static final String FILE_EXTENSION_DELIMITER = "\\.";
    private static final int SIMPLE_NAME_INDEX = 0;
    private static final int EXTENSION_INDEX = 1;
    private static final int SPLIT_SIZE = 2;

    private final String simpleName;
    private final String extension;

    public static FileName of(final String fullName) {
        String[] simpleNameAndExtension = fullName.split(FILE_EXTENSION_DELIMITER, SPLIT_SIZE);
        if (simpleNameAndExtension.length != SPLIT_SIZE) {
            throw new IllegalArgumentException(String.format("잘못된 파일 이름입니다.(%s)", fullName));
        }

        return new FileName(simpleNameAndExtension[SIMPLE_NAME_INDEX], simpleNameAndExtension[EXTENSION_INDEX]);
    }

    @Override
    public String toString() {
        return simpleName + '.' + extension;
    }
}
