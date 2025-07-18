package pl.alex.auth.user.persistable;

import pl.alex.auth.user.create.CreateUserCommand;

import java.util.List;
import java.util.UUID;

public interface UsersRepository {

    UserEntity findById(UUID id);

    boolean existsByEmail(String email);

    UserEntity saveUser(CreateUserCommand command);

    List<UserEntity> findAll();
}
