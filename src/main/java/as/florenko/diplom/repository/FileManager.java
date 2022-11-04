package as.florenko.diplom.repository;

import as.florenko.diplom.model.DownloadFile;
import lombok.Getter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Getter
public class FileManager {
    private final String prefixPath = "D:\\Java Project\\DIPLOM\\Back\\newdiplo\\downloads\\";
    private final String deleted = "(deleted)";
    public void createFile(String filename, byte[] bytes) {
        File file = new File(prefixPath + filename);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean changeName(String newFilename, String path) {
        File oldFile = new File(path);
        if (!oldFile.exists()) {
            return false;
        }
        File file = new File(prefixPath + newFilename);
        return oldFile.renameTo(file);
    }

    public byte[] getFile(DownloadFile file) throws IOException {
        Path path = Path.of(file.getPath());
        return Files.readAllBytes(path);
    }

    public boolean delete(String filename, String path) {
        File oldFile = new File(path);
        if (!oldFile.exists()) {
            return false;
        }
        File file = new File(prefixPath + deleted + filename);
        return oldFile.renameTo(file);
    }
}
