package ru.betuganova.Service.CurrentUserManager;


import org.springframework.stereotype.Component;
import ru.betuganova.Model.User;

/**
 * Implementation of the {@link CurrentUserManager} interface.
 * This class manages the currently authenticated user.
 */
@Component
public class CurrentUserManagerImpl implements CurrentUserManager {
    private User currentUser;

    /**
     * {@inheritDoc}
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * {@inheritDoc}
     */
    public void setCurrentUser(User user) {
        currentUser = user;
    }
}
