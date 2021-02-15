package com.example.marinin.back.dao.dao;

import com.example.marinin.back.models.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    List<User> getAllUsers();

    User getUser(int id);

    void save(User user);

    void update(User incomingUser);

    void delete(int id);

    Optional<User> loadUserByUsername(String userName);
}
