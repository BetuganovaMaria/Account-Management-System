package ru.betuganova.Service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.betuganova.Entity.UserEntity;
import ru.betuganova.Exception.FriendDoesntExistException;
import ru.betuganova.Exception.FriendExistsException;
import ru.betuganova.Exception.UserExistsException;
import ru.betuganova.Exception.UserFriendException;
import ru.betuganova.Mapper.UserMapper;
import ru.betuganova.Model.HairColor;
import ru.betuganova.Model.User;
import ru.betuganova.Repository.UserRepository;
import ru.betuganova.Service.CurrentUserManager.CurrentUserManager;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link UserService} interface that handles user-related operations.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final CurrentUserManager currentUserManager;
    private final UserMapper userMapper;

    /**
     * Constructs a new {@code UserServiceImpl} with the specified repositories and user manager.
     *
     * @param currentUserManager The manager handling the current logged-in user.
     */
    @Autowired
    public UserServiceImpl(CurrentUserManager currentUserManager, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.currentUserManager = currentUserManager;
        this.userMapper = new UserMapper(userRepository);
    }

    /**
     * {@inheritDoc}
     */
    public Boolean IsAuthorized() {
        return currentUserManager.getCurrentUser() != null;
    }

    /**
     * c
     * {@inheritDoc}
     */
    @Transactional
    public void login(String login) throws NoSuchElementException {
        User user = userMapper.toModel(userRepository.findByLogin(login));

        if (user == null) {
            throw new NoSuchElementException("User with login: '" + login + "' wasn't found");
        }

        currentUserManager.setCurrentUser(user);
    }

    /**
     * {@inheritDoc}
     */
    public void logout() {
        currentUserManager.setCurrentUser(null);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public User createUser(String login,
                           String name,
                           int age,
                           String gender,
                           HairColor hairColor) throws UserExistsException {

        UserEntity userByLogin = userRepository.findByLogin(login);

        if (userByLogin != null) {
            throw new UserExistsException("User was already added");
        }

        int genderInt;
        switch (gender) {
            case "Male":
                genderInt = 1;
                break;

            case "Female":
                genderInt = 2;
                break;

            default:
                genderInt = 0;
        }
        UserEntity user = new UserEntity(login, name, age, genderInt, hairColor.toString());
        userRepository.save(user);

        return userMapper.toModel(user);
    }

    /**
     * {@inheritDoc}
     */
    public User getUserInfo() {
        return currentUserManager.getCurrentUser();
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void addFriend(String login) throws FriendExistsException, NoSuchElementException, UserFriendException {
        UserEntity currentUser = userRepository.findByLogin(currentUserManager.getCurrentUser().getLogin());
        List<UserEntity> friends = userRepository.findFriendsByUserLogin(currentUser.getLogin());

        UserEntity friend = friends == null ? null : friends
                .stream()
                .filter(f -> f.getLogin().equals(login))
                .findFirst()
                .orElse(null);
        UserEntity user = userRepository.findByLogin(login);

        if (friend == null) {
            if (user == null) {
                throw new NoSuchElementException("User with login: '" + login + "' wasn't found");
            }
            if (Objects.equals(currentUser.getLogin(), user.getLogin())) {
                throw new UserFriendException("User can't be made it's friend");
            }

            currentUser.addFriend(userRepository.findByLogin(login));
            userRepository.save(currentUser);

        } else {
            throw new FriendExistsException("Friend was already added");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public void deleteFriend(String login) throws FriendDoesntExistException, NoSuchElementException {
        UserEntity currentUser = userRepository.findByLogin(currentUserManager.getCurrentUser().getLogin());
        List<UserEntity> friends = userRepository.findFriendsByUserLogin(currentUser.getLogin());
        UserEntity friend = friends == null ? null : friends
                .stream()
                .filter(f -> f.getLogin().equals(login))
                .findFirst()
                .orElse(null);

        if (friend == null) {
            throw new FriendDoesntExistException("Friend was already deleted");
        }

        currentUser.deleteFriend(friend);
        userRepository.save(currentUser);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public List<User> getUsersByHairColorAndGender(String hairColor, int gender) {
        return userRepository.findAll()
                .stream()
                .filter(user -> hairColor == null || hairColor.equalsIgnoreCase(user.getHairColor()))
                .filter(user -> gender == 0 || user.getGender() == gender)
                .map(userMapper::toModel)
                .collect(Collectors.toList());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public List<User> getFriendsById(Long id) throws NoSuchElementException {
        return userRepository.findById(id)
                .map(userEntity -> userRepository.findFriendsByUserLogin(userEntity.getLogin())
                        .stream()
                        .map(userMapper::toModel)
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new NoSuchElementException("User with id " + id + " not found"));
    }
}
