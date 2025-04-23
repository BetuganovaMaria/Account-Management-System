package ru.betuganova.Mapper;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.betuganova.Dto.UserDto;
import ru.betuganova.Model.User;

@Component
public class UserDtoMapper {
    private final ModelMapper modelMapper;

    public UserDtoMapper() {
        this.modelMapper = new ModelMapper();
        configureMappings();
    }

    private void configureMappings() {
        Converter<Integer, String> toGenderString = ctx -> {
            Integer gender = ctx.getSource();
            if (gender == null) return "Undetermined";
            if (gender == 1) return "Male";
            if (gender == 2) return "Female";
            return "Undetermined";
        };

        Converter<String, Integer> toGenderInt = ctx -> {
            String gender = ctx.getSource();
            if (gender == null) return 0;
            if (gender.equalsIgnoreCase("Male")) return 1;
            if (gender.equalsIgnoreCase("Female")) return 2;
            return 0;
        };

        modelMapper.typeMap(User.class, UserDto.class).addMappings(mapper ->
                mapper.using(toGenderString).map(User::getGender, UserDto::setGender)
        );

        modelMapper.typeMap(UserDto.class, User.class).addMappings(mapper ->
                mapper.using(toGenderInt).map(UserDto::getGender, User::setGender)
        );
    }

    public UserDto toDto(User user) {
        return user == null ? null : modelMapper.map(user, UserDto.class);
    }

    public User toModel(UserDto user) {
        return user == null ? null : modelMapper.map(user, User.class);
    }
}
