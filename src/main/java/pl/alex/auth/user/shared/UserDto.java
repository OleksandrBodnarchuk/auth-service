package pl.alex.auth.user.shared;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class UserDto {
    UUID id;
    String name;
    String password;
    String email;

}