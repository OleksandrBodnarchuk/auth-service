package pl.alex.auth.user.persistable;

import java.util.Optional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import pl.alex.auth.user.create.CreateUserCommand;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class JpaUsersRepository implements UsersRepository {

    JpaUsersDao jpaUsersDao;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserEntity findById(UUID id) {
        return jpaUsersDao.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUsersDao.existsByEmail(email);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return jpaUsersDao.findByEmail(email);
    }

    @Override
    public UserEntity saveUser(CreateUserCommand command) {
        UserEntity user = UserEntity.builder()
                .email(command.getEmail())
                .name(command.getName())
                .password(bCryptPasswordEncoder.encode(command.getPassword()))
                .build();
        return jpaUsersDao.save(user);
    }

    @Override
    public List<UserEntity> findAll() {
        return jpaUsersDao.findAll();
    }
}
