package ru.netology.cloudService.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloudService.entity.Files;
import ru.netology.cloudService.entity.Users;
import ru.netology.cloudService.model.FileResponse;
import ru.netology.cloudService.service.AuthService;
import ru.netology.cloudService.service.FileService;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@AllArgsConstructor
@RequestMapping("/list")
public class ListController {

    private final FileService fileService;
    private final AuthService authService;

    @GetMapping
    public List<FileResponse> getAllFiles(
            @RequestHeader("auth-token") String authToken,
            @RequestParam(value = "limit", required = false) Integer limit) {

        Users user = authService.getCurrentUser(authToken);


        List<Files> filesList = fileService.getAllFiles(user, limit);

        return filesList.stream()
                .map(file -> new FileResponse(file.getFileName(), file.getSize()))
                .collect(Collectors.toList());
    }
}