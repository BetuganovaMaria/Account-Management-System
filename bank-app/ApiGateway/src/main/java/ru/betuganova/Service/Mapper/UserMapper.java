package ru.betuganova.Service.Mapper;

import ru.betuganova.Service.Model.User;

import java.util.*;

public class UserMapper {
    public User mapToUser(Map<String, Object> map) {
        String login = (String) map.get("login");
        String name = (String) map.get("name");
        int age = (int) map.get("age");
        String genderStr = map.get("gender").toString();

        String hairColorStr = (String) map.get("hairColor");

        List<String> friendsList = (List<String>) map.getOrDefault("friends", new ArrayList<>());
        Set<String> friends = new HashSet<>(friendsList);

        return new User(login, name, age, genderStr, hairColorStr, friends);
    }
}
