package pl.alex.auth.user.create;

import lombok.Builder;
import pl.alex.auth.user.shared.UserDto;

import java.util.UUID;

@Builder
public record CreateUserResponse(UUID id, String name, String email) {
    public static CreateUserResponse from(UserDto userDto) {
        return CreateUserResponse.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }
}
