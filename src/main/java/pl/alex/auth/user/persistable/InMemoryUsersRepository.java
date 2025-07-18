package pl.alex.auth.user.persistable;

import pl.alex.auth.user.create.CreateUserCommand;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUsersRepository implements UsersRepository {

    private final Map<UUID, UserEntity> inMemoryUsers = new ConcurrentHashMap<>();

    @Override
    public UserEntity findById(UUID id) {
        return inMemoryUsers.get(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return inMemoryUsers.values().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));
    }

    @Override
    public UserEntity saveUser(CreateUserCommand command) {
        UserEntity user = UserEntity.builder()
                .id(UUID.randomUUID())
                .email(command.getEmail())
                .password(command.getPassword())
                .name(command.getName())
                .build();
        inMemoryUsers.put(user.getId(), user);
        return user;
    }

    @Override
    public List<UserEntity> findAll() {
        return (List<UserEntity>) inMemoryUsers.values();
    }

    public void deleteAll() {
        inMemoryUsers.clear();
    }
}
