package com.example.marinin.back.service.service;


import com.example.marinin.back.models.Role;

import java.util.Set;

public interface RoleService {

    Set<Role> getSetOfAllRoles();

    Set<Role> getSetOfRolesByName(String[] strArr);

    Set<Role> getSetOfRolesByName(String string);

    Role getRoleByID(int id);
}
