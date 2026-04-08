package ru.netology.cloudService.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.netology.cloudService.entity.Files;
import ru.netology.cloudService.entity.Users;
import ru.netology.cloudService.exception.InputDataException;
import ru.netology.cloudService.exception.UnauthorizedException;
import ru.netology.cloudService.model.FileResponse;
import ru.netology.cloudService.repository.FileRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class FileService {

    private final FileRepository fileRepository;


    public void uploadFile(Users user, String filename, MultipartFile file) {


        try {
            fileRepository.save(Files.builder()
                    .fileName(filename)
                    .date(LocalDateTime.now())
                    .size(file.getSize())
                    .type(file.getContentType())
                    .content(file.getBytes())
                    .user(user)
                    .build());
            log.info("User {} uploaded file {}", user.getLogin(), filename);
        } catch (IOException e) {
            log.error("Upload file error", e);
            throw new InputDataException("Input data exception");
        }
    }

    public void deleteFile(Users user, String filename) {
        log.info("User {} deleting file {}", user.getLogin(), filename);
        fileRepository.deleteByUserAndFileName(user, filename);
    }

    public Files downloadFile(Users user, String filename) {
        Files file = fileRepository.findByUserAndFileName(user, filename);
        if (file == null) {
            log.error("Download file error for user {}", user.getLogin());
            throw new InputDataException("Error input data");
        }
        log.info("User {} downloaded file {}", user.getLogin(), filename);
        return file;
    }

    public void editFileName(Users user, String filename, String newFileName) {

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

    public List<FileResponse> getAllFiles(Users user, Integer limit) {

        log.info("User {} is requesting file list", user.getLogin());


        Sort sort = Sort.by(Sort.Direction.ASC, "fileName");


        if (limit != null && limit > 0) {
            return fileRepository.findAllByUser(user, Sort.by("fileName"))
                    .stream()
                    .map(f -> new FileResponse(f.getFileName(), f.getSize()))
                    .collect(Collectors.toList());
        } else {
            return fileRepository.findAllByUser(user, sort).stream()
                    .map(f -> new FileResponse(f.getFileName(), f.getSize()))
                    .collect(Collectors.toList());
        }

    }
}

