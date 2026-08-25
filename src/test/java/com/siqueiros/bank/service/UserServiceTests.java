package com.siqueiros.bank.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.siqueiros.bank.dto.UserRequestDTO;
import com.siqueiros.bank.dto.UserResponseDTO;
import com.siqueiros.bank.exception.EntityNotFoundException;
import com.siqueiros.bank.model.User;
import com.siqueiros.bank.repositories.UserRepository;
import com.siqueiros.bank.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Unit tests for UserService")
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    @DisplayName("getAllUsers tests")
    class GetAllUsersTests {
        @Nested
        @DisplayName("Successfully scenarios")
        class SuccessFullyScenarios{
            @Test
            @DisplayName("Should return a list of DTOs when there are users")
            void getAllUsers_ShouldReturnAListOfDTOsWhenThereAreUsers() {
                var userOne = new User(1L, "Karen Barrera", "barrera@gmail.com", "barrera2903", "4567862323", LocalDateTime.now());
                var userTwo = new User(2L, "Mariano Avila", "marianoavila@apple.com", "mariano2893", "7485839292", LocalDateTime.now());
                when(userRepository.findAll()).thenReturn(List.of(userOne, userTwo));

                List<UserResponseDTO> result = userService.getAllUsers();

                assertNotNull(result, "La lista de retorno nunca debe ser nula");
                assertEquals(2, result.size(), "La lista debe ser exactamente 2 usuarios");

                assertEquals(1L, result.get(0).id());
                assertEquals("Karen Barrera", result.get(0).fullName());

                assertEquals(2L,  result.get(1).id());
                assertEquals("Mariano Avila", result.get(1).fullName());

                verify(userRepository, times(1)).findAll();
            }
        }

        @Nested
        @DisplayName("Error scenarios")
        class  ErrorScenarios{
            @Test
            @DisplayName("Should return an empty list when there are no users")
            void getAllUsers_ShouldReturnAnEmptyListWhenThereAreNoUsers() {
                when(userRepository.findAll()).thenReturn(List.of());

                List<UserResponseDTO> result = userService.getAllUsers();

                assertNotNull(result);
                assertTrue(result.isEmpty(), "La lista devuelta por el servicio debería estar vacía");
                verify(userRepository, times(1)).findAll();
            }
        }
    }

    @Nested
    @DisplayName("GetUserById tests")
    class GetUserByIdTests {
        @Nested
        @DisplayName("Successfully scenarios")
        class SuccessfullyScenarios{
            @Test
            @DisplayName("Should return a UserResponseDTO when the id exists")
            void getUserById_ShouldReturnAUserResponseDTOWhenTheIdExists() {
                long userId = 1L;
                User mockUser = new User(userId, "Mariana Rodirguez", "rodriguez@didi.com", "rodriguez1234", "2237653482", LocalDateTime.now());
                when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

                UserResponseDTO result = userService.getUserById(userId);

                assertNotNull(result, "El resultado no debe ser nulo");
                assertEquals(userId, result.id());
                assertEquals("Mariana Rodirguez", result.fullName());

                verify(userRepository, times(1)).findById(userId);
            }
        }

        @Nested
        @DisplayName("Error scenarios")
        class ErrorScenarios{
            @Test
            @DisplayName("Should throw EntityNotFoundException with correct message when the Id does not exist")
            void getUserById_ShouldThrowEntityNotFoundExceptionWhenTheIdDoesNotExist() {
                long userId = 1L;
                String expectedMessage = "Usuario no encontrado. Id: " + userId;

                when(userRepository.findById(userId)).thenReturn(Optional.empty());

                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> userService.getUserById(userId),
                        "Se esperaba que el método lanzara EntityNotFoundException");

                assertEquals(expectedMessage, ex.getMessage());
                verify(userRepository, times(1)).findById(userId);
            }
        }
    }

    @Nested
    @DisplayName("createUser tests")
    class CreateUserTests {
        @Nested
        @DisplayName("Successfully scenarios")
        class SuccessfullyScenarios{
            @Test
            @DisplayName("Should save the user and return the DTO with and ID")
            void createUser_ShouldSaveTheDTOAndReturnTheDTOWithAndID() {
                var requestDTO = new UserRequestDTO(
                        "Jaime Bonilla",
                        "bonilla_12@uber.com",
                        "bonilla12345",
                        "4443782736"
                );

                User savedUser = new User(
                        1L,
                        requestDTO.fullName(),
                        requestDTO.email(),
                        requestDTO.passwordHash(),
                        requestDTO.phoneNumber(),
                        LocalDateTime.now()
                );

                when(userRepository.save(any(User.class))).thenReturn(savedUser);

                UserResponseDTO result = userService.createUser(requestDTO);

                assertNotNull(result, "El DTO de respuesta no debe ser nulo");
                assertEquals(1L, result.id(), "El ID devuelto debe coincidir con el asignado por el repo");
                assertEquals(requestDTO.fullName(), result.fullName());

                verify(userRepository, times(1)).save(any(User.class));
            }
        }
    }

    @Nested
    @DisplayName("deleteUser tests")
    class DeleteUserTests {
        @Nested
        @DisplayName("Successfully scenarios")
        class SuccessfullyScenarios{
            @Test
            @DisplayName("Should logicalDelete the user and return a DTO when the Id exists")
            void deleteUser_ShouldDeleteTheDTOAndReturnTheDTOWithAndID() {
                long userId = 1L;
                var mockUser = new User(userId, "Norma Guzman", "guzman.norma@hotmail.com", "password1234", "3432637282", LocalDateTime.now());

                when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
                doNothing().when(userRepository).delete(mockUser);

                UserResponseDTO result = userService.deleteUser(userId);

                assertNotNull(result, "El DTO de respuesta no debe ser nulo");
                assertEquals(userId, result.id());
                assertEquals("Norma Guzman", result.fullName());

                verify(userRepository, times(1)).findById(userId);
                verify(userRepository, times(1)).delete(mockUser);
            }
        }

        @Nested
        @DisplayName("Error scenarios")
        class ErrorScenarios{
            @Test
            @DisplayName("Should throw EntityNotFoundException and do not attempt to logicalDelete if the id does not exists")
            void deleteUser_ShouldThrowEntityNotFoundExceptionAndDoNotAttemptToDeleteIfIdDoesNotExist() {
                long userId = 1L;
                String  expectedMessage = "Usuario no encontrado. Id: " + userId;

                when(userRepository.findById(userId)).thenReturn(Optional.empty());

                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(userId),
                        "Se esperaba que el método lanzara EntityNotFoundException");

                assertEquals(expectedMessage, ex.getMessage());
                verify(userRepository, times(1)).findById(userId);
                verify(userRepository, times(0)).delete(any(User.class));
            }
        }
    }

    @Nested
    @DisplayName("getUserByEmail tests")
    class getUerByEmailTests {
        @Nested
        @DisplayName("Successfully scenarios")
        class SuccessfullyScenarios{
            @Test
            @DisplayName("Should return a DTO when the email exists")
            void getUserByEmail_ShouldReturnTheDTOAndReturnTheDTOWithAndID() {
                String targetEmail = "lorena_campos@apple.com";
                User mockUser = new User(1L, "Lorena Campos", targetEmail,"password1234" ,"9876542323", LocalDateTime.now());

                when(userRepository.findByEmail(targetEmail)).thenReturn(Optional.of(mockUser));

                UserResponseDTO result = userService.getUserByEmail(targetEmail);

                assertNotNull(result, "El resultado no debe ser nulo");
                assertEquals(1L, result.id());
                assertEquals("Lorena Campos", result.fullName());
                assertEquals(targetEmail, result.email());

                verify(userRepository, times(1)).findByEmail(targetEmail);
            }
        }

        @Nested
        @DisplayName("Error scenarios")
        class ErrorScenarios{
            @Test
            @DisplayName("Should throw EntityNotFoundException when the email does not exists")
            void getUserByEmail_ShouldThrowEntityNotFoundExceptionWhenTheEmailDoesNotExist() {
                String targetEmail = "lorenacampos_20@apple.com";
                String expectedMessage = "Usuario no encontrado. Email: " + targetEmail;

                when(userRepository.findByEmail(targetEmail)).thenReturn(Optional.empty());

                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> userService.getUserByEmail(targetEmail),
                        "Se esperaba que el método lanzara EntityNotFoundException");

                assertEquals(expectedMessage, ex.getMessage());

                verify(userRepository, times(1)).findByEmail(targetEmail);
            }
        }
    }

    @Nested
    @DisplayName("updateUser tests")
    class UpdateUserTests {
        @Nested
        @DisplayName("Successfully scenarios")
        class SuccessfullyScenarios{
            @Test
            @DisplayName("Should modify the user and return a DTO when the id exists")
            void updateUser_ShouldReturnTheDTOAndReturnTheDTOWithAndID() {
                long userId = 1L;
                var originalUser = new User(
                        userId,
                        "Juan Ramirez",
                        "juan.ramirez@gmail.com",
                        "password1234" ,
                        "8736732828",
                        LocalDateTime.now()
                );

                var requestDTO = new UserRequestDTO(
                        "Juan Gonzalez",
                        "gonzalez.juan@gmail.com",
                        "password4321",
                        "3334447283"
                );

                var updatedUser = new User(
                        userId,
                        requestDTO.fullName(),
                        requestDTO.email(),
                        requestDTO.passwordHash(),
                        requestDTO.phoneNumber(),
                        LocalDateTime.now()
                );

                when(userRepository.findById(userId)).thenReturn(Optional.of(originalUser));
                when(userRepository.save(any(User.class))).thenReturn(updatedUser);

                UserResponseDTO result = userService.updateUser(userId, requestDTO);

                assertNotNull(result, "El DTO devuelto tras la actualización no debe ser nulo");
                assertEquals(userId, result.id());
                assertEquals("Juan Gonzalez", result.fullName());

                verify(userRepository, times(1)).findById(userId);
                verify(userRepository, times(1)).save(originalUser);
            }

        }

        @Nested
        @DisplayName("Error scenarios")
        class ErrorScenarios{
            @Test
            @DisplayName("Should throw EntityNotFoundException when the id does not exists")
            void updateUser_ShouldThrowEntityNotFoundExceptionWhenTheIdDoesNotExist() {
                long userId = 99L;
                String expectedMessage = "Usuario no encontrado. Id: " + userId;
                var requestDTO = new UserRequestDTO(
                        "Graciano Perez",
                        "graciano.perez@outlook.com",
                        "password4566",
                        "8763294638"
                );

                when(userRepository.findById(userId)).thenReturn(Optional.empty());

                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> userService.updateUser(userId, requestDTO),
                        "Se esperaba que lanzara EntityNotFoundException");

                assertEquals(expectedMessage, ex.getMessage());

                verify(userRepository, times(1)).findById(userId);
                verify(userRepository, times(0)).save(any(User.class));
            }
        }
    }
}
