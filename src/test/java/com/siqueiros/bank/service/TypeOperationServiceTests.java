package com.siqueiros.bank.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.siqueiros.bank.dto.TypeOperationRequestDTO;
import com.siqueiros.bank.dto.TypeOperationResponseDTO;
import com.siqueiros.bank.exception.EntityNotFoundException;
import com.siqueiros.bank.model.TypeOperation;
import com.siqueiros.bank.repositories.TypeOperationRepository;
import com.siqueiros.bank.service.impl.TypeOperationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@DisplayName("Type Operation Service Unit Tests")
public class TypeOperationServiceTests {

    @Mock
    private TypeOperationRepository typeOperationRepository;

    @InjectMocks
    private TypeOperationServiceImpl typeOperationService;

    @Nested
    @DisplayName("getAllTypeOperation Tests")
    class GetAllTypeOperationTests {
        @Nested
        @DisplayName("Successfully scenarios")
        class SuccessfullyScenarios {
            @Test
            @DisplayName("Should return a list of DTOs when there are type operations")
            void getALlTypeOperations_ShouldReturnAListOfDTOsWhenThereAreTypeOperations() {
                var typeOperation1 = new TypeOperation(1L, "deposito");
                var typeOperation2 = new TypeOperation(2L, "retiro");

                when(typeOperationRepository.findAll()).thenReturn(List.of(typeOperation1, typeOperation2));

                List<TypeOperationResponseDTO> result = typeOperationService.getAllTypeOperations();

                assertNotNull(result, "La lista no debe estar vacía");

                assertEquals(2, result.size(), "La lista debe tener 2 registros");
                assertEquals(1L, result.get(0).id());
                assertEquals(2L, result.get(1).id());
                assertEquals(typeOperation1.getName(), result.get(0).name());
                verify(typeOperationRepository, times(1)).findAll();
            }
        }
        @Nested
        @DisplayName("Error scenarios")
        class ErrorScenarios {
            @Test
            @DisplayName("Should return an empty list when there are no type operations")
            void getALlTypeOperations_ShouldReturnAnEmptyListWhenThereAreNoTypeOperations() {
                when(typeOperationRepository.findAll()).thenReturn(List.of());
                List<TypeOperationResponseDTO> result = typeOperationService.getAllTypeOperations();
                assertNotNull(result, "El resultado está vacío pero no es nulo");
                assertTrue(result.isEmpty(), "El resultado está vacío");
                verify(typeOperationRepository, times(1)).findAll();
            }
        }
    }

    @Nested
    @DisplayName("createTypeOperation Tests")
    class CreateTypeOperationTests {
        @Nested
        @DisplayName("Successfully scenarios")
        class SuccessfullyScenarios {
            @Test
            @DisplayName("Should return the DTO with an Id")
            void createTypeOperation_ShouldReturnTheDTOWithAnId() {
                TypeOperationRequestDTO request = new TypeOperationRequestDTO("Deposito");
                TypeOperation mockTypeOperation = new TypeOperation(1L, request.name());

                when(typeOperationRepository.save(any(TypeOperation.class))).thenReturn(mockTypeOperation);

                TypeOperationResponseDTO response =  typeOperationService.createTypeOperation(request);

                assertNotNull(response, "El DTO de respuesta no debe ser nulo");
                assertEquals(1L, response.id(), "El Id debe ser igual");
                assertEquals(request.name(), response.name());
                verify(typeOperationRepository, times(1)).save(any(TypeOperation.class));
            }
        }

        @Nested
        @DisplayName("Error scenarios")
        class ErrorScenarios {
            @Test
            @DisplayName("Should throw DataIntegrityViolation when the TypeOperation is already registered")
            void createTypeOperation_ShouldThrowDataIntegrityViolationWhenTheTypeOperationIsAlreadyRegistrated() {
                TypeOperationRequestDTO typeOperationDuplicated = new TypeOperationRequestDTO("deposito");

                when(typeOperationRepository.save(any(TypeOperation.class)))
                        .thenThrow(new DataIntegrityViolationException("El tipo de operación ya está registrada"));

                DataIntegrityViolationException ex = assertThrows(DataIntegrityViolationException.class, () ->{
                    typeOperationService.createTypeOperation(typeOperationDuplicated);
                });

                assertEquals("El tipo de operación ya está registrada", ex.getMessage());

                verify(typeOperationRepository, times(1)).save(any(TypeOperation.class));
            }
        }
    }

