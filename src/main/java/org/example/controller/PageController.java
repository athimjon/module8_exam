package org.example.controller;

import org.example.entity.Status;
import org.example.entity.Task;
import org.example.entity.User;
import org.example.repo.StatusRepository;
import org.example.repo.TaskRepository;
import org.example.repo.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PageController {
    private final TaskRepository taskRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;

    public PageController(TaskRepository taskRepository, StatusRepository statusRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String getTaskHomePage(Model model) {
        List<Task> tasks = taskRepository.findAll();
        List<Status> statuses = statusRepository.findAll();
        model.addAttribute("tasks", tasks);
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

    @GetMapping("/register")
    public String getRegisterPage(){
        return "auth/register";
    }

}
