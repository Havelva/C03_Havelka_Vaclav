package graphics.utils;

import java.io.InputStream;

public class Resources {

    public static InputStream loadFile(String fileName, ResourceType resourceType) {
        switch (resourceType) {
            case TrueTypeFont:
                return Resources.class.getResourceAsStream(String.format("/fonts/%s%s", fileName, resourceType.getType()));
            case Objects:
            case Materials:
                return Resources.class.getResourceAsStream(String.format("/models/%s%s", fileName, resourceType.getType()));
            case Icon:
                return Resources.class.getResourceAsStream(String.format("/images/%s%s", fileName, resourceType.getType()));
            default:
                return Resources.class.getResourceAsStream(fileName);
        }
    }
}
