package ru.betuganova.Exception;

public class FriendExistsException extends RuntimeException {
    public FriendExistsException(String message) {
        super(message);
    }
}
