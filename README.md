# Облачное хранилище
## Принцип работы:
- ### _Вход в облачное хранилище_

Для входа в облачное хранилище необходимо ввести в форму входа логин и пароль. Эти данные отправляются на сервер и обрабатываются в методе createAuthenticationToken() класса Controller.
В данном методе происходит аутентификация пользователя и присвоение ему JWTToken. Если введеного в форму входа логина и пароля нет в базе данный пользователей, то HTTPStatus запроса устанавливается равним UNAUTHORIZED (401) 

- ### _Загрузка файла в хранилище_

Загрузка файла обрабатывается в методе addFile() класса Controller. 
```java
@RestController
@CrossOrigin
public class Controller {
    @PostMapping("/file")
    @Consumes(value = "multipart/form-data")
    public ResponseEntity addFile(@RequestParam String filename, @RequestBody MultipartFile file) throws IOException {
        return fileRepository.addFile(filename, file.getSize(), file.getBytes());
    }
}
```
Метод addFile() принимает в качестве параметров имя файла и файл приведенный к типу MultipartFile. Далее имя файла, его размер и массив байт передаются в качестве параметров в метод addFile() класса FileRepository.

Принцип работы метода fileRepository.addFile():
1. Проверка целостности данных. Если имя файла равно _null_ или размер массива байт равен 0, или размер файла меньше или равен 0, то это свидетельствует о некорректности данных полученных от пользователя. Если хотя бы один из выше перечисленных критериев не выполняется, то в качестве ответа медот вернет _ResponseEntity.status(400).body("Error input data")_;
2. При загрузке файла, есть вероятность того, что файл уже был загружен пользователем ранее. Для анализа данного случая в методе происходит поиск файла в базе файлов. Если файл с таким именем присутствует в базе, то в качестве ответа метод вернет _ResponseEntity.ok("Success upload")_;
3. Если файла нет в базе, то создается новый объект класса DownloadFile. Файл записывается в облако, а его параметры, такие как имя, размер, путь загружаются в базу данных;
4. Для проверки того, что файл действительно появился в баже производится поиск файла в данной базе. Если окажется, что файл отсутствует, то метотд вернет _ResponseEntity.status(500).body("Error adding file")_;
5. Если информация о файле загружена в базу и проверка №4 пройдено, то метод возвращает _ResponseEntity.ok("Success upload")_.

- ### _Переименование файла_

Изменение имени файла обрабатывается в методе changeName() класса Controller
```java
@RestController
@CrossOrigin
public class Controller {
    @PutMapping("/file")
    @Consumes(value = "application/json")
    public ResponseEntity changeName(@RequestParam String filename, @RequestBody FileName newName) {
        return fileRepository.changeName(filename, newName.getFilename());
    }
}
```
Метод принимает в качестве параметров текущее имя файла и новое имя. Эти же данные используются в качестве параметров метода changeName() класса FIleRepository.

Принцип работы метода fileRepository.changeName():
1. Проверка наличия файла в базе. Если файл отстутствует в базе файлов, то в качестве ответа метод вернет _ResponseEntity.status(400).body("Error input data")_;
2. Если файл был найдет в базе, то далее производится изменение его имени. Если по какой-то причине переименовать файл не удалось, в качестве ответа метод вернет _ResponseEntity.status(500).body("Error upload file")_;
3. После изменения имени файла производится обновление информации о файле в базе данных.

- ### _Удаление файлов_

Удаление файла обрабатывается в методе deleteFile() класса Controller.
```java
@RestController
@CrossOrigin
public class Controller {
    @DeleteMapping("/file")
    public ResponseEntity deleteFile(@RequestParam String filename) {
        return fileRepository.deleteFile(filename);
    }
}
```
Метод принимает в качестве параметра имя файла. Это имя так же используется в качестве параметра метода deleteFile() класса FIleRepository.

Принцип работы метода fileRepository.deleteFile():
1. Проверка наличия файла в базе. Если файл отстутствует в базе файлов, то в качестве ответа метод вернет _ResponseEntity.status(400).body("Error input data")_;
2. Зачастую файлы из базы не удаляются, а помечаются как удаленные. Поэтому следующим шагом происходит переименование файла. Новое имя будет начинаться со строки (deleted). Если по какой-то причине переименовать файл не удалось, в качестве ответа метод вернет _ResponseEntity.status(500).body("Error delete file")_;
3. После успешного изменения имени файла происходит изменение пути файла в базе данных и с помощью параметра _deleted_ файл отмечается как удаленный. 

