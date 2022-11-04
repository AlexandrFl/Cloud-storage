package as.florenko.diplom.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class JWTResponse {

    public ResponseEntity<Object> generateResponse (String message, HttpStatus status) {
        Map<String, Object> map = new HashMap<>();
        map.put("auth-token", message);
        System.out.println(map);
        return new ResponseEntity<>(map, status);
    }

}
