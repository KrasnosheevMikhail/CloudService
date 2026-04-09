package ru.netology.cloudService.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.cloudService.entity.Files;
import ru.netology.cloudService.entity.Users;
import ru.netology.cloudService.repository.FileRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class FileServiceTest {

    @InjectMocks
    private FileService fileService;

    @Mock
    private FileRepository fileRepository;

    public static final String LOGIN_1 = "login1";
    public static final String PASSWORD_1 = "pass1";
    public static final Users USER_1 = Users.builder().login(LOGIN_1).password(PASSWORD_1).build();

    public static final String FILENAME_1 = "fileName1";
    public static final String FILENAME_2 = "fileName2";


    public static final Files FILE_ENTITY = Files.builder()
            .fileName(FILENAME_1)
            .content("test".getBytes())
            .type("text/plain")
            .size(4L)
            .date(LocalDateTime.now())
            .user(USER_1)
            .build();


    @Test
    void deleteFile_ShouldCallRepositoryMethod() {

        fileService.deleteFile(USER_1, FILENAME_1);

        Mockito.verify(fileRepository, Mockito.times(1))
                .deleteByUserAndFileName(USER_1, FILENAME_1);
    }


    @Test
    void downloadFile_ShouldReturnFileEntity() {
        Mockito.when(fileRepository.findByUserAndFileName(USER_1, FILENAME_1)).thenReturn(FILE_ENTITY);

        Files result = fileService.downloadFile(USER_1, FILENAME_1);

        assertEquals(FILENAME_1, result.getFileName());
        Mockito.verify(fileRepository, Mockito.times(1))
                .findByUserAndFileName(USER_1, FILENAME_1);
    }


    @Test
    void getAllFiles_ShouldReturnList() {
        List<Files> mockList = Collections.singletonList(FILE_ENTITY);
        Mockito.when(fileRepository.findAllByUser(USER_1, any())).thenReturn(mockList);

        List<Files> result = fileService.getAllFiles(USER_1, 10);

        assertEquals(1, result.size());
        assertEquals(FILENAME_1, result.get(0).getFileName());
    }
}