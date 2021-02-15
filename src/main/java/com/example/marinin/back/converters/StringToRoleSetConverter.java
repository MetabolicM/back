package com.example.marinin.back.converters;

import com.example.marinin.back.models.Role;
import com.example.marinin.back.service.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Set;


@Component
public class StringToRoleSetConverter implements Converter<String, Set<Role>> {

    @Autowired
    private RoleService roleService;

    public StringToRoleSetConverter() {
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public Set<Role> convert(String inputString) {
        return roleService.getSetOfRolesByName(inputString);
    }
}
