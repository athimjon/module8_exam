package org.example.controller;

import org.example.entity.Status;
import org.example.entity.Task;
import org.example.repo.StatusRepository;
import org.example.repo.TaskRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PageController {
    private final TaskRepository taskRepository;
    private final StatusRepository statusRepository;

    public PageController(TaskRepository taskRepository, StatusRepository statusRepository) {
        this.taskRepository = taskRepository;
        this.statusRepository = statusRepository;
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


}