    @Nested
    @DisplayName("UpdateTypeOperationTests")
    class UpdateTypeOperationTests {
        @Nested
        @DisplayName("Successfully scenarios")
        class SuccessfullyScenarios {
            @Test
            @DisplayName("Should return a DTO with the updated data")
            void updateTypeOperation_ShouldReturnTheDTOWithTheUpdatedData() {
                long typeOperationId = 1L;
                var originalTypeOperation = new TypeOperation(typeOperationId, "deposito");
                var request = new TypeOperationRequestDTO("deposito a cuenta de cheques");
                var updatedTypeOperation = new TypeOperation(typeOperationId, request.name());

                when(typeOperationRepository.findById(typeOperationId)).thenReturn(Optional.of(originalTypeOperation));
                when(typeOperationRepository.save(any(TypeOperation.class))).thenReturn(updatedTypeOperation);

                var response =  typeOperationService.updateTypeOperation(typeOperationId, request);

                assertNotNull(response, "El DTO no debe ser nulo");
                assertEquals(typeOperationId, response.id(), "El Id debe ser igual");
                assertEquals(request.name(), response.name());

                verify(typeOperationRepository, times(1)).findById(typeOperationId);
                verify(typeOperationRepository, times(1)).save(originalTypeOperation);

            }
        }

        @Nested
        @DisplayName("Error scenarios")
        class ErrorScenarios {
            @Test
            @DisplayName("Should throw a EntityNotFoundException when the Id does not exists")
            void updateTypeOperation_ShouldThrowEntityNotFoundExceptionWhenTheIdDoesNotExist() {
                long  typeOperationId = 1L;
                var resquest = new TypeOperationRequestDTO("deposito en cuenta de cheques");
                String expectedMessage = "Tipo de operación no encontrada. Id:" + typeOperationId;

                when(typeOperationRepository.findById(typeOperationId)).thenReturn(Optional.empty());

                EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () ->
                    typeOperationService.updateTypeOperation(typeOperationId, resquest),
                    "Se esperaba que lanzará la excepción EntityNotFoundException"
                );

                assertEquals(expectedMessage, ex.getMessage());

                verify(typeOperationRepository, times(1)).findById(typeOperationId);
                verify(typeOperationRepository, never()).save(any(TypeOperation.class));
            }

            @Test
            @DisplayName("Should throw DataIntegrityViolationWhenTheTypeOperationNameIsAlreadyInUse")
            void updateTypeOperation_ShouldThrowDataIntegrityViolationWhenTheTypeOperationNameIsAlreadyInUse() {
                long requestId = 2L;
                long storagedId = 1L;

                String duplicatedName = "deposito";
                String expectedMessage = "El tipo de opreación proporcionada ya está registrada y en uso";

                var request = new  TypeOperationRequestDTO(duplicatedName);

               TypeOperation recordToEdit = new TypeOperation(requestId, "deposito");
               TypeOperation duplicatedRecord = new TypeOperation(storagedId, duplicatedName);

                when(typeOperationRepository.findById(requestId)).thenReturn(Optional.of(recordToEdit));

                when(typeOperationRepository.findByName(duplicatedName))
                        .thenReturn(Optional.of(duplicatedRecord));

                DataIntegrityViolationException ex = assertThrows(DataIntegrityViolationException.class, () ->
                        typeOperationService.updateTypeOperation(requestId, request));

                assertEquals(expectedMessage, ex.getMessage());
                verify(typeOperationRepository).findByName(duplicatedName);
            }
        }
    }
}
