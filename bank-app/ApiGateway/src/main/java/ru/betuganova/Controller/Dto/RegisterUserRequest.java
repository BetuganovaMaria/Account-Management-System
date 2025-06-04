package ru.betuganova.Controller.Dto;


import lombok.Data;

@Data
public class RegisterUserRequest {
    private UserDto user;
    private String password;
}
