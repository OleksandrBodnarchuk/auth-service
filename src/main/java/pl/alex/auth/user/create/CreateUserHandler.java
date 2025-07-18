package pl.alex.auth.user.create;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.alex.auth.user.persistable.UsersRepository;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
class CreateUserHandler {

    UsersRepository usersRepository;

    public CreateUserHandler(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    CreateUserResponse handle(CreateUserCommand command) {
        try {
            if (usersRepository.existsByEmail(command.getEmail())) {
                throw new RuntimeException("User already exists");
            }
            return CreateUserResponse.from(usersRepository.saveUser(command).toDto());
        } catch (Exception e) {
            log.error("[{}] threw exception: {}", this.getClass().getSimpleName(), e.getMessage());
            throw new RuntimeException(e); // TODO: update to some DomainException with status codes
        }
    }
}
