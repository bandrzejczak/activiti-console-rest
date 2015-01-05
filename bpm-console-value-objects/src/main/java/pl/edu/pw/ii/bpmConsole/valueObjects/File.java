package pl.edu.pw.ii.bpmConsole.valueObjects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.zip.ZipInputStream;

public class File {
    @NotNull
    public String fileName;
    @NotNull
    public Long fileSize;
    @NotNull
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
        if(base64 == null || base64.length() == 0)
            content = new byte[0];
        else
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
