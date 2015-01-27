package pl.edu.pw.ii.bpmConsole.valueObjects;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Rights {
    NONE(false, false, false),
    CLAIM(true, false, false),
    WRITE(true, true, false),
    UNCLAIM(true, true, true);

    private final Boolean read;
    private final Boolean write;
    private final Boolean unclaim;

    Rights(Boolean read, Boolean write, Boolean unclaim) {
        this.read = read;
        this.write = write;
        this.unclaim = unclaim;
    }

    @JsonProperty("read")
    public Boolean canRead() {
        return read;
    }

    @JsonProperty("write")
    public Boolean canWrite() {
        return write;
    }

    @JsonProperty("unclaim")
    public Boolean canUnclaim() {
        return unclaim;
    }
}
