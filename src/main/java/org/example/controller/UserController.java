package org.example.controller;

import jakarta.transaction.Transactional;
import org.example.dto.UserRoleForm;
import org.example.dto.UserRolesForm;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.repo.RoleRepository;
import org.example.repo.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequestMapping("/users")
@Controller()
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping()
    public String usersPage(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("roles", roleRepository.findAll());
        return "user-management";
    }

    @Transactional
    @PostMapping("/update/roles")
    public String updateUserRoles(@ModelAttribute UserRolesForm userRolesForm) {
        for (UserRoleForm userRole : userRolesForm.getUserRoles()) {
            List<Role> roles = new ArrayList<>();
            User user = userRepository.findById(userRole.getUserId()).get();
            for (Integer roleId : userRole.getRoleIds()) {
                Role role = roleRepository.findById(roleId).get();
                roles.add(role);
            }
            user.setRoles(roles);
            userRepository.save(user);
        }
        return "redirect:/";
    }


    @PostMapping("/delete/{userId}")
    public String deleteUser(@PathVariable Integer userId) {
        System.out.println("❌❌❌ " + userId);
//        userRepository.deleteById(userId);
        return "redirect:/users";
    }


}
