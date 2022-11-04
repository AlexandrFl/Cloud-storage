package as.florenko.diplom.controller;

import as.florenko.diplom.model.DownloadFile;
import as.florenko.diplom.model.FileName;
import as.florenko.diplom.repository.DbFileManager;
import as.florenko.diplom.repository.FileRepositoryImpl;
import as.florenko.diplom.repository.FileManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

//@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@RequiredArgsConstructor
public class ControllerTests {

    private final static String PREFIX_PATH = "D:\\Java Project\\DIPLOM\\Back\\newdiplo\\downloads\\";
    @Autowired
    private FileRepositoryImpl fileRepository;
    private final static int NOT_DELETED = 0;

    @BeforeEach
    void start() {
        System.out.println("--Запуск теста--");
    }

    @AfterEach
    void end() {
        System.out.println("--Тест завершен--");
    }


    @Test
    void getListTest() {
        int limit = 3;
        int wrongLimit = 0;

        // OK
        System.out.println("ТЕСТ ОТОБРАЖЕНИЯ СПИСКА ФАЙЛОВ");
        System.out.println("Тестирование OK");
        Controller controller1 = new Controller(fileRepository);
        HttpStatus testStatus1 = controller1.getList(limit).getStatusCode();
        HttpStatus status1 = HttpStatus.OK;
        System.out.println("Ожидалось получить - " + status1 + "\nПолучено - " + testStatus1);
        Assertions.assertEquals(status1, testStatus1);

        // BAD REQUEST
        System.out.println("\nТестирование BAD REQUEST");
        HttpStatus testStatus2 = controller1.getList(wrongLimit).getStatusCode();
        HttpStatus status2 = HttpStatus.BAD_REQUEST;
        System.out.println("Ожидалось получить - " + status2 + "\nПолучено - " + testStatus2);
        Assertions.assertEquals(status2, testStatus2);

        // INTERNAL_SERVER_ERROR
        System.out.println("\nТестирование INTERNAL_SERVER_ERROR");
        List list = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            DownloadFile downloadFile = DownloadFile.builder()
                    .path(PREFIX_PATH + i + ".txt")
                    .filename(i + ".txt")
                    .build();
            list.add(downloadFile);
        }
        DbFileManager dbFileManager3 = Mockito.mock(DbFileManager.class);
        Mockito.when(dbFileManager3.findAllByDeleted(NOT_DELETED)).thenReturn(list);

        FileRepositoryImpl fileRepository3 = new FileRepositoryImpl(new FileManager(), dbFileManager3);
        Controller controller3 = new Controller(fileRepository3);

