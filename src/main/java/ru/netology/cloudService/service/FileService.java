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
import ru.netology.cloudService.repository.FileRepository;
import ru.netology.cloudService.repository.UsersRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class FileService {

    private final FileRepository fileRepository;
    private final UsersRepository usersRepository;


    public void uploadFile(Users user, String filename, MultipartFile file) {
        try {
            Files newFile = Files.builder()
                    .fileName(filename)
                    .date(LocalDateTime.now())
                    .size(file.getSize())
                    .type(file.getContentType())
                    .content(file.getBytes())
                    .user(user)
                    .build();
            fileRepository.save(newFile);
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

    public List<Files> getAllFiles(Users user, Integer limit) {
        log.info("User {} get all files", user.getLogin());
        Sort sort = Sort.by("fileName").ascending();
        List<Files> allFiles = fileRepository.findAllByUser(user, sort);

        if (limit != null && limit > 0 && limit < allFiles.size()) {
            return allFiles.subList(0, limit);
        }
        return allFiles;
    }
}