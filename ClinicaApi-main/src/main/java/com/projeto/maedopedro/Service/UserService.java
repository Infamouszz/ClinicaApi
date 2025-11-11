package com.projeto.maedopedro.Service;

import com.projeto.maedopedro.Model.UserModel.User;
import com.projeto.maedopedro.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));}

    public User save(User newUser){
        if (newUser.getId() == null){
            newUser.setCreation_date(LocalDateTime.now());
        }
        return userRepository.save(newUser);
    }
}
