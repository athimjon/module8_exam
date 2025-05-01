package org.example.controller;

import org.example.entity.Role;
import org.example.entity.Status;
import org.example.entity.Task;
import org.example.entity.User;
import org.example.entity.enums.Roles;
import org.example.repo.RoleRepository;
import org.example.repo.StatusRepository;
import org.example.repo.TaskRepository;
import org.example.repo.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class PageController {
    private final TaskRepository taskRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public PageController(TaskRepository taskRepository, StatusRepository statusRepository, UserRepository userRepository, RoleRepository roleRepository) {
        this.taskRepository = taskRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/")
    public String getTaskHomePage(Model model) {
        List<Task> tasks = taskRepository.findAll();
        List<Status> statuses = statusRepository.findByIsActiveTrue();
        long limit = statusRepository.count();
        model.addAttribute("tasks", tasks);
        model.addAttribute("limit", limit);
        model.addAttribute("statuses", statuses);
        return "task-home-page";
    }

    @GetMapping("/login")
    public String getLogInPage() {
        return "auth/login";
    }


    @GetMapping("/status")
    public String getCreateStatusPage() {
        return "create-status";
    }
    @GetMapping("/task")
    public String getCreateTaskPage(Model model) {
        List<User> users = userRepository.findAll();
        List<Status> statuses = statusRepository.findAll();
        model.addAttribute("statuses",statuses);
        model.addAttribute("users",users);
        return "create-task";
    }
    @GetMapping("/change-role")
    public String userPage(Model model) {
        List<User> allUsers = userRepository.findAll();
        List<Role> allRoles = roleRepository.findAll();
        model.addAttribute("users",allUsers);
        model.addAttribute("roles",allRoles);
        return "update-user-role";
    }
    @PostMapping("/users/changeRole")
    public String changeUserRole(@RequestParam  Integer userId,
                                 @RequestParam("roleNames") List<String> roleNames) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            List<Roles> enumRoles = roleNames.stream()
                    .map(Roles::valueOf)
                    .toList();

            List<Role> newRoles = roleRepository.findAllByRoleNameIn(enumRoles);

            user.setRoles(newRoles);
            userRepository.save(user);
        }
        return "redirect:/";
    }

    @GetMapping("/register")
    public String getRegisterPage(){
        return "auth/register";
    }

}
