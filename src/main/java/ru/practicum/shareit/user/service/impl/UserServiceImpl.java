package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectCreationException;
import ru.practicum.shareit.exception.ObjectExistenceException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private long curId = 1;

    @Override
    public UserDto addUser(UserDto user) {
        checkUser(curId, user);
        user.setId(curId);
        return UserMapper.toUserDto(repository.save(UserMapper.toUser(user)));
    }

    @Override
    public UserDto getUser(Long id) {
        Optional<User> user = repository.findById(id);
        if (user.isEmpty())
            throwUserDoesntExists();
        return UserMapper.toUserDto(user.get());
    }

    @Override
    public UserDto updateUser(Long userId, UserDto user) {
        Optional<User> userToUpdateOpt = repository.findById(userId);
        if (userToUpdateOpt.isEmpty())
            throwUserDoesntExists();
        checkUser(userId, user);

        User userToUpdate = userToUpdateOpt.get();

        userToUpdate.setEmail(user.getEmail() == null ? userToUpdate.getEmail() : user.getEmail());
        userToUpdate.setName(user.getName() == null ? userToUpdate.getName() : user.getName());

        return UserMapper.toUserDto(repository.save(userToUpdate));
    }

    @Override
    public void deleteUser(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<UserDto> getAll() {
        return repository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    private void checkUser(Long userId, UserDto user) {
        Optional<User> foundedUser = repository.findByEmail(user.getEmail());
        if (foundedUser.isEmpty() || !foundedUser.get().getEmail().equals(user.getEmail()))
            throw new ObjectCreationException("User with this email already exists");

    }

    private void throwUserDoesntExists() {
        throw new ObjectExistenceException("User doesn't exists");
    }
}