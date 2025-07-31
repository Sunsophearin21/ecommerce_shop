package com.sunsophearin.shopease.security.service.impl;

import com.sunsophearin.shopease.security.entities.User;
import com.sunsophearin.shopease.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl {
    private final UserRepository userRepository;
    public User findUserName(String username){
        return userRepository.findByUsername(username).orElseThrow();
    }
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
