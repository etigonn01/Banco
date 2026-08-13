package com.siqueiros.bank.dto;


import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Unit tests for UserRequestDTO validation")
public class UserRequestDTOTest {
    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("should pass all validations when the fields meet the constraints.")
    void ShouldPassAllValidationsWhenTheFieldsMeetTheConstraints() {
        var dto = new UserRequestDTO(
                "Manuel Carbajal",
                "carbajal@apple.com",
                "carbajal1234",
                "6783982726"
        );

        Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Nested
    @DisplayName("Validations for the fullName field")
    class ValidationsForTheFullNameField {
        @Test
        @DisplayName("Should fail when the full name has fewer than 8 characters.")
        void ShouldFailWhenTheFullNameHasFewerThan8Characters() {
            var dto = new UserRequestDTO(
                    "ma",
                    "mariana@outlook.com",
                    "mariana1234",
                    "6783982726"
            );
            Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
            assertEquals(1, violations.size());
            var violation = violations.iterator().next();
            assertEquals("El nombre debe tener entre 8 y 120 caracteres", violation.getMessage());
            assertEquals("fullName", violation.getPropertyPath().toString());
        }

        @Test
        @DisplayName("Should fail when the full name has more than 120 characters")
        void ShouldFailWhenTheFullNameHasMoreThan120Characters() {
            String veryLongName = "a".repeat(121);
            var dto = new UserRequestDTO(
                    veryLongName,
                    "mariana@outlook.com",
                    "mariana1234",
                    "6783982726"
            );

            Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
            assertEquals(1, violations.size());
            assertEquals("fullName", violations.iterator().next().getPropertyPath().toString());
            assertEquals("El nombre debe tener entre 8 y 120 caracteres", violations.iterator().next().getMessage());
        }
    }

    @Nested
    @DisplayName("Validations for the email field")
    class ValidationsForTheEmailField {
        @Test
        @DisplayName("Should fail when the email has fewer than 8 characters ")
        void ShouldFailWhenTheFullNameHasFewerThan8Characters() {
            var dto = new UserRequestDTO(
                    "Mariana Contreras Avila",
                    "m@a.com",
                    "mariana1234",
                    "6783982726"
            );
            Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
            assertEquals(1, violations.size());
            var violation = violations.iterator().next();
            assertEquals("El correo electrónico debe tener entre 8 y 50 caracteres", violation.getMessage());
            assertEquals("email", violation.getPropertyPath().toString());
        }

        @Test
        @DisplayName("Should fail when the email has more than 50 characters ")
        void ShouldFailWhenTheFullNameHasMoreThan50Characters() {
            String veryLongEmail = "m".repeat(50);
            var dto = new UserRequestDTO(
                    "Mariana Contreras Avila",
                    veryLongEmail + "@gmail.com",
                    "mariana1234",
                    "6783982726"
            );
            Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
            assertEquals(1, violations.size());
            var violation = violations.iterator().next();
            assertEquals("El correo electrónico debe tener entre 8 y 50 caracteres", violation.getMessage());
            assertEquals("email", violation.getPropertyPath().toString());
        }

        @Test
        @DisplayName("Should fail when the email format is invalid")
        void  ShouldFailWhenTheEmailFormatIsInvalid() {
            var dto = new UserRequestDTO(
                    "Manuel Carbajal",
                    "carbajalapple.com",
                    "carbajal1234",
                    "6783982726"
            );
            Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
            assertEquals(1, violations.size());
            var violation = violations.iterator().next();
            assertEquals("El formato del correo electrónico no es válido", violation.getMessage());
            assertEquals("email", violation.getPropertyPath().toString());
        }
    }

    @Nested
    @DisplayName("Validations for the password hash field")
    class ValidationsForThePasswordField {
        @Test
        @DisplayName("Should fail when the password hash is empty or null")
        void ShouldFailWhenThePasswordIsEmpty() {
            var dto = new UserRequestDTO(
                    "Manuel Carbajal",
                    "carbajal@apple.com",
                    "        ",
                    "6783982726"
            );
            Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
            assertEquals(1, violations.size());
            assertEquals("El hash de la contraseña no puede estar vació", violations.iterator().next().getMessage());
        }
    }

    @Nested
    @DisplayName("Validations for the phone number field")
    class ValidationsForThePhoneNumberField {
        @Test
        @DisplayName("Should fail then the phone number has fewer than 10 digits")
        void ShouldFailWhenThePhoneNumberHasFewerThan10Digits() {
            var dto = new UserRequestDTO(
                    "Manuel Carbajal",
                    "carbajal@apple.com",
                    "carbajal1234",
                    "678398272"
            );
            Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
            assertEquals(1, violations.size());
            assertEquals("El número de teléfono debe ser un formato de 10 dígitos", violations.iterator().next().getMessage());
        }
        @Test
        @DisplayName("Should fail then the phone number has more than 10 digits")
        void ShouldFailWhenThePhoneNumberHasMoreThan10Digits() {
            var dto = new UserRequestDTO(
                    "Manuel Carbajal",
                    "carbajal@apple.com",
                    "carbajal1234",
                    "67839827223"
            );
            Set<ConstraintViolation<UserRequestDTO>> violations = validator.validate(dto);
            assertEquals(1, violations.size());
            assertEquals("El número de teléfono debe ser un formato de 10 dígitos", violations.iterator().next().getMessage());
        }
    }
}
