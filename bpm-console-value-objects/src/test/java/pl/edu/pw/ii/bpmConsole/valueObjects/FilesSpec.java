package pl.edu.pw.ii.bpmConsole.valueObjects;

import org.junit.Test;
import pl.edu.pw.ii.bpmConsole.test.Base64Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static pl.edu.pw.ii.bpmConsole.test.AssertJThrowableAssert.assertThrown;

public class FilesSpec {

    private static final String CONTENT_ENCODED = "Y29udGVudA==";
    private static final String CONTENT_DECODED = "content";
    private static String CONTENT_ZIPPED_1;
    private static String CONTENT_ZIPPED_2;
    private static String CONTENT_ZIPPED_3;

    public FilesSpec() throws IOException {
        CONTENT_ZIPPED_1 = new Base64Resource("testZip1.zip").toString();
        CONTENT_ZIPPED_2 = new Base64Resource("testZip2.zip").toString();
        CONTENT_ZIPPED_3 = new Base64Resource("testZip3.zip").toString();
    }

    @Test
    public void shouldDecodeBase64Content() {
        //given
        File file = new File();
        file.base64 = CONTENT_ENCODED;
        //when
        byte[] content = file.getContent();
        //then
        assertThat(new String(content, StandardCharsets.UTF_8)).isEqualTo(CONTENT_DECODED);
    }

    @Test
    public void shouldDetectZipFileType() {
        //given
        File file = new File();
        file.base64 = CONTENT_ZIPPED_1;
        //when - then
        assertThat(file.isZip()).isTrue();
    }

    @Test
    public void shouldThrowExceptionIfZipFileIsNotZipped() {
        //given
        File file = new File();
        file.base64 = CONTENT_ENCODED;
        //when - then
        assertThrown(() -> new ZipFile(file))
                .isExactlyInstanceOf(FileNotZippedException.class);
    }

    @Test
    public void shouldReadFilenameAndSizeFromZipFile() {
        //given
        File file = new File();
        file.base64 = CONTENT_ZIPPED_1;
        //when
        ZipFile zipFile = new ZipFile(file);
        File fileInsideZip = zipFile.iterator().next();
        //then
        assertThat(fileInsideZip.filename).isEqualTo("document");
        assertThat(fileInsideZip.filesize).isEqualTo(0);
    }

    @Test
    public void shouldReadDeepFileStructureInsideZip() {
        //given
        File file = new File();
        file.base64 = CONTENT_ZIPPED_2;
        ZipFile zipFile = new ZipFile(file);
        //when
        Integer filesCount = 0;
        for (File fileInsideZip : zipFile)
            filesCount++;
        //then
        assertThat(filesCount).isEqualTo(3);
    }

    @Test
    public void shouldStripFileInsideZipFromItsPath() {
        //given
        File file = new File();
        file.base64 = CONTENT_ZIPPED_3;
        ZipFile zipFile = new ZipFile(file);
        //when
        File zippedFile = zipFile.iterator().next();
        //then
        assertThat(zippedFile.filename).isEqualTo("stripped");
    }

}