package ru.netology.cloudService;

import com.google.gson.Gson;
import jakarta.servlet.ServletContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import ru.netology.cloudService.controller.FileController;
import ru.netology.cloudService.model.AuthorizationRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CloudServiceApplicationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    MockMvc mockMvc;

    private static final String WRONG_LOGIN = "user1";
    private static final String LOGIN = "pochta1@mail.ru";
    private static final String PASSWORD = "pass1";
    private static final String ENDPOINT_LOGIN = "/login";
    private static final String ENDPOINT_LOGOUT = "/logout";
    private static AuthorizationRequest validRequest;
    private static AuthorizationRequest invalidRequest;
    private static final Gson gson = new Gson();

    @BeforeAll
    public static void beforeAll() {
        validRequest = new AuthorizationRequest(LOGIN, PASSWORD);
        invalidRequest = new AuthorizationRequest(WRONG_LOGIN, PASSWORD);
    }

    @Test
    void testLoginSuccess() throws Exception {
        mockMvc.perform(post(ENDPOINT_LOGIN).contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(validRequest))).andExpect(status().isOk());
    }

    @Test
    void testLoginFail() throws Exception {
        mockMvc.perform(post(ENDPOINT_LOGIN).contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(invalidRequest))).andExpect(status().isUnauthorized());
    }

    @Test
    void testLogoutSuccess() throws Exception {
        mockMvc.perform(post(ENDPOINT_LOGOUT).contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(validRequest))).andExpect(status().is3xxRedirection());
    }


    @Test
    void FilesControllerTest() {
        ServletContext servletContext = webApplicationContext.getServletContext();
        Assertions.assertNotNull(servletContext);
        Assertions.assertNotNull(webApplicationContext.getBean(FileController.class));
    }

}