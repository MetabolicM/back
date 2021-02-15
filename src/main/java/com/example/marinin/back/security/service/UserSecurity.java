package com.example.marinin.back.security.service;

import com.example.marinin.back.dao.dao.UserDAO;
import com.example.marinin.back.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserSecurity implements UserDetailsService {

    private UserDAO userDAO;

    public UserSecurity() {
    }

    @Autowired
    public UserSecurity(UserDAO userDAO) {
        this.userDAO = userDAO;
    }


    @Override
    @Transactional
    public User loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> userOptional = userDAO.loadUserByUsername(userName);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        throw new UsernameNotFoundException("User not found by name: " + userName);
    }
}
