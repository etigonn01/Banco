package com.siqueiros.bank.controller;

import com.siqueiros.bank.dto.UserRequestDTO;
import com.siqueiros.bank.dto.UserResponseDTO;
import com.siqueiros.bank.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/search/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDTO getUserByEmail(@RequestParam String email) {
        return userService.getUserByEmail(email);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO createUser(@Valid @RequestBody UserRequestDTO request) {
        return userService.createUser(request);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDTO updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO request) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDTO deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

}
