package com.siqueiros.bank.service;

import com.siqueiros.bank.dto.UserRequestDTO;
import com.siqueiros.bank.dto.UserResponseDTO;
import java.util.List;

public interface UserService {
    List<UserResponseDTO> getAllUsers();
    UserResponseDTO getUserById(Long id);
    UserResponseDTO createUser(UserRequestDTO request);
    UserResponseDTO deleteUser(Long id);
    UserResponseDTO getUserByEmail(String email);
    UserResponseDTO updateUser(Long id, UserRequestDTO request);
}
