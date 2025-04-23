package ru.betuganova.Model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.HashSet;

/**
 * Represents a user in the system.
 */
@Getter
@Setter
@NoArgsConstructor
public class User {
    private String login;
    private String name;
    private int age;
    private int gender;
    private HairColor hairColor;
    private Set<String> friends;

    /**
     * Constructs a new user with the given attributes.
     *
     * @param login The unique login of the user.
     * @param name The name of the user.
     * @param age The age of the user.
     * @param gender The gender of the user (1 for male, 2 for female, other values for unspecified).
     * @param hairColor The hair color of the user.
     */
    public User(String login, String name, int age, int gender, HairColor hairColor) {
        this.login = login;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.hairColor = hairColor;
        this.friends = new HashSet<>();
    }
}