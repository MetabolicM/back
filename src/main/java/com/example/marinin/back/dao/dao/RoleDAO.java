package com.example.marinin.back.dao.dao;

import com.example.marinin.back.models.Role;

import java.util.Set;

public interface RoleDAO {
     Set<Role> getSetOfAllRoles();
     Role getRoleByID(int id);
     Set<Role> getSetOfRolesByName(String[] strArr);
     Set<Role> getSetOfRolesByName(String string);
     Role getRoleByName(String string);
}
