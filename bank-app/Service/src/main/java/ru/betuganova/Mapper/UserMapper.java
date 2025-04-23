package ru.betuganova.Mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.betuganova.Entity.UserEntity;
import ru.betuganova.Model.HairColor;
import ru.betuganova.Model.User;
import ru.betuganova.Repository.UserRepository;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    private UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public UserMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.modelMapper = new ModelMapper();
        configureMappings();
    }

    private void configureMappings() {
        Converter<String, HairColor> toEnum = ctx -> HairColor.valueOf(ctx.getSource().toUpperCase());
        Converter<HairColor, String> toString = ctx -> ctx.getSource().name();

        Converter<Set<UserEntity>, Set<String>> friendsToLogins = ctx ->
                ctx.getSource() == null ? null :
                        ctx.getSource().stream()
                                .map(UserEntity::getLogin)
                                .collect(Collectors.toSet());

        Converter<Set<String>, Set<UserEntity>> loginsToFriends = ctx ->
                ctx.getSource() == null ? null :
                        ctx.getSource().stream()
                                .map(userRepository::findByLogin)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet());

        modelMapper.createTypeMap(UserEntity.class, User.class)
                .addMappings(mapper -> {
                    mapper.using(toEnum).map(UserEntity::getHairColor, User::setHairColor);
                    mapper.using(friendsToLogins).map(UserEntity::getFriends, User::setFriends);
                });

        modelMapper.createTypeMap(User.class, UserEntity.class)
                .addMappings(mapper -> {
                    mapper.using(toString).map(User::getHairColor, UserEntity::setHairColor);
                    mapper.using(loginsToFriends).map(User::getFriends, UserEntity::setFriends);
                });
    }

    public User toModel(UserEntity user) {
        return user == null ? null : modelMapper.map(user, User.class);
    }

    public UserEntity toEntity(User user) {
        return user == null ? null : modelMapper.map(user, UserEntity.class);
    }
}
