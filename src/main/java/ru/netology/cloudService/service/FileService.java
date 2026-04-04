package ru.netology.cloudService.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudService.exception.InputDataException;
import ru.netology.cloudService.exception.UnauthorizedException;
import ru.netology.cloudService.model.FileResponse;
import ru.netology.cloudService.repository.AuthorizationRepository;
import ru.netology.cloudService.entity.Files;
import ru.netology.cloudService.entity.Users;
import ru.netology.cloudService.repository.FileRepository;
import ru.netology.cloudService.repository.UsersRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class FileService {
    UsersRepository usersRepository;
    FileRepository fileRepository;
    AuthorizationRepository authorizationRepository;

    private Users getUser(String authToken) {
        if (authToken.startsWith("Bearer ")) {
            authToken = authToken.substring(7);
        }
        final String username = authorizationRepository.getUserNameByToken(authToken);
        return usersRepository.findByLogin(username)
                .orElseThrow(() -> new UnauthorizedException("Authorization error"));
    }

    public void uploadFile(String authToken, String filename, MultipartFile file) {
        final Users user = getUser(authToken);
        if (user == null) {
            throw new UnauthorizedException("Authorization error");
        }
        try {
            fileRepository.save(new Files(filename,
                    LocalDateTime.now(),
                    file.getSize(),
                    file.getContentType(),
                    file.getBytes(), user));
            log.info("User {} upload file {}", user.getLogin(), filename);
        } catch (IOException e) {
            log.error("Upload file error");
            throw new InputDataException("Input data exception");
        }

    }

    public void deleteFile(String authToken, String filename) {
        final Users user = getUser(authToken);
        if (user == null) {
            log.error("Delete file error");
            throw new UnauthorizedException("Authorization error");
        }
        log.info("User {} delete file {}", user.getLogin(), filename);
        fileRepository.deleteByUserAndFileName(user, filename);
    }

    public Files downloadFile(String authToken, String filename) {
        final Users user = getUser(authToken);
        if (user == null) {
            throw new UnauthorizedException("Authorization error");
        }
        final Files file = fileRepository.findByUserAndFileName(user, filename);
        if (file == null) {
            log.error("Download file error");
            throw new InputDataException("Error input data");
        }
        log.info("User {} download file {}", user.getLogin(), filename);
        return file;
    }

    public void editFileName(String authToken, String filename, String newFileName) {
        final Users user = getUser(authToken);
        if (user == null) {
            log.error("Edit file error");
            throw new UnauthorizedException("Authorization error");
        }
        if (newFileName != null) {
            fileRepository.editFileNameByUser(user, filename, newFileName);
            log.info("User {} edit file {}", user.getLogin(), filename);
        } else {
            throw new InputDataException("Error input data");
        }
    }

    public List<FileResponse> getAllFiles(String authToken, Integer limit) {
        final Users user = getUser(authToken);
        if (user == null) {
            log.error("Get all files error");
            throw new UnauthorizedException("Authorization error");
        }
        log.info("User {} get all files", user.getLogin());
        return fileRepository.findAllByUser(user, Sort.by("filename")).stream()
                .map(f -> new FileResponse(f.getFileName(), f.getSize()))
                .collect(Collectors.toList());
    }


}
