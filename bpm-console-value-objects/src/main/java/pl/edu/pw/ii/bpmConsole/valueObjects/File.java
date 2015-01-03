package pl.edu.pw.ii.bpmConsole.valueObjects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.ZipInputStream;

public class File {
    public Long filesize;
    public String filename;
    public String base64;
    @JsonIgnore
    private byte[] content;

    public File() {
    }

    public File(byte[] content) {
        this.content = content;
    }

    public byte[] getContent() {
        if (content == null)
            decode();
        return content;
    }

    private void decode() {
        content = Base64.getDecoder().decode(base64);
    }

    @JsonIgnore
    public boolean isZip() {
        try {
            return createZipInputStream().getNextEntry() != null;
        } catch (IOException e) {
            return false;
        }
    }

    private ZipInputStream createZipInputStream() {
        return new ZipInputStream(new ByteArrayInputStream(getContent()));
    }
}
