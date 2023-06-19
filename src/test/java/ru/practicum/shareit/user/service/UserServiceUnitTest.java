package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.exception.ObjectExistenceException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repo.UserRepository;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private User user1;

    @BeforeEach
    void beforeEach() {
        user1 = User.builder()
                .id(1)
                .name("user1 name")
                .email("user1@testmail.com")
                .build();

    }

    @Test
    public void getUser_shouldReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        UserDto result = userService.getUser(1L);

        verify(userRepository, times(1)).findById(1L);
        assertUsers(user1, result);
    }

    @Test
    public void updateUserName() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user1));

        UserDto changedUser = UserDto.builder()
                .id(1)
                .name("user1 changed name")
                .email(null)
                .build();

        when(userRepository.save(any())).thenReturn(user1);

        UserDto result = userService.updateUser(1L, changedUser);

        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any());
        assertUsers(user1, result);
    }

    @Test
    public void updateUserName_ForWrongUserId_ShouldThrowException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        ObjectExistenceException exception = assertThrows(ObjectExistenceException.class, () ->
                userService.updateUser(99L, UserDto.builder().build()));

        assertThat(exception.getMessage(), is("User doesn't exists"));
    }

    private void assertUsers(User input, UserDto output) {
        assertThat(input.getId(), is(output.getId()));
        assertThat(input.getEmail(), is(output.getEmail()));
        assertThat(input.getName(), is(output.getName()));
    }
}
