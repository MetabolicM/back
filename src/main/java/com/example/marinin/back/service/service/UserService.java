package com.example.marinin.back.service.service;

import com.example.marinin.back.models.User;

import java.util.List;

public interface UserService { //nonsense

    List<User> getAllUsers();

    User getUser(int id);

    void save(User user);

    void update(User updatedUser);

    void delete(int id);

}