- ### _Скачивание файлов_

Загрузка файла из облака обрабатывается в методе get() класса Controller.
```java
@RestController
@CrossOrigin
public class Controller {
    @GetMapping("/file")
    public ResponseEntity get(@RequestParam String filename) throws IOException {
        return fileRepository.getFile(filename);
    }
}
```
Метод принимает в качестве параметра имя файла. Это имя так же используются в качестве параметров метода deleteFile() класса FIleRepository.

Принцип работы метода fileRepository.getFile():
1. Проверка наличия файла в базе. Если файл отстутствует в базе файлов, то в качестве ответа метод вернет _ResponseEntity.status(400).body("Error input data")_;
2. Далее происходит преобразование файла в массив байт. Если преобразование завершилось с некорректным результатом, метод вернет _ResponseEntity.status(500).body("Error upload file")_.

- ### _Вывод списка файлов_

Получение пользователем списка файлов находящихся в базе данных обрабатывается методом getList() класса Controller.
В качестве параметра метод принимает лимит - кол-во файлов для одновременного отображения. Если список файлов больше установленного лимита метод вернет _ResponseEntity.status(500).body("Error getting file list")_.

## Демонстрация работы облачного хранилища:
1. В качестве демонстрационного изображения использовался jpg файл. Файл и его параметры приведены ниже.
   ![](https://sun9-east.userapi.com/sun9-76/s/v1/ig2/sgRWKVVLbIgjjAhXPZOSTDEj3aEGMDOkUf87xY5rBH2AeRooCDzT_kCxp7IJWQPaTuRE2vB4LbPlsLQv1L4HJ-V3.jpg?size=1280x1024&quality=95&type=album)
   ![](https://sun9-east.userapi.com/sun9-73/s/v1/ig2/DTc-wYgt7vLzE0Rw6yMFd0E_aoBdP8AmlGYeNqQ9aQDjTZ6uDsanriimFlM0vNTeRkzx9-9aTeVe5vSUA7FaU-nx.jpg?size=502x621&quality=95&type=album)


2. Вход в облако
   ![](https://sun9-east.userapi.com/sun9-26/s/v1/ig2/u9o16ljPn_zzrkAcoMVMJmeYrfKd852bnp-PahB-7xUf14azoFMzKnL1R4HaSt7KGDrfMRWkOkMkJFKBAmpiLoXs.jpg?size=1916x979&quality=95&type=album)
3. Загрузка файла
   ![](https://sun9-west.userapi.com/sun9-51/s/v1/ig2/UVZEGlZ2o4vQdZqJXrL-jMDp3sz5_pYNexf99DVjqEDTRGa4whgHCHzEo9y3e44dP2ZMbNACi04GWc4gB-ZP9RKk.jpg?size=1919x983&quality=95&type=album)
4. Изменение имены файла
   ![](https://sun9-north.userapi.com/sun9-81/s/v1/ig2/Eb2MBstnyfgL5Y0AyECPAyqtGgjRq0LeV1xoUO-Sh4NPVRYCANU5Lo0f62_5pPi4tsgiEOgdfsJoBVuhbTABq9eK.jpg?size=1919x983&quality=95&type=album)
5. Скачивание файла
   ![](https://sun9-east.userapi.com/sun9-28/s/v1/ig2/zEHVBCC9NP6YirLPOZ8VhC3FxzTsfTyQIFp6NwVN2ENrKinR8-p7-AsoFxOeWUCZ2_EXyVXnyO9qsFOgBbiq3Wlx.jpg?size=1274x168&quality=95&type=album)
6. Удаление файла
   ![](https://sun9-west.userapi.com/sun9-38/s/v1/ig2/BzgwKcnldBVP0hOFqgkhmFEIa36q3OCYqDPnHwiH7nV4fkGkBIHaD3xgCTTbQG5dCem8VBdhH5j6xsZmm4n1K8l7.jpg?size=1918x982&quality=95&type=album)
7. Выход
   ![](https://sun9-north.userapi.com/sun9-85/s/v1/ig2/-zUnU5bfujnq9b5NKEJI2dWqcKenra7ZmEM0wQBCRsSUuAQ6Z8OIrqjrQyxjMVh1ED2d0TpcibKde6OtHBHGsC2z.jpg?size=1919x985&quality=95&type=album)