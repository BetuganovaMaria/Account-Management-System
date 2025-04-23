package ru.betuganova.Service.CurrentUserManager;


import ru.betuganova.Model.User;

/**
 * Manages the current authenticated user in the system.
 */
public interface CurrentUserManager {
    /**
     * Retrieves the currently authenticated user.
     *
     * @return The current user.
     */
    User getCurrentUser();

    /**
     * Sets the currently authenticated user.
     *
     * @param user The user to set as the current user.
     */
    void setCurrentUser(User user);
}
