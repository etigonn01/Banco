package com.siqueiros.bank.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.siqueiros.bank.dto.UserRequestDTO;
import com.siqueiros.bank.dto.UserResponseDTO;
import com.siqueiros.bank.exception.EntityNotFoundException;
import com.siqueiros.bank.model.User;
import com.siqueiros.bank.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.hamcrest.CoreMatchers;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(UserController.class)
public class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Nested
    @DisplayName("Get all users validation tests")
    class GetAllUsersValidationTests {
        @Nested
        @DisplayName("Successfully scenarios")
        class SuccessfullyScenarios {
            @Test
            @DisplayName("Should return a list raise users with 200 OK status when there are users")
            void getAllUsers_ShouldReturnAListOfUsersWith200OKStatusWhenThereAreUsers() throws Exception {
                UserResponseDTO user1 = new UserResponseDTO(
                        1L,
                        "Juan Medina Sauceda",
                        "sauceda@outlook.com",
                        "sauceda1234",
                        "7836549273",
                        LocalDateTime.now()
                );
                UserResponseDTO user2 = new UserResponseDTO(
                        1L,
                        "Gabriela Orlenas Juarez",
                        "ornelas@gmail.com",
                        "ornelas0987",
                        "8976452839",
                        LocalDateTime.now()
                );
                UserResponseDTO user3 = new UserResponseDTO(
                        1L,
                        "María Hernández Suárez",
                        "suarez@yahoo.com",
                        "hernandez5678",
                        "2910273846",
                        LocalDateTime.now()
                );
                List<UserResponseDTO> mockUsers = List.of(user1, user2, user3);
                when(userService.getAllUsers()).thenReturn(mockUsers);
                mockMvc.perform(get("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.size()").value(3))
                        .andExpect(jsonPath("$[1].fullName").value("Gabriela Orlenas Juarez"));
            }

            @Test
            @DisplayName("Should return an empty list with 200 Ok status when there are no users")
            void getAllUsers_ShouldReturnAnEmptyListWith200OkStatusWhenThereAreNoUsers() throws Exception {
                when(userService.getAllUsers()).thenReturn(List.of());
                mockMvc.perform(get("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.size()").value(0));
            }
        }
    }

    @Nested
    @DisplayName("Get user by id validation tests")
    class GetUserByIdValidationTests {
        @Nested
        @DisplayName("Successfully scenarios")
        class SuccessfullyScenarios {
            @Test
            @DisplayName("Should return a user and 200 Ok status when the Id exists")
            void getUserById_ShouldReturnAUserAnd200OkStatusWhenTheIdExists() throws Exception {
                Long userId = 1L;
                UserResponseDTO mockUser = new UserResponseDTO(
                        userId,
                        "Manuel Contreras",
                        "contreras@outlook.com",
                        "password1234",
                        "8763647282",
                        LocalDateTime.now());
                when(userService.getUserById(userId)).thenReturn(mockUser);
                mockMvc.perform(get("/api/v1/users/search/" + userId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(userId))
                        .andExpect(jsonPath("$.fullName").value("Manuel Contreras"));
            }
        }

        @Nested
        @DisplayName("Error scenarios")
        class ErrorScenarios {
            @Test
            @DisplayName("Should return 404 Not Found when the Id doesn't exist")
            void getUserById_ShouldReturn404NotFoundWhenTheIdDoesntExist() throws Exception {
                Long userId = 99L;
                String exceptionMessage = "Usuario no encontrado. Id: " + userId;
                when(userService.getUserById(userId)).thenThrow(new EntityNotFoundException(exceptionMessage));
                mockMvc.perform(get("/api/v1/users/search/" + userId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.message").value(exceptionMessage));
            }

            // test para el userdto
            @Test
            @DisplayName("Should return 400 Bad Request when the id cannot be converted to type Long")
            void getUserById_ShouldReturn400BadRequestWhenTheIdCannotBeConvertedToLong() throws Exception {
                String invalidId = "4.5a";
                String expectedExceptionMessage = "El párametro 'id' debe ser un número entero válido";
                mockMvc.perform(get("/api/v1/users/search/" + invalidId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.status").value(400))
                        .andExpect(jsonPath("$.validations.id").value(CoreMatchers.containsString(expectedExceptionMessage)))
                        .andExpect(jsonPath("$.validations.id").value(CoreMatchers.containsString(invalidId)));
            }
        }
    }

    @Nested
    @DisplayName("Get user by email tests")
    class GetUserByEmailValidationTests {
        @Nested
        @DisplayName("Successfully scenarios")
        class SuccessfullyScenarios {
            @Test
            @DisplayName("Should return 200 OK status when the email exists")
            void getUserByEmail_ShouldReturn200OkStatusWhenTheEmailExists() throws Exception {
                String email = "contreras@outlook.com";
                UserResponseDTO mockUser = new UserResponseDTO(
                        1L,
                        "Manuel Contreras",
                        "contreras@outlook.com",
                        "password1234",
                        "8763647282",
                        LocalDateTime.now());
                when(userService.getUserByEmail(email)).thenReturn(mockUser);
                mockMvc.perform(get("/api/v1/users/search?email=" + email)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.email").value(email))
                        .andExpect(jsonPath("$.fullName").value("Manuel Contreras"));
            }
        }

        @Nested
        @DisplayName("Error scenarios")
        class ErrorScenarios {
            @Test
            @DisplayName("Should return 400 Bad Request when the email is not sent")
            void getUserByEmail_ShouldReturn400BadRequestWhenTheEmailIsNotSent() throws Exception {
                String expectedMessage = "La petición no recibió el párametro requerido";
                mockMvc.perform(get("/api/v1/users/search?")
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.status").value(400))
                        .andExpect(jsonPath("$.message").value(CoreMatchers.containsString(expectedMessage)));
            }

            @Test
            @DisplayName("Should return 404 Not Found when the email does not exists")
            void getUserByEmail_ShouldReturn404NotFoundWhenTheEmailDoesNotExists() throws Exception {
                String emailNotFound = "siqueiros@apple.com";
                String expectedExceptionMessage = "Usuario no encontrado. Email: " + emailNotFound;
                when(userService.getUserByEmail(emailNotFound)).thenThrow(new EntityNotFoundException(expectedExceptionMessage));
                mockMvc.perform(get("/api/v1/users/search?email=" + emailNotFound)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.status").value(404))
                        .andExpect(jsonPath("$.message").value(expectedExceptionMessage));
            }
        }
    }

    @Nested
    @DisplayName("User registration validation tests")
    class UserRegistrationValidationTests {
        @Nested
        @DisplayName("Successfully scenarios")
        class SuccessfullyScenarios {
            @Test
            @DisplayName("Should return 201 created status and the resource created when the payload is valid")
            void createUser_ShouldReturn201CreatedStatusAndTheResourceCreatedWhenThePayloadIsValid() throws Exception {
                UserRequestDTO request = new UserRequestDTO(
                        "Pedro Siqueiros",
                        "siqueiros@gmail.com",
                        "siqueiros2039",
                        "7653847263"
                );
                UserResponseDTO response = new UserResponseDTO(
                        1L,
                        "Pedro Siqueiros",
                        "siqueiros@gmail.com",
                        "siqueiros2039",
                        "7653847263",
                        LocalDateTime.now()
                );
                when(userService.createUser(any(UserRequestDTO.class))).thenReturn(response);
                mockMvc.perform(post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.id").value(1L))
                        .andExpect(jsonPath("$.fullName").value("Pedro Siqueiros"));
            }
        }

        @Nested
        @DisplayName("Error scenarios")
        class ErrorScenarios {
            @Test
            @DisplayName("Should return 400 Bad Request whent the fullName length is less than 8 digits")
            void createUser_ShouldReturn400BadRequestWhenTheFullNameLengthIsLessThan8Digits() throws Exception {
                UserRequestDTO invalidRequest = new UserRequestDTO(
                        "Pedro",
                        "siqueiros@gmail.com",
                        "siqueiros2039",
                        "7653847263"
                );
                mockMvc.perform(post("/api/v1/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(invalidRequest)))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").value("Los datos enviados en la petición no cumplen con los requisitos"))
                        .andExpect(jsonPath("$.validations.fullName").value("El nombre debe tener entre 8 y 120 caracteres"));
                verifyNoInteractions(userService);
            }
        }

    }

    @Nested
    @DisplayName("User update validation tests")
    class UserUpdateValidationTests {
        @Nested
        @DisplayName("Successfully scenarios")
        class UserUpdateScenarios {
            @Test
            @DisplayName("Should return 200 OK status and update if the user retains the same unique data")
            void updateUser_ShouldReturn200OKStatusAndUpdateIfTheUserRetainsTheSameUniqueData() throws Exception {
                Long userId = 99L;
                UserRequestDTO request = new UserRequestDTO(
                        "María Rosario Espinoza Burgos",
                        "rosario@apple.com",
                        "password1234",
                        "7658374728"
                );
                UserResponseDTO response = new UserResponseDTO(
                        userId,
                        request.fullName(),
                        request.email(),
                        request.passwordHash(),
                        request.phoneNumber(),
                        LocalDateTime.now()
                );

                when(userService.updateUser(eq(userId), any(UserRequestDTO.class))).thenReturn(response);

                mockMvc.perform(put("/api/v1/users/" + userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(userId))
                        .andExpect(jsonPath("$.email").value("rosario@apple.com"));
            }
        }

        @Nested
        @DisplayName("Error scenarios")
        class UserUpdateErrorScenarios {
            @Test
            @DisplayName("Should return 404 Not Found when the id does not exists and the payload is valid")
            void updateUser_ShouldReturn404BadRequestWhenTheIdDoesNotExistsAndThePayloadIsValid() throws Exception {
                long userId = 100L;
                String errorMessage = "Usuario no encontrado. Id: " + userId;
                UserRequestDTO request = new UserRequestDTO(
                        "María Rosario Espinoza Burgos",
                        "rosario@apple.com",
                        "password1234",
                        "7658374728"
                );

                when(userService.updateUser(eq(userId), any(UserRequestDTO.class))).thenThrow(new EntityNotFoundException(errorMessage));
                mockMvc.perform(put("/api/v1/users/" + userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.status").value(404))
                        .andExpect(jsonPath("$.message").value(errorMessage));
            }

            @Test
            @DisplayName("Should return 400 Bad Request when the payload is invalid")
            void updateUser_ShouldReturn400BadRequestWhenThePayloadIsInvalid() throws Exception {
                long userID = 1L;
                var invalidRequest = new UserRequestDTO("María Rosario Hernandez", "rosarios.apple.com", "password1234", "7658374728");

                mockMvc.perform(put("/api/v1/users/" + userID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.status").value(400))
                        .andExpect(jsonPath("$.validations.email").value("El formato del correo electrónico no es válido"));
                verifyNoInteractions(userService);
            }

            @Test
            @DisplayName("Should throw DataIntegrityViolation when the email belongs to another user")
            void updateUser_ShouldThrowDataIntegrityViolationWhenTheEmailBelongsToAnotherUser() throws Exception {
                long userId = 99L;
                User originalUser = new User(userId, "Maria Cortes", "cortes.maria@apple.com", "password1234", "9997778833", LocalDateTime.now());

                var request = new UserRequestDTO("Maria Sanchez Cortes", "cortes.maria@apple.com", "password4321", "9998887364");

                when(userService.updateUser(eq(userId), any(UserRequestDTO.class)))
                        .thenThrow(new DataIntegrityViolationException("El correo electrónico ya está registrado por otro usuario"));

                mockMvc.perform(put("/api/v1/users/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isConflict())
                        .andExpect(jsonPath("$.status").value(409))
                        .andExpect(jsonPath("$.error").value("Conflict"))
                        .andExpect(jsonPath("$.message").value("El correo electrónico ya está registrado por otro usuario"));
            }

            @Test
            @DisplayName("Should throw DataIntegrityViolation when the phone number belongs to another user")
            void updateUser_ShouldThrowDataIntegrityViolationWhenThePhoneNumberBelongsToAnotherUser() throws Exception {
                long userId = 99L;
                var request = new UserRequestDTO("Maria Sanchez Cortes", "sanchez.maria@gmail.com", "password4321", "9997778833");

                when(userService.updateUser(eq(userId), any(UserRequestDTO.class)))
                        .thenThrow(new DataIntegrityViolationException("El número de télefono ya está registrado por otro usuario"));

                mockMvc.perform(put("/api/v1/users/" + userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isConflict())
                        .andExpect(jsonPath("$.status").value(409))
                        .andExpect(jsonPath("$.error").value("Conflict"))
                        .andExpect(jsonPath("$.message").value("El número de télefono ya está registrado por otro usuario"));
            }
        }
    }

    @Nested
    @DisplayName("UserDeleteTests")
    class  UserDeleteTests {
        @Nested
        @DisplayName("Successfully scenarios")
        class UserDeleteScenarios {
            @Test
            @DisplayName("Should return 200 OK status when the id exists")
            void  deleteUser_ShouldReturn200OKStatusWhenTheIdExistsAndThePayloadIsValid() throws Exception {
                long userId = 1L;
                UserResponseDTO userDeleted = new UserResponseDTO(
                        userId,
                        "Graciela Ramirez",
                        "ramirezgraciela@yahoo.com",
                        "password1234",
                        "8734672829",
                        LocalDateTime.now()
                );
                when(userService.deleteUser(userId)).thenReturn(userDeleted);
                mockMvc.perform(delete("/api/v1/users/" + userId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(userId))
                        .andExpect(jsonPath("$.phoneNumber").value("8734672829"));
                ;
            }
        }

        @Nested
        @DisplayName("Error scenarios")
        class  UserDeleteErrorScenarios {
            @Test
            @DisplayName("Should return 404 Not Found when id does not exists")
            void deleteUser_ShouldReturn404NotFoundWhenTheIDDoesNotExists()  throws Exception {
                long userId = 100L;
                String errorMessage = "Usuario no encontrado. Id: " + userId;
                when(userService.deleteUser(eq(userId))).thenThrow(new EntityNotFoundException(errorMessage));
                mockMvc.perform(delete("/api/v1/users/" + userId)
                                .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.status").value(404))
                        .andExpect(jsonPath("$.message").value(errorMessage));
            }
        }
    }
}
