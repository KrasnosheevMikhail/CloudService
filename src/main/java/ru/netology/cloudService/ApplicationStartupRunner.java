package ru.netology.cloudService;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.netology.cloudService.entity.Users;
import ru.netology.cloudService.repository.UsersRepository;

@AllArgsConstructor
@Component
public class ApplicationStartupRunner implements CommandLineRunner {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        usersRepository.save(Users.builder()
                .login("user1@mail.ru")
                .password(passwordEncoder.encode("pass1"))
                .build());
        usersRepository.save(Users.builder()
                .login("user2@mail.ru")
                .password(passwordEncoder.encode("pass2"))
                .build());
        usersRepository.save(Users.builder()
                .login("user3@mail.ru")
                .password(passwordEncoder.encode("pass3"))
                .build());
    }
}
