package com.example.marinin.back.dao.impl;

import com.example.marinin.back.dao.dao.RoleDAO;
import com.example.marinin.back.models.Role;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.Set;

@Repository
public class RoleDAOImp implements RoleDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Set<Role> getSetOfAllRoles() {
        TypedQuery<Role> tQuery = (TypedQuery<Role>) entityManager.createQuery("SELECT r FROM Role r order by r.id"); // May be
        Set<Role> internalRoleSet = new HashSet<>(tQuery.getResultList());
        return internalRoleSet;
    }

    @Override
    public Role getRoleByID(int id) {
        return entityManager.find(Role.class, id);
    }

    @Override
    public Set<Role> getSetOfRolesByName(String[] strArr) {
        Set<Role> internalSet = new HashSet<>();
        for (String str : strArr) {
            internalSet.add(getRoleByName(str));
        }
        return internalSet;
    }

    @Override
    public Set<Role> getSetOfRolesByName(String string) {
        Set<Role> internalSet = new HashSet<>();
        internalSet.add(getRoleByName(string));
        return internalSet;
    }

    @Override
    public Role getRoleByName(String string){
        return (Role) entityManager.createQuery("SELECT r FROM Role r WHERE r.role=:roleName").
                setParameter("roleName", string).getSingleResult();
    }
}
