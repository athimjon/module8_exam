package org.example.controller;

import org.example.entity.Status;
import org.example.entity.Task;
import org.example.entity.User;
import org.example.repo.RoleRepository;
import org.example.repo.StatusRepository;
import org.example.repo.TaskRepository;
import org.example.repo.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MAINTAINER','ROLE_PROGRAMMER')")
    @GetMapping("/")
    public String getTaskHomePage(Model model) {
        List<Task> tasks = taskRepository.findAll();
        List<Status> statuses = statusRepository.findByIsActiveTrueOrderByPositionNumberAsc();
        Integer min = statuses.stream()
                .map(Status::getPositionNumber)
                .min(Integer::compareTo)
                .orElse(null);

        Integer max = statuses.stream()
                .map(Status::getPositionNumber)
                .max(Integer::compareTo)
                .orElse(null);
        model.addAttribute("tasks", tasks);
        model.addAttribute("max", max);
        model.addAttribute("min", min);
        model.addAttribute("statuses", statuses);
        return "task-home-page";
    }

    @GetMapping("/login")
    public String getLogInPage() {
        return "auth/login";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MAINTAINER')")
    @GetMapping("/status")
    public String getCreateStatusPage() {
        return "create-status";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MAINTAINER')")
    @GetMapping("/task")
    public String getCreateTaskPage(Model model) {
        List<User> users = userRepository.findAll();
        List<Status> statuses = statusRepository.findAll();
        model.addAttribute("statuses",statuses);
        model.addAttribute("users",users);
        return "create-task";
    }



    @GetMapping("/register")
    public String getRegisterPage(){
        return "auth/register";
    }

}
