package org.example.repo;

import org.example.entity.Role;
import org.example.entity.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findAllByRoleNameIn(List<Roles> enumRoles);
}