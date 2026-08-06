package com.siqueiros.bank.service;

import com.siqueiros.bank.model.User;
import java.util.Optional;
import java.util.List;

public interface UserService {
    List<User> getAll();
    Optional<User> getById(Long id);
    User save(User user);
    Optional<User> delete(Long id);
    Optional<User> getByEmail(String email);
}
