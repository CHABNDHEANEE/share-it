package ru.practicum.shareit.user.dto;
import lombok.*;
import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    @NonNull
    private String name;
    @Email
    @NonNull
    private String email;
}
