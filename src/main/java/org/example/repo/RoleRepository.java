package org.example.repo;

import org.example.entity.Role;
import org.example.entity.enums.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findAllByRoleNameIn(List<Roles> enumRoles);
    @Modifying
    @Query(value = "INSERT INTO roles(role_name) VALUES('ROLE_PROGRAMMER'),('ROLE_MAINTAINER'),('ROLE_ADMIN')" ,nativeQuery = true)
    void insertRolesToDB();
}