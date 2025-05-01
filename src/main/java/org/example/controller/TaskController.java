package org.example.controller;

import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.example.entity.*;
import org.example.repo.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequestMapping("/task")
@Controller
public class TaskController {
    private final TaskRepository taskRepository;
    private final AttachmentRepository attachmentRepository;
    private final StatusRepository statusRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    public TaskController(TaskRepository taskRepository, AttachmentRepository attachmentRepository, StatusRepository statusRepository, UserRepository userRepository,
                          CommentRepository commentRepository) {
        this.taskRepository = taskRepository;
        this.attachmentRepository = attachmentRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @PostMapping("/create")
    public String createStatus(@ModelAttribute Task task,
                               @RequestParam Integer statusId,
                               @RequestParam Integer userId,
                               @RequestParam MultipartFile file) throws IOException {
        Status status = statusRepository.findById(statusId).get();
        User user = userRepository.findById(userId).get();
        Attachment attachment = Attachment.builder()
                .name(file.getOriginalFilename())
                .content(file.getBytes())
                .build();
        attachmentRepository.save(attachment);

        task.setStatus(status);
        task.setUser(user);
        task.setAttachment(attachment);

        taskRepository.save(task);
        return "redirect:/";
    }

    @PostMapping("/update/right")
    public String changeTaskStatusToRight(@RequestParam Integer taskId) {
        Task task = taskRepository.findById(taskId).get();
        Integer posNumber = task.getStatus().getPositionNumber();
        List<Status> statuses = statusRepository.getStatusRight(posNumber);
        task.setStatus(statuses.get(0));
        taskRepository.save(task);
        return "redirect:/";
    }

    @PostMapping("/update/left")
    public String changeTaskStatusToLeft(@RequestParam Integer taskId) {
        Task task = taskRepository.findById(taskId).get();
        Integer posNumber = task.getStatus().getPositionNumber();
        List<Status> statuses = statusRepository.getStatusLeft(posNumber);
        task.setStatus(statuses.get(0));
        taskRepository.save(task);
        return "redirect:/";
    }


}
