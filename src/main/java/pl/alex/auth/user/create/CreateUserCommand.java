package pl.alex.auth.user.create;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateUserCommand {

    String name;
    String password;
    String email;

    static CreateUserCommand from(CreateUserRequest createUserRequest) {
        return CreateUserCommand.builder()
                .name(createUserRequest.login())
                .password(createUserRequest.password())
                .email(createUserRequest.email())
                .build();
    }
}
