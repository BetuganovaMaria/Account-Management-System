package ru.betuganova.Controller.Mapper;

import org.modelmapper.ModelMapper;
import ru.betuganova.Controller.Dto.UserDto;
import ru.betuganova.Service.Model.User;

public class UserDtoMapper {
    private final ModelMapper modelMapper;

    public UserDtoMapper() {
        this.modelMapper = new ModelMapper();
    }

    public UserDto toDto(User user) {
        return user == null ? null : modelMapper.map(user, UserDto.class);
    }

    public User toModel(UserDto user) {
        return user == null ? null : modelMapper.map(user, User.class);
    }
}
