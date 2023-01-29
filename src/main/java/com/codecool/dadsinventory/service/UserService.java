package com.codecool.dadsinventory.service;

import com.codecool.dadsinventory.model.Gender;
import com.codecool.dadsinventory.model.UserEntity;
import com.codecool.dadsinventory.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity saveUser(UserEntity user){
        log.info("***** SAVING NEW USER -> username : {} <- TO THE DB. *****",user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        List<SimpleGrantedAuthority> roles = new ArrayList<>();
        if (user.getGender() == Gender.MALE) {
            roles.add(new SimpleGrantedAuthority("ROLE_DAD"));
        }
        else{
            roles.add(new SimpleGrantedAuthority("ROLE_MUM"));
        }
        return new User(user.getUsername(),user.getPassword(),roles);
    }
}
