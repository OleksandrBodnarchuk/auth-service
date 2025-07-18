package pl.alex.auth.user.create;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CreateUserController.class)
class CreateUserControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CreateUserHandler createUserHandler;

    @Test
    void postCreateUser_return_201_withUserCreated() throws Exception {
        CreateUserRequest request = new CreateUserRequest("test", "password", "test@email.com");

        CreateUserResponse response = new CreateUserResponse(
                UUID.randomUUID(),
                request.name(),
                request.password(),
                request.email()
        );

        when(createUserHandler.handle(any(CreateUserCommand.class))).thenReturn(response);

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "test",
                                  "password": "password",
                                  "email": "test@email.com"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@email.com"));

        verify(createUserHandler, times(1)).handle(any(CreateUserCommand.class));
    }

    @Nested
    class CreateUserRequestTests {
        @Test
        void postCreateUserWithoutEmail_shouldReturn400() throws Exception {
            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "name": "test",
                                      "password": "password"
                                    }
                                    """))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void postCreateUserWithoutPassword_shouldReturn400() throws Exception {
            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "name": "test",
                                      "email": "password@gmail.com"
                                    }
                                    """))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void postCreateUserWithoutName_shouldReturn400() throws Exception {
            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "email": "test@gmail.com",
                                      "password": "password"
                                    }
                                    """))
                    .andExpect(status().isBadRequest());
        }

        @Test
        void postCreateUserWithInvalidEmail_shouldReturn400() throws Exception {
            mockMvc.perform(post("/register")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("""
                                    {
                                      "name": "test",
                                      "password": "password",
                                      "email": "test"
                                    }
                                    """))
                    .andExpect(status().isBadRequest());
        }
    }
}