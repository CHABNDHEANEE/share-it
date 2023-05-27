package ru.practicum.shareit.user;

import ru.practicum.shareit.user.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(UserDto user);

    UserDto getUser(Long id);

    UserDto updateUser(Long userId, UserDto user);

    void deleteUser(Long id);

    List<UserDto> getAll();
}
