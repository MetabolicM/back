package com.example.marinin.back.dao.impl;


import com.example.marinin.back.dao.dao.UserDAO;
import com.example.marinin.back.models.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;


@Repository
public class UserDAOImp implements UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> getAllUsers() {
        TypedQuery<User> tQuery = (TypedQuery<User>) entityManager.createQuery("SELECT DISTINCT u FROM User u JOIN u.roles order by u.id");
        return tQuery.getResultList();
    }

    @Override
    public User getUser(int id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public void save(User user) {
        entityManager.persist(user);
    }

    @Override
    public void update(User incomingUser) {
        entityManager.merge(incomingUser);
    }

    @Override
    public void delete(int id) {
        User removableUser = getUser(id);
        entityManager.remove(removableUser);
    }

    @Override
    public Optional<User> loadUserByUsername(String userName) {
        try {
            User internalUser = (User) entityManager
                    .createQuery("SELECT DISTINCT u FROM User u JOIN FETCH u.roles WHERE u.name=:userName ")
                    .setParameter("userName", userName).getSingleResult();

            return Optional.of(internalUser);
        }catch (NoResultException noResultException){
            return Optional.empty();
        }
    }
}
