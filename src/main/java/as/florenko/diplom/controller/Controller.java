package as.florenko.diplom.controller;

import as.florenko.diplom.repository.FileRepositoryImpl;
import as.florenko.diplom.model.AuthRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import as.florenko.diplom.model.FileName;
import as.florenko.diplom.security.JWTUtil;

import javax.ws.rs.Consumes;

import java.io.IOException;

@RestController
@CrossOrigin
public class Controller {
    @Autowired
    private final FileRepositoryImpl fileRepository;

    public Controller(FileRepositoryImpl fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtTokenUtil;

    @PostMapping("/login")
    public ResponseEntity<Object> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
            System.out.println(authentication);
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials", e);
        }
        String jwt = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal());
        return new JWTResponse().generateResponse(jwt, HttpStatus.OK);
    }

    @PostMapping("/logout")
    @GetMapping("/logout")
    public ResponseEntity logout() {
        return ResponseEntity.ok("Success logout");
    }

    @GetMapping("/list")
    public ResponseEntity getList(@RequestParam int limit) {
        return fileRepository.getList(limit);
    }

    @PostMapping("/file")
    @Consumes(value = "multipart/form-data")
    public ResponseEntity addFile(@RequestParam String filename, @RequestBody MultipartFile file) throws IOException {
        return fileRepository.addFile(filename, file.getSize(), file.getBytes());
    }

    @PutMapping("/file")
    @Consumes(value = "application/json")
    public ResponseEntity changeName(@RequestParam String filename, @RequestBody FileName newName) {
        return fileRepository.changeName(filename, newName.getFilename());
    }

    @DeleteMapping("/file")
    public ResponseEntity deleteFile(@RequestParam String filename) {
        return fileRepository.deleteFile(filename);
    }

    @GetMapping("/file")
    public ResponseEntity get(@RequestParam String filename) throws IOException {
        return fileRepository.getFile(filename);
    }
}
