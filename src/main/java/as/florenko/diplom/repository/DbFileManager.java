package as.florenko.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import as.florenko.diplom.model.DownloadFile;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DbFileManager extends JpaRepository<DownloadFile, Long> {
    DownloadFile findByFilenameAndDeleted(String filename, int deleted);
    List<DownloadFile> findAllByDeleted(int deleted);
}
