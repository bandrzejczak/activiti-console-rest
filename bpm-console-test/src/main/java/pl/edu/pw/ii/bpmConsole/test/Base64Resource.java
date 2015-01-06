package pl.edu.pw.ii.bpmConsole.test;

import com.google.common.io.Resources;
import pl.edu.pw.ii.bpmConsole.valueObjects.File;

import java.io.IOException;
import java.util.Base64;

public class Base64Resource {

    private final byte[] resourceBytes;
    private final String name;

    public Base64Resource(String name) throws IOException {
        this.name = name;
        resourceBytes = Resources
                .asByteSource(Resources.getResource(name))
                .read();
    }

    public String toString() {
        return Base64
                .getEncoder()
                .encodeToString(resourceBytes);
    }

    public File toFile() {
        File file = new File();
        file.fileName = stripFromPath(name);
        file.fileSize = (long) resourceBytes.length;
        file.base64 = toString();
        return file;
    }

    private String stripFromPath(String fileName) {
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

}
