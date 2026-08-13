package com.siqueiros.bank.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.siqueiros.bank.dto.UserRequestDTO;
import com.siqueiros.bank.dto.UserResponseDTO;
import com.siqueiros.bank.exception.EntityNotFoundException;
import com.siqueiros.bank.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Nested
    @DisplayName("Scenarios for success")
    class ScenariosForSuccess {
        @Test
        @DisplayName("Should return a list of users with 200 OK status")
        void getAllUsers_ShouldReturnAListOfUsersWith200OKStatus() throws Exception {
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
    @DisplayName("ErrorScenarios")
    class ErrorScenarios {

        @Test
        @DisplayName("Should return 404 Not Found when the Id doesn't exist")
        void getUserById_ShouldReturn404NotFoundWhenTheIdDoesntExist() throws Exception {
            Long userId = 99L;
            String exceptionMessage = "Usuario no encontrado. Id: " +  userId;
            when(userService.getUserById(userId)).thenThrow(new EntityNotFoundException(exceptionMessage));
            mockMvc.perform(get("/api/v1/users/search/" + userId)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value(exceptionMessage));
        }

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

        @Test
        @DisplayName("Should return 400 Bad Request when the fullName length is greater than 120 digits")
        void createUser_ShouldReturn400BadRequestWhenTheFullNameLengthIsGreaterThan120Digits() throws Exception {
            UserRequestDTO invalidRequest = new UserRequestDTO(
                    "RxbQgqarUAYxJyfftUykgJHcMxcLuDpXtJVQqTxmepvtFxdCFwjFMRhRKncqNwnfNbYTmYyzchNhLvfHCCaEZiDpVZkDrbrNrvQXxSuYwEpDtApqSbjZKfmtK",
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

        @Test
        @DisplayName("Should return 400 Bad Request when the email format is invalid")
        void createUser_ShouldReturn400BadRequestWhenTheEmailFormatIsInvalid() throws Exception {
            UserRequestDTO invalidRequest = new UserRequestDTO(
                    "Pedro Siqueiros",
                    "siqueiros.gmail.com",
                    "siqueiros2039",
                    "7653847263"
            );
            mockMvc.perform(post("/api/v1/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Los datos enviados en la petición no cumplen con los requisitos"))
                    .andExpect(jsonPath("$.validations.email").value("El formato del correo electrónico no es valido"));
            verifyNoInteractions(userService);
        }

        @Test
        @DisplayName("Should return 400 Bad Request when the email length is less than 8 digits")
        void createUser_ShouldReturn400BadRequestWhenTheEmailLengthIsLessThan8Digits() throws Exception {
            UserRequestDTO invalidRequest = new UserRequestDTO(
                    "Pedro Siqueiros",
                    "s@a.com",
                    "siqueiros2039",
                    "7653847263"
            );
            mockMvc.perform(post("/api/v1/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Los datos enviados en la petición no cumplen con los requisitos"))
                    .andExpect(jsonPath("$.validations.email").value("El correo electrónico debe tener entre 8 y 50 caracteres"));
            verifyNoInteractions(userService);
        }

        @Test
        @DisplayName("Should return 400 Bad Request when the email length is greater than 50 digits")
        void createUser_ShouldReturn400BadRequestWhenTheEmailLengthIsGreaterThan50Digits() throws Exception {
            UserRequestDTO invalidRequest = new UserRequestDTO(
                    "Pedro Siqueiros",
                    "RxbQgqarUAYxJyfftUykgJHcMxcLuDpXtJVQqTxmepvtFxdCFwjFMRhRKncqNwnfNbYTmYyzchNhLvfHCCaEZiDpVZkDrbrNrvQXxSuYwEpDtApqSbjZKfmtK@gmail.com",
                    "siqueiros2039",
                    "7653847263"
            );
            mockMvc.perform(post("/api/v1/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Los datos enviados en la petición no cumplen con los requisitos"))
                    .andExpect(jsonPath("$.validations.email").value("El correo electrónico debe tener entre 5 y 50 caracteres"));
            verifyNoInteractions(userService);
        }

        @Test
        @DisplayName("Should return 400 Bad Request when the password hash is empty")
        void createUser_ShouldReturn400BadRequestWhenThePasswordHashIsEmpty() throws Exception {
            UserRequestDTO invalidRequest = new UserRequestDTO(
                    "Pedro Siqueiros",
                    "siqueiros@gmail.com",
                    "",
                    "7653847263"
            );
            mockMvc.perform(post("/api/v1/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Los datos enviados en la petición no cumplen con los requisitos"))
                    .andExpect(jsonPath("$.validations.passwordHash").value("El hash de la contraseña no puede estar vació"));
            verifyNoInteractions(userService);
        }

        @Test
        @DisplayName("Should return 400 Bad Request when the phoneNumber is different than 10 digits")
        void createUser_ShouldReturn400BadRequestWhenThePhoneNumberIsDifferentThan10Digits() throws Exception {
            UserRequestDTO invalidRequest = new UserRequestDTO(
                    "Pedro Siqueiros",
                    "siqueiros@gmail.com",
                    "password1234",
                    "892738272"
            );
            mockMvc.perform(post("/api/v1/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(invalidRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.message").value("Los datos enviados en la petición no cumplen con los requisitos"))
                    .andExpect(jsonPath("$.validations.phoneNumber").value("El número de teléfono debe ser un formato de 10 dígitos"));
            verifyNoInteractions(userService);
        }
    }
}
