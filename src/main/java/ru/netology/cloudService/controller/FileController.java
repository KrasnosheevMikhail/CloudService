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
import ru.netology.cloudService.service.AuthService;
import ru.netology.cloudService.service.FileService;


@RestController
@AllArgsConstructor
@RequestMapping("/file")
public class FileController {

    private final FileService fileService;
    private final AuthService authService;

    @PostMapping()
    public ResponseEntity<?> uploadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename,
            @RequestParam("file") MultipartFile file) {

        Users user = authService.getCurrentUser(authToken);
        fileService.uploadFile(user, filename, file);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename) {

        Users user = authService.getCurrentUser(authToken);
        fileService.deleteFile(user, filename);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<byte[]> downloadFile(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("filename") String filename) {

        Users user = authService.getCurrentUser(authToken);
        Files fileEntity = fileService.downloadFile(user, filename);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileEntity.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\""
                        + fileEntity.getFileName() + "\"")
                .body(fileEntity.getContent());
    }
}
