package pl.alex.auth.user.persistable;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.alex.auth.user.shared.UserDto;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String password;
    private String email;

    public UserDto toDto() {
        return UserDto.builder()
                .id(id)
                .name(name)
                .password(password)
                .email(email)
                .build();
    }
}
