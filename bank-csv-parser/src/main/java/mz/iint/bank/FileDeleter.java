package mz.iint.bank;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class FileDeleter {
    private final String filePath;

    public FileDeleter(String filePath) {
        this.filePath = filePath;
    }

    public void deleteFile() {
        File outFile = new File(filePath);
        try {
            Files.delete(outFile.toPath());
        } catch (IOException e) { }
    }
}
