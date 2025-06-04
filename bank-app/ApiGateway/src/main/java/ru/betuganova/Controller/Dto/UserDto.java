package ru.betuganova.Controller.Dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class UserDto {
    private String login;
    private String name;
    private int age;
    private String gender;
    private String hairColor;
    private Set<String> friends;
}
