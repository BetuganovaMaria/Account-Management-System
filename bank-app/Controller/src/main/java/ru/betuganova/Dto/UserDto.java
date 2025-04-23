package ru.betuganova.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.betuganova.Model.HairColor;

import java.util.Set;

/**
 * Represents a user dto.
 */
@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private String login;
    private String name;
    private int age;
    private String gender;
    private HairColor hairColor;
    private Set<String> friends;

    /**
     * Constructs a new user dto with the given attributes.
     *
     * @param login The unique login of the user.
     * @param name The name of the user.
     * @param age The age of the user.
     * @param gender The gender of the user (1 for male, 2 for female, other values for undetermined).
     * @param hairColor The hair color of the user.
     */
    UserDto(String login, String name, int age, String gender, HairColor hairColor, Set<String> friends) {
        this.login = login;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.hairColor = hairColor;
        this.friends = friends;
    }
}
