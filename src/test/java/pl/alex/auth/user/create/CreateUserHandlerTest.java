package pl.alex.auth.user.create;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import pl.alex.auth.user.persistable.InMemoryUsersRepository;

import static org.junit.jupiter.api.Assertions.*;

class CreateUserHandlerTest {

    private final InMemoryUsersRepository usersRepository = new InMemoryUsersRepository();
    private final CreateUserHandler createUserHandler = new CreateUserHandler(usersRepository);

    @AfterEach
    void setup() {
        usersRepository.deleteAll();
    }

    @Test
    void when_handle_create_user_then_return_user_with_id() {
        var command = new CreateUserCommand("test", "test", "test@email.com");

        var userResponse = createUserHandler.handle(command);
        assertNotNull(userResponse);
        assertNotNull(userResponse.id());
        assertTrue(usersRepository.existsByEmail(command.getEmail()));
    }

    @Test
    void when_handle_create_user_thenThrowIfUserExists() {
        var command = new CreateUserCommand("test", "test", "test@email.com");
        assertThrows(RuntimeException.class, () -> createUserHandler.handle(command));
    }

}