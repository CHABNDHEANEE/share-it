package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectCreationException;
import ru.practicum.shareit.exception.ObjectExistenceException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final Map<Long, User> users;
    private long curId = 1;

    @Override
    public UserDto addUser(UserDto user) {
        checkUser(curId, user);
        user.setId(curId);
        users.put(curId, UserMapper.toUser(user));
        return getUser(curId++);
    }

    @Override
    public UserDto getUser(Long id) {
        if (!users.containsKey(id))
            throwUserDoesntExists();
        return UserMapper.toUserDto(users.get(id));
    }

    @Override
    public UserDto updateUser(Long userId, UserDto user) {
        if (!users.containsKey(userId))
            throwUserDoesntExists();
        checkUser(userId, user);

        User userToUpdate = users.get(userId);

        userToUpdate.setEmail(user.getEmail() == null ? userToUpdate.getEmail() : user.getEmail());
        userToUpdate.setName(user.getName() == null ? userToUpdate.getName() : user.getName());

        users.put(userId, userToUpdate);
        return getUser(userId);
    }

    @Override
    public void deleteUser(Long id) {
        if (users.remove(id) == null)
            throwUserDoesntExists();
    }

    @Override
    public List<UserDto> getAll() {
        return users.values().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    private void checkUser(Long userId, UserDto user) {
        if (users.values().stream()
                .anyMatch(o -> o.getEmail().equals(user.getEmail()) && userId != o.getId()))
            throw new ObjectCreationException("User with this email already exists");
    }

    private void throwUserDoesntExists() {
        throw new ObjectExistenceException("User doesn't exists");
    }
}
