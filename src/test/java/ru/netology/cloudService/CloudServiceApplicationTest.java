package ru.netology.cloudService;


import com.google.gson.Gson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureWebMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.netology.cloudService.model.AuthorizationRequest;
import ru.netology.cloudService.model.AuthorizationResponse;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureWebMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WebMvcTest
public class CloudServiceApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    private static String validToken;
    private static final Gson gson = new Gson();
    private static final String LOGIN = "user1@mail.ru";
    private static final String PASSWORD = "pass1";

    @BeforeAll
    static void setupAll(@Autowired MockMvc mockMvc) throws Exception {
        AuthorizationRequest request = new AuthorizationRequest(LOGIN, PASSWORD);
        MvcResult result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(request)))
                .andExpect(status().isOk())
                .andReturn();
        validToken = gson.fromJson(result.getResponse().getContentAsString(), AuthorizationResponse.class).getAuthToken();
        Assertions.assertNotNull(validToken, "Токен не должен быть null");
    }

    @Test
    void testProtectedEndpointWithValidToken() throws Exception {
        mockMvc.perform(get("/list")
                        .header("auth-token", "Bearer " + validToken)
                        .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}