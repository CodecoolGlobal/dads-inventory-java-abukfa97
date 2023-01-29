package com.codecool.dadsinventory.controller;

import com.codecool.dadsinventory.model.UserEntity;
import com.codecool.dadsinventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public UserEntity registerUser(@RequestBody UserEntity user){
        return userService.saveUser(user);
    }
}
