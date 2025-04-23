package ru.betuganova.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.betuganova.Dto.UserDto;
import ru.betuganova.Mapper.UserDtoMapper;
import ru.betuganova.Model.HairColor;
import ru.betuganova.Service.UserService.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/users")
@Tag(name = "User Controller", description = "Operations related to users")
public class UserController {
    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    @Autowired
    public UserController(UserService userService, UserDtoMapper userDtoMapper) {
        this.userService = userService;
        this.userDtoMapper = userDtoMapper;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "409", description = "User with the same login already exists")
    })
    @PostMapping
    public ResponseEntity<?> createUser(
            @RequestParam String login,
            @RequestParam String name,
            @RequestParam int age,
            @RequestParam String gender,
            @RequestParam HairColor hairColor) {

        userService.createUser(login, name, age, gender, hairColor);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Login user",
            description = "Logs in a user by their login"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged in successfully"),
            @ApiResponse(responseCode = "404", description = "User with the given login was not found")
    })
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String login) {
        userService.login(login);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Logout user",
            description = "Logs out the currently logged-in user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User logged out successfully")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        userService.logout();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Add a friend to the current user", description = "Adds a friend by their login to the currently logged-in user's friend list.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend added successfully"),
            @ApiResponse(responseCode = "404", description = "Friend not found"),
            @ApiResponse(responseCode = "409", description = "Friend already in the friend list or other conflict")
    })
    @PostMapping("/add-friend")
    public ResponseEntity<?> addFriend(@RequestParam String friendLogin) {
        userService.addFriend(friendLogin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(
            summary = "Delete friend",
            description = "Deletes a friend by login from the current user's friend list"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Friend successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Friend with the given login was not found")
    })
    @DeleteMapping("/delete-friend")
    public ResponseEntity<?> deleteFriend(@RequestParam String friendLogin) {
        userService.deleteFriend(friendLogin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Get user info", description = "Returns information about the currently logged-in user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user information")
    })
    @GetMapping("/info")
    public ResponseEntity<UserDto> getInfo() {
        UserDto user = userDtoMapper.toDto(userService.getUserInfo());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Operation(summary = "Get users", description = "Retrieves all users filtered by optional hair color and gender")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users")
    })
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(
            @RequestParam(name = "hairColor", required = false) String hairColor,
            @RequestParam(name = "gender", required = false) Integer gender
    ) {
        List<UserDto> users = userService.getUsersByHairColorAndGender(hairColor, (gender != null) ? gender : 0)
                .stream()
                .map(userDtoMapper::toDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @Operation(summary = "Get user's friends", description = "Returns a list of friends for a given user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of user's friends"),
            @ApiResponse(responseCode = "404", description = "User with the given ID not found")
    })
    @GetMapping("/{id}/friends")
    public ResponseEntity<List<UserDto>> getUserFriends(@PathVariable Long id) {
        List<UserDto> users = userService.getFriendsById(id)
                .stream()
                .map(userDtoMapper::toDto)
                .collect(Collectors.toList());

        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
