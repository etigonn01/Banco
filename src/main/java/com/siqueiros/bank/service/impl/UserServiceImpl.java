package com.siqueiros.bank.service.impl;

import com.siqueiros.bank.dto.UserRequestDTO;
import com.siqueiros.bank.dto.UserResponseDTO;
import com.siqueiros.bank.exception.UserNotFoundException;
import com.siqueiros.bank.model.User;
import com.siqueiros.bank.repositories.UserRepository;
import com.siqueiros.bank.service.UserService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDTO getUserById(Long id) {
       return userRepository.findById(id)
               .map(this::mapToResponseDTO)
               .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado. Id: " + id));
    }

    @Override
    public UserResponseDTO createUser(UserRequestDTO request) {
        User user = new User(request.fullName(), request.email(), request.passwordHash(), request.phoneNumber());
        User createdUser = userRepository.save(user);
        return mapToResponseDTO(createdUser);
    }

    @Override
    public UserResponseDTO deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado. Id: " + id));
        userRepository.delete(user);
        return mapToResponseDTO(user);
    }

    @Override
    public UserResponseDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapToResponseDTO)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado. Id: " + email));
    }

    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado. Id: " + id));
        user.setFullName(request.fullName());
        user.setEmail(request.email());
        user.setPasswordHash(request.passwordHash());
        user.setPhoneNumber(request.phoneNumber());

        User updatedUser = userRepository.save(user);
        return mapToResponseDTO(updatedUser);
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getPhoneNumber(),
                user.getCreatedAt());
    }
}
