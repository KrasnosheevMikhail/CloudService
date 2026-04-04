package ru.netology.cloudService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 35L;

    private String login;
    private String password;
}
