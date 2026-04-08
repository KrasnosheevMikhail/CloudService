package ru.netology.cloudService.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudService.entity.Files;
import ru.netology.cloudService.entity.Users;
import ru.netology.cloudService.service.AuthorizationService;
import ru.netology.cloudService.service.FileService;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;

    private final AuthorizationService authorizationService;

    @PostMapping()
    public ResponseEntity<?> uploadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename,
            @RequestParam("file") MultipartFile file) {


        Users user = authorizationService.getCurrentUser(authToken);


        fileService.uploadFile(user, filename, file);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename) {

        Users user = authorizationService.getCurrentUser(authToken);
        fileService.deleteFile(user, filename);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> downloadFile(@RequestHeader("auth-token") String authToken,
                                               @RequestParam("filename") String filename) {
        Users user = authorizationService.getCurrentUser(authToken);
        Files file = fileService.downloadFile(user, filename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getContent());
    }

    @PutMapping()
    public ResponseEntity<?> editFileName(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename,
            @RequestBody Map<String, String> fileNameRequest) {

        Users user = authorizationService.getCurrentUser(authToken);

        fileService.editFileName(user, filename, fileNameRequest.get("filename"));
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

    /*"RestController
@AllArgsConstructor
@RequestMapping("/file")
@CrossOrigin(origins = "http://localhost:8080")

public class FileController {

    private final FileService fileService;
    private final AuthorizationService authorizationService;

    @PostMapping()
    public ResponseEntity<?> uploadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename,
            @RequestParam("file") MultipartFile file) {

        Users user = authorizationService.getCurrentUser(authToken);
        fileService.uploadFile(user, filename, file);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @DeleteMapping()
    public ResponseEntity<?> deleteFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename) {
        Users user = authorizationService.getCurrentUser(authToken);
        fileService.deleteFile(user, filename);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @GetMapping(produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> downloadFile(@RequestHeader("auth-token") String authToken,
                                               @RequestParam("filename") String filename) {
        Files file = service.downloadFile(user, filename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getContent());
    }





}*/
/*public class FileController {

    private FileService service;

    @PostMapping()
    public ResponseEntity<?> uploadFile(@RequestHeader("auth-token") String authToken,
                                        @RequestParam("filename") String filename, MultipartFile file) {
        service.uploadFile(authToken, filename, file);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteFile(@RequestHeader("auth-token") String authToken,
                                        @RequestParam("filename") String filename) {
        service.deleteFile(authToken, filename);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> downloadFile(@RequestHeader("auth-token") String authToken,
                                               @RequestParam("filename") String filename) {
        Files file = service.downloadFile(authToken, filename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(file.getContent());
    }

    @PutMapping()
    public ResponseEntity<?> editFileName(@RequestHeader("auth-token") String authToken,
                                          @RequestParam("filename") String filename,
                                          @RequestBody Map<String, String> fileNameRequest) {
        service.editFileName(authToken, filename, fileNameRequest.get("filename"));
        return ResponseEntity.ok(HttpStatus.OK);
    }
}*/
