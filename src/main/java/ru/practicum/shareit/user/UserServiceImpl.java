package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.ObjectCreationException;
import ru.practicum.shareit.exception.ObjectExistenceException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;

    @Override
    public UserDto addUser(UserDto user) {
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
    @Transactional
    public UserDto updateUser(Long userId, UserDto user) {
        Optional<User> userToUpdateOpt = repository.findById(userId);
        if (userToUpdateOpt.isEmpty())
            throwUserDoesntExists();

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

    private void checkUser(UserDto user) {
        Optional<User> foundedUser = repository.findByEmail(user.getEmail());
        if (foundedUser.isPresent())
            throw new ObjectCreationException("User with this email already exists");

    }

    private void throwUserDoesntExists() {
        throw new ObjectExistenceException("User doesn't exists");
    }
}