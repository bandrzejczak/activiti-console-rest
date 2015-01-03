package pl.edu.pw.ii.bpmConsole.test;

import com.google.common.io.Resources;

import java.io.IOException;
import java.util.Base64;

public class Base64Resource {

    private final byte[] resourceBytes;

    public Base64Resource(String name) throws IOException {
        resourceBytes = Resources
                .asByteSource(Resources.getResource(name))
                .read();
    }

    public String toString() {
        return Base64
                .getEncoder()
                .encodeToString(resourceBytes);
    }

}
