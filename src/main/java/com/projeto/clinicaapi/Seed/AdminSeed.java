package com.projeto.clinicaapi.Seed;

import com.projeto.clinicaapi.Model.UserModel.Role;
import com.projeto.clinicaapi.Model.UserModel.User;
import com.projeto.clinicaapi.Repository.UserRepository;
import com.projeto.clinicaapi.Service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSeed implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {

        if (userRepository.findByUsername("admin").isEmpty()) {

            User admin = User
                    .builder()
                    .username("admin")
                    .password(passwordEncoder.encode("senhalegal"))
                    .role(Role.ROLE_ADMIN)
                    .build();

            userService.save(admin);
            log.debug("created ADMIN user - {}", admin);
        }
    }

}