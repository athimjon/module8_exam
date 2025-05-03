package org.example.controller;

import jakarta.servlet.annotation.MultipartConfig;
import jakarta.transaction.Transactional;
import org.example.dto.UserProfileDTO;
import org.example.dto.UserRoleForm;
import org.example.dto.UserRolesForm;
import org.example.entity.*;
import org.example.repo.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequestMapping("/user")
@Controller()
public class UserController {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;
    private final CommentRepository commentRepository;

    public UserController(UserRepository userRepository, RoleRepository roleRepository, AttachmentRepository attachmentRepository, TaskRepository taskRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.attachmentRepository = attachmentRepository;
        this.taskRepository = taskRepository;
        this.commentRepository = commentRepository;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping()
    public String usersPage(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("users", userRepository.findUsersThatDoesNotHaveId(user.getId()));
        model.addAttribute("roles", roleRepository.findAll());
        return "user-management";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
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

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Transactional
    @PostMapping("/delete/{userId}")
    public String deleteUser(@PathVariable Integer userId) {
        User user = userRepository.findById(userId).orElse(null);

        List<Task> userTasks = taskRepository.findAllByUserId(userId);
        taskRepository.deleteAll(userTasks);

        List<Comment> userComments = commentRepository.findAllByUserId(userId);
        commentRepository.deleteAll(userComments);

        Attachment userAttachment = user.getAttachment();
        if (userAttachment != null) {
            boolean isAttachmentUsedElsewhere = userRepository.existsByAttachment(userAttachment)
                                                || taskRepository.existsByAttachment(userAttachment);
            if (!isAttachmentUsedElsewhere) {
                attachmentRepository.delete(userAttachment);
            }
        }

        user.getRoles().clear();

        userRepository.save(user);

        userRepository.deleteById(userId);

        System.out.println("❌❌❌ " + userId);
//        userRepository.deleteById(userId);
        return "redirect:/user";
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MAINTAINER','ROLE_PROGRAMMER')")
    @GetMapping("/profile/settings")
    public String getUserProfileSettingsPage(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User dbUser = userRepository.findById(user.getId()).get();
        model.addAttribute("user", dbUser);
        return "user-profile";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MAINTAINER','ROLE_PROGRAMMER')")
    @Transactional
    @PostMapping("/profile/update")
    public String updateProfileImage(@ModelAttribute UserProfileDTO userProfileDTO,
                                     @RequestParam(required = false) MultipartFile file
    ) throws IOException {
        User user = userRepository.findById(userProfileDTO.getUserId()).get();
        if (file != null && !file.isEmpty()) {
            Attachment attachment = attachmentRepository.save(Attachment.builder()
                    .name(file.getOriginalFilename())
                    .content(file.getBytes()).build());
            user.setAttachment(attachment);
        }
        if (userProfileDTO.getFirstName() != null && userProfileDTO.getLastName() != null) {
            user.setFirstName(userProfileDTO.getFirstName());
            user.setLastName(userProfileDTO.getLastName());
        }
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                user,
                currentAuth.getCredentials(),
                currentAuth.getAuthorities()
        );

        // Set the new Authentication in the context
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        return "redirect:/user/profile/settings";
    }

}
