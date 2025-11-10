package com.projeto.maedopedro.Seed;

import com.projeto.maedopedro.Model.UserModel.Role;
import com.projeto.maedopedro.Model.UserModel.User;
import com.projeto.maedopedro.Repository.UserRepository;
import com.projeto.maedopedro.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecretarySeed implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("secretary").isEmpty()) {

            User secretary = User
                    .builder()
                    .username("secretary")
                    .password(passwordEncoder.encode("senhalegal2"))
                    .role(Role.ROLE_SECRETARY)
                    .build();

            userService.save(secretary);
            log.debug("created SECRETARY user - {}", secretary);
        }
    }
}
