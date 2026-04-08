package ru.netology.cloudService.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.netology.cloudService.entity.Users;
import ru.netology.cloudService.repository.FileRepository;

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



    @Test
    void deleteFile_ShouldCallRepositoryMethod() {

        fileService.deleteFile(USER_1, FILENAME_1);


        Mockito.verify(fileRepository, Mockito.times(1))
                .deleteByUserAndFileName(USER_1, FILENAME_1);
    }

    @Test
    void editFileName_ShouldCallRepositoryMethod() {

        fileService.editFileName(USER_1, FILENAME_1, FILENAME_2);


        Mockito.verify(fileRepository, Mockito.times(1))
                .editFileNameByUser(USER_1, FILENAME_1, FILENAME_2);
    }

    @Test
    void uploadFile_ShouldCallSaveOnRepository() {

        fileService.uploadFile(USER_1, FILENAME_1, null);


        Mockito.verify(fileRepository, Mockito.times(1)).save(any());


    }
}