package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectCreationException;
import ru.practicum.shareit.user.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final List<User> users;

    public User addUser(User user) {
        checkUser(user);

        users.add(user);
        return user;
    }

    public User getUser(Integer id) {
        return users.get(id - 1);
    }

    public User updateUser(User user) {
        checkUser(user);
        users.remove((int) user.getId() - 1);
        users.add(user);
        users.sort(Comparator.comparing(User::getId));
        return user;
    }

    public void deleteUser(Integer id) {
        users.removeIf(o -> o.getId() == id);
    }

    private void checkUser(User user) {
        if (users.stream().noneMatch(o -> o.getEmail().equals(user.getEmail()))) {
            throw new ObjectCreationException("User with this email already exists");
        }
    }
}
