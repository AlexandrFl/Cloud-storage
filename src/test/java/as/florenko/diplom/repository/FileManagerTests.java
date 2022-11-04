package as.florenko.diplom.repository;

import as.florenko.diplom.model.DownloadFile;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@RequiredArgsConstructor
public class FileManagerTests {
    private final FileManager fileManager = new FileManager();
    private final String prefixPath = "D:\\Java Project\\DIPLOM\\Back\\newdiplo\\downloads\\";
    private final byte[] text = "Hello".getBytes();
    private final DownloadFile downloadFile = DownloadFile.builder()
            .path(prefixPath + "getName.txt")
            .filename("getName.txt")
            .build();
    private static final Date DATE = new Date();

    @BeforeEach
    public void start() {
        System.out.println("\nSTART TESTING" + " " + DATE);
    }

    @AfterEach
    public void end() {
        System.out.println("END TESTING" + " " + DATE);
    }

    @Test
    void createFileTest() {
        System.out.println("ТЕСТ СОЗДАНИЯ ФАЙЛА");
        fileManager.createFile("createName.txt", text);
        File file = new File(prefixPath + "createName.txt");
        Assertions.assertTrue(file.exists());
        System.out.println("Файл создан" + " " + DATE);
        Assertions.assertTrue(file.delete());
        System.out.println("Файл удален" + " " + DATE);
    }

    @Test
    void changeNameTest() {
        System.out.println("ТЕСТ ПЕРЕИМЕНОВАНИЯ ФАЙЛА");
        File oldFile = new File(prefixPath + "oldName.txt");
        try (FileOutputStream fos = new FileOutputStream(oldFile)) {
            fos.write(text, 0, text.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File newFile = new File(prefixPath + "newName.txt");

        System.out.println("Файл создан с именем - " + oldFile.getName() + " " + DATE);
        Assertions.assertTrue(fileManager.changeName("newName.txt", prefixPath + "oldName.txt"));
        System.out.println("Файл переименован. Новое имя - " + newFile.getName() + " " + DATE);
        Assertions.assertTrue(newFile.delete());
        System.out.println("Файл удален " + " " + DATE);
    }

    @Test
    void getFileTest() throws IOException {
        System.out.println("ТЕСТ СКАЧИВАНИЯ ФАЙЛА");
        File getFile = new File(prefixPath + "getName.txt");
        try (FileOutputStream fos = new FileOutputStream(getFile)) {
            fos.write(text, 0, text.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertArrayEquals(text, fileManager.getFile(downloadFile));
        System.out.println("Скаченный файл равен первоначальному");
        Assertions.assertTrue(getFile.delete());
        System.out.println("Файл удален " + " " + DATE);
    }

    @Test
    void deleteFileTest() {
        System.out.println("ТЕСТ УДАЛЕНИЯ ФАЙЛА");
        File notDeleteFIle = new File(prefixPath + "deleteName.txt");
        try (FileOutputStream fos = new FileOutputStream(notDeleteFIle)) {
            fos.write(text, 0, text.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertTrue(notDeleteFIle.exists());
        System.out.println("Файл для удаления создан" + " " + DATE);

        String deleted = "(deleted)";
        File deletedFile = new File(prefixPath + deleted + "deleteName.txt");
        Assertions.assertTrue(fileManager.delete("deleteName.txt", notDeleteFIle.getPath()));
        System.out.println("Файл отмечен в БД как удаленный " + " " + DATE);
        Assertions.assertTrue(deletedFile.delete());
    }
}

