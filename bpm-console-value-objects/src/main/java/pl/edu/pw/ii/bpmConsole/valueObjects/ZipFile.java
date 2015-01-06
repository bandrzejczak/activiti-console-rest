package pl.edu.pw.ii.bpmConsole.valueObjects;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipFile implements Iterable<File> {

    private final File file;
    private List<File> filesInsideZip;

    public ZipFile(File file) {
        this.file = file;
        if (!file.isZip())
            throw new FileNotZippedException();
    }

    private void unzip() {
        filesInsideZip = new ArrayList<>();
        try (
                ByteArrayInputStream contentStream = new ByteArrayInputStream(file.getContent());
                ZipInputStream zipInputStream = new ZipInputStream(contentStream)
        ) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory())
                    filesInsideZip.add(createFile(entry, zipInputStream));
            }
        } catch (IOException e) {
            throw new UnzippingException(e);
        }
    }

    private File createFile(ZipEntry entry, ZipInputStream zipInputStream) throws IOException {
        File file = new File(IOUtils.toByteArray(zipInputStream));
        file.fileName = stripFromPath(entry.getName());
        file.fileSize = entry.getSize();
        return file;
    }

    private String stripFromPath(String fileName) {
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    @Override
    public Iterator<File> iterator() {
        unzipIfNecessary();
        return filesInsideZip.iterator();
    }

    @Override
    public void forEach(Consumer<? super File> action) {
        unzipIfNecessary();
        filesInsideZip.forEach(action);
    }

    @Override
    public Spliterator<File> spliterator() {
        unzipIfNecessary();
        return filesInsideZip.spliterator();
    }

    public Stream<File> stream(){
        return StreamSupport.stream(spliterator(), false);
    }

    private void unzipIfNecessary() {
        if (filesInsideZip == null)
            unzip();
    }
}
