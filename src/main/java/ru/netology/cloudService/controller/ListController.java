package ru.netology.cloudService.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.netology.cloudService.entity.Users;
import ru.netology.cloudService.model.FileResponse;
import ru.netology.cloudService.service.AuthorizationService;
import ru.netology.cloudService.service.FileService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/list")
public class ListController {

    private final FileService service;
    private final AuthorizationService authorizationService;

    @GetMapping
    List<FileResponse> getAllFiles(
            @RequestHeader("auth-token") String authToken,
            @RequestParam("limit") Integer limit) {
        Users user = authorizationService.getCurrentUser(authToken);
        return service.getAllFiles(user, limit);
    }
}
