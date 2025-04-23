package ru.betuganova.Exception;

public class FriendDoesntExistException extends RuntimeException {
    public FriendDoesntExistException(String message) {
        super(message);
    }
}
