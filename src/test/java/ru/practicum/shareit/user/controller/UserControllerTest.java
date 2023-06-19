package ru.practicum.shareit.user.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.impl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import ru.practicum.shareit.item.comment.dto.CommentDto;
import ru.practicum.shareit.item.comment.service.impl.CommentServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.impl.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static ru.practicum.shareit.auxilary.RequestWithJson.patchJson;
import static ru.practicum.shareit.auxilary.RequestWithJson.postJson;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserServiceImpl userService;

    private UserDto user1;
    private UserDto userResult;
    @BeforeEach
    void beforeEach() {
        user1 = UserDto.builder()
                .name("user1 name")
                .email("user1@mail.com")
                .build();

        userResult = UserDto.builder()
                .id(1L)
                .name("user1 name")
                .email("user1@mail.com")
                .build();
    }

    @Test
    public void addUserTest() throws Exception {
        when(userService.addUser(user1)).thenReturn(userResult);

        checkItemProp(mockMvc.perform(postJson("/users", user1)));
    }

    @Test
    public void getUserTest() throws Exception {
        when(userService.getUser(1L)).thenReturn(userResult);

        checkItemProp(mockMvc.perform(get("/users/1")));
    }

    @Test
    public void updateUserTest() throws Exception {
        user1.setEmail("updated@mail.com");
        user1.setName("updated name");
        userResult.setName("updated name");
        userResult.setEmail("updated@mail.com");
        when(userService.updateUser(1L, user1)).thenReturn(userResult);

        MvcResult mvcResult = checkItemProp(mockMvc.perform(patchJson("/users/1", user1)));

        UserDto result = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), UserDto.class);

        assertThat(result.getId(), is(1L));
        assertThat(result.getName(), is("updated name"));
        assertThat(result.getEmail(), is("updated@mail.com"));
    }

    @Test
    public void deleteUserTest() throws Exception {
        doNothing().when(userService);

        mockMvc.perform(delete("/users/1"));
    }

    @Test
    public void getAllTest() throws Exception {
        when(userService.getAll()).thenReturn(List.of(userResult));

        checkItemListProp(mockMvc.perform(get("/users")));
    }

    private static MvcResult checkItemProp(ResultActions request) throws Exception {
        return request.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").isString())
                .andExpect(jsonPath("$.email").isString())
                .andReturn();
    }

    private static void checkItemListProp(ResultActions request) throws Exception {
        request.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").isNumber())
                .andExpect(jsonPath("$[0].name").isString())
                .andExpect(jsonPath("$[0].email").isString());
    }
}
