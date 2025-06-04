package ru.betuganova.Service.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String login;
    private String name;
    private int age;
    private String gender;
    private String hairColor;
    private Set<String> friends;
}