        HttpStatus testStatus3 = controller3.getList(limit).getStatusCode();
        HttpStatus status3 = HttpStatus.INTERNAL_SERVER_ERROR;
        System.out.println("Ожидалось получить - " + status3 + "\nПолучено - " + testStatus3);
        Assertions.assertEquals(status3, testStatus3);
    }

    @Test
    void changeNameTest() {
        System.out.println("ТЕСТ ИЗМЕНЕНИЯ ИМЕНИ ФАЙЛА");
        String oldName = "Name.txt";
        FileName fileName = new FileName();
        String newName = "newName.txt";
        fileName.setFilename(newName);
        System.out.println("Тестирование BAD REQUEST");

        //BAD REQUEST
        DbFileManager dbFileManager1 = Mockito.mock(DbFileManager.class);
        Mockito.when(dbFileManager1.findByFilenameAndDeleted(oldName, 0)).thenReturn(null);

        FileRepositoryImpl fileRepository1 = new FileRepositoryImpl(new FileManager(), dbFileManager1);
        Controller testController1 = new Controller(fileRepository1);

        HttpStatus testStatus1 = HttpStatus.BAD_REQUEST;
        HttpStatus status1 = testController1.changeName(oldName, fileName).getStatusCode();

        System.out.println("Ожидалось получить - " + status1 + "\nПолучено - " + testStatus1);
        Assertions.assertEquals(status1, testStatus1);

        //ERROR UPLOAD FILE
        System.out.println("\nТестирование INTERNAL_SERVER_ERROR");
        DownloadFile downloadFile = DownloadFile.builder()
                .path(PREFIX_PATH + "name.txt")
                .filename("name.txt")
                .build();
        DbFileManager dbFileManager2 = Mockito.mock(DbFileManager.class);
        Mockito.when(dbFileManager2.findByFilenameAndDeleted(oldName, 0)).thenReturn(downloadFile);

        FileManager fileManager2 = Mockito.mock(FileManager.class);
        Mockito.when(fileManager2.changeName(oldName, newName)).thenReturn(false);

        FileRepositoryImpl fileRepository2 = new FileRepositoryImpl(fileManager2, dbFileManager2);
        Controller testController2 = new Controller(fileRepository2);

        HttpStatus testStatus2 = testController2.changeName(oldName, fileName).getStatusCode();
        HttpStatus status2 = HttpStatus.INTERNAL_SERVER_ERROR;

        System.out.println("Ожидалось получить - " + status2 + "\nПолучено - " + testStatus2);
        Assertions.assertEquals(testStatus2, status2);

        // OK
        System.out.println("\nТестирование ОК");
        DbFileManager dbFileManager3 = Mockito.mock(DbFileManager.class);
        Mockito.when(dbFileManager3.findByFilenameAndDeleted(oldName, 0)).thenReturn(downloadFile);

        FileManager fileManager3 = Mockito.mock(FileManager.class);
        Mockito.when(fileManager3.changeName(oldName, newName)).thenReturn(true);

        FileRepositoryImpl fileRepository3 = new FileRepositoryImpl(fileManager3, dbFileManager3);
        Controller testController3 = new Controller(fileRepository3);

        HttpStatus testStatus3 = testController3.changeName(oldName, fileName).getStatusCode();
        HttpStatus status3 = HttpStatus.INTERNAL_SERVER_ERROR;

        System.out.println("Ожидалось получить - " + status3 + "\nПолучено - " + testStatus3);
        Assertions.assertEquals(testStatus3, status3);
    }

    @Test
    void deleteFileTest() {
        System.out.println("ТЕСТ УДАЛЕНИЯ ФАЙЛА");
        System.out.println("Тестирование BAD REQUEST");
        // BAD REQUEST
        String oldName = "Name.txt";
        DownloadFile downloadFile = DownloadFile.builder()
                .path(PREFIX_PATH + "name.txt")
                .filename("name.txt")
                .build();

        DbFileManager dbFileManager1 = Mockito.mock(DbFileManager.class);
        Mockito.when(dbFileManager1.findByFilenameAndDeleted(oldName, NOT_DELETED)).thenReturn(null);
        FileRepositoryImpl fileRepository1 = new FileRepositoryImpl(new FileManager(), dbFileManager1);
        Controller controller1 = new Controller(fileRepository1);
        HttpStatus status1 = HttpStatus.BAD_REQUEST;
        HttpStatus testStatus1 = controller1.deleteFile(oldName).getStatusCode();

        System.out.println("Ожидалось получить - " + status1 + "\nПолучено - " + testStatus1);
        Assertions.assertEquals(testStatus1, status1);

        //INTERNAL SERVER ERROR
        System.out.println("\nТестирование INTERNAL_SERVER_ERROR");
        FileManager fileManager2 = Mockito.mock(FileManager.class);
        Mockito.when(fileManager2.delete(oldName, PREFIX_PATH)).thenReturn(false);

        DbFileManager dbFileManager2 = Mockito.mock(DbFileManager.class);
        Mockito.when(dbFileManager2.findByFilenameAndDeleted(oldName, NOT_DELETED)).thenReturn(downloadFile);

        FileRepositoryImpl fileRepository2 = new FileRepositoryImpl(fileManager2, dbFileManager2);
        Controller controller2 = new Controller(fileRepository2);
        HttpStatus status2 = HttpStatus.INTERNAL_SERVER_ERROR;
        HttpStatus testStatus2 = controller2.deleteFile(oldName).getStatusCode();

        System.out.println("Ожидалось получить - " + status2 + "\nПолучено - " + testStatus2);
        Assertions.assertEquals(testStatus2, status2);
    }

    @Test
    void getFile() throws IOException {
        System.out.println("ТЕСТ СКАЧИВАНИЯ ФАЙЛА");
        System.out.println("Тестирование BAD REQUEST");
        byte[] fileBytes = "hello".getBytes();
        byte[] emptyArr = "".getBytes();
        System.out.println("\nТестирование INTERNAL_SERVER_ERROR");
        DownloadFile downloadFile = DownloadFile.builder()
                .path(PREFIX_PATH + "name.txt")
                .filename("name.txt")
                .build();
        String oldName = "Name.txt";
        // BAD REQUEST

        DbFileManager dbFileManager1 = Mockito.mock(DbFileManager.class);
        Mockito.when(dbFileManager1.findByFilenameAndDeleted(oldName, NOT_DELETED)).thenReturn(null);
        FileRepositoryImpl fileRepository1 = new FileRepositoryImpl(new FileManager(), dbFileManager1);
        Controller controller1 = new Controller(fileRepository1);
        HttpStatus status1 = HttpStatus.BAD_REQUEST;
        HttpStatus testStatus1 = controller1.deleteFile(oldName).getStatusCode();

        System.out.println("Ожидалось получить - " + status1 + "\nПолучено - " + testStatus1);
        Assertions.assertEquals(testStatus1, status1);

        //INTERNAL SERVER ERROR
        FileManager fileManager2 = Mockito.mock(FileManager.class);
        Mockito.when(fileManager2.getFile(downloadFile)).thenReturn(emptyArr);

        DbFileManager dbFileManager2 = Mockito.mock(DbFileManager.class);
        Mockito.when(dbFileManager2.findByFilenameAndDeleted(oldName, NOT_DELETED)).thenReturn(downloadFile);

        FileRepositoryImpl fileRepository2 = new FileRepositoryImpl(fileManager2, dbFileManager2);
        Controller controller2 = new Controller(fileRepository2);
        HttpStatus status2 = HttpStatus.INTERNAL_SERVER_ERROR;
        HttpStatus testStatus2 = controller2.deleteFile(oldName).getStatusCode();

        System.out.println("Ожидалось получить - " + status2 + "\nПолучено - " + testStatus2);
        Assertions.assertEquals(testStatus2, status2);
    }
}

