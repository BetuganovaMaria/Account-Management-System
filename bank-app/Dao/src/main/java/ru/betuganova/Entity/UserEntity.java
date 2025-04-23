package ru.betuganova.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.HashSet;

/**
 * Represents a user entity in the system, stored in the "users" table.
 * This entity contains the user's personal information and a list of friends.
 */
@Getter
@Setter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class UserEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login")
    private String login;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "age")
    private int age;

    @NotNull
    @Column(name = "gender")
    private int gender;

    @NotNull
    @Column(name = "hair_color")
    private String hairColor;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private Set<UserEntity> friends;

    /**
     * Constructs a new user entity with the given attributes.
     *
     * @param login The unique login of the user.
     * @param name The name of the user.
     * @param age The age of the user.
     * @param gender The gender of the user (1 for male, 2 for female, other values for unspecified).
     * @param hairColor The hair color of the user.
     */
    public UserEntity(String login, String name, int age, int gender, String hairColor) {
        this.login = login;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.hairColor = hairColor;
        friends = new HashSet<>();
    }

    /**
     * Adds a friend to the user's friend list.
     * The friend is identified by their login.
     *
     * @param user the user to be added as a friend.
     */
    public void addFriend(UserEntity user) {
        friends.add(user);
    }

    /**
     * Removes a friend from the user's friend list.
     * The friend is identified by their login.
     *
     * @param user the user to be removed from the friend list.
     */
    public void deleteFriend(UserEntity user) {
        friends.remove(user);
    }
}
