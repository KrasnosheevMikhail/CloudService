package ru.netology.cloudService.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.netology.cloudService.repository.AuthorizationRepository;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationResponse extends AuthorizationRepository implements Serializable {

    @Serial
    private static final long serialVersionUID = -3L;

    @JsonProperty("auth-token")
    private String authToken;

}
