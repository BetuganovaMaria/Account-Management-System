package ru.betuganova.Service.UserService;

import ru.betuganova.Model.HairColor;
import ru.betuganova.Model.User;

import java.util.List;

/**
 * Service interface for managing user-related operations.
 */
public interface UserService {
    /**
     * Checks if a user is currently authorized (logged in).
     *
     * @return {@code true} if a user is logged in, otherwise {@code false}.
     */
    Boolean IsAuthorized();

    /**
     * Logs in a user with the given login.
     *
     * @param login The login of the user.
     */
    void login(String login);

    /**
     * Logs out the currently logged-in user.
     */
    void logout();

    /**
     * Creates a new user with the specified details.
     *
     * @param login     The user's login.
     * @param name      The user's name.
     * @param age       The user's age.
     * @param gender    The user's gender (1 for male, 2 for female, other values for undetermined).
     * @param hairColor The user's hair color (Unknown for undetermined colors)
     * @return The newly created {@link User} object.
     */
    User createUser(String login, String name, int age, String gender, HairColor hairColor);

    /**
     * Retrieves information about the currently logged-in user.
     *
     * @return A string containing user details.
     */
    User getUserInfo();

    /**
     * Adds a friend to the currently logged-in user's friend list.
     *
     * @param login The login of the friend to add.
     */
    void addFriend(String login);

    /**
     * Removes a friend from the currently logged-in user's friend list.
     *
     * @param login The login of the friend to remove.
     */
    void deleteFriend(String login);

    /**
     * Retrieves a list of users filtered by hair color and gender.
     *
     * @param hairColor the hair color to filter users by;
     * @param gender the gender to filter users by; use a predefined value (e.g., 1 for male, 2 for female);
     * @return a list of users matching the specified hair color and gender criteria
     */
    List<User> getUsersByHairColorAndGender(String hairColor, int gender);

    /**
     * Retrieves the list of friends for a user by their ID.
     *
     * @param id the ID of the user whose friends are to be retrieved
     * @return a list of users who are friends with the specified user
     */
    List<User> getFriendsById(Long id);
}
