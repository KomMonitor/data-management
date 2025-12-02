package de.hsbo.kommonitor.datamanagement.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class TempFileInputStream extends FileInputStream {
    private final File file;

    public TempFileInputStream(File file) throws FileNotFoundException {
        super(file);
        this.file = file;
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (file.exists()) {
            file.delete();
        }
    }
}
