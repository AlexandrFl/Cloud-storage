package as.florenko.diplom.repository;

import org.springframework.http.ResponseEntity;

import java.io.IOException;

public interface FileRepository {

    ResponseEntity getList(int limit);

    ResponseEntity addFile(String filename, long size, byte[] byteArr);

    ResponseEntity changeName(String oldName, String newName);

    ResponseEntity deleteFile(String filename);

    ResponseEntity getFile(String filename) throws IOException;
}
