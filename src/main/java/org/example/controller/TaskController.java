package org.example.controller;

import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.example.entity.*;
import org.example.repo.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    public TaskController(TaskRepository taskRepository, AttachmentRepository attachmentRepository, StatusRepository statusRepository, UserRepository userRepository, CommentRepository commentRepository) {
        this.taskRepository = taskRepository;
        this.attachmentRepository = attachmentRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MAINTAINER')")
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

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MAINTAINER','ROLE_PROGRAMMER')")
    @PostMapping("/update/right")
    public String changeTaskStatusToRight(@RequestParam   Integer taskId){
        Task task = taskRepository.findById(taskId).get();
        Integer posNumber = task.getStatus().getPositionNumber();
        List<Status> statuses = statusRepository.getStatusRight(posNumber);
        task.setStatus(statuses.get(0));
        taskRepository.save(task);
        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MAINTAINER','ROLE_PROGRAMMER')")
    @PostMapping("/update/left")
    public String changeTaskStatusToLeft(@RequestParam   Integer taskId){
        Task task = taskRepository.findById(taskId).get();
        Integer posNumber = task.getStatus().getPositionNumber();
        List<Status> statuses = statusRepository.getStatusLeft(posNumber);
        task.setStatus(statuses.get(0));
        taskRepository.save(task);
        return "redirect:/";
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MAINTAINER','ROLE_PROGRAMMER')")
    @GetMapping("/update/{taskId}")
    public String getUpdateTaskPage(@PathVariable Integer taskId, Model model) {
        List<User> allUsers = userRepository.findAll();
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("task", taskRepository.findById(taskId).get());
        return "task-update";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MAINTAINER')")
    @Transactional
    @PostMapping("/update")
    public String updateTaskPage(@RequestParam MultipartFile file,
                                 @RequestParam Integer taskId,
                                 @RequestParam Integer userId,
                                 @RequestParam String title) throws IOException {
        Task task = taskRepository.findById(taskId).get();
        User user = userRepository.findById(userId).get();
        if (!file.isEmpty()) {
            Attachment attachment = attachmentRepository.save(Attachment.builder()
                    .name(file.getOriginalFilename())
                    .content(file.getBytes())
                    .build());
            attachmentRepository.save(attachment);
            task.setAttachment(attachment);
        }
        task.setUser(user);
        task.setTitle(title);
        taskRepository.save(task);
        return "redirect:/";
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MAINTAINER','ROLE_PROGRAMMER')")
    @Transactional
    @PostMapping("/add/comment")
    public String addTaskComment(@RequestParam Integer taskId,
                                 @RequestParam String comment) throws IOException {
        User commentator = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Task task = taskRepository.findById(taskId).get();
        Comment saved = Comment.builder()
                .user(commentator)
                .comment(comment)
                .build();
        commentRepository.save(saved);
        task.getComments().add(saved);
        return "redirect:/task/update/" + task.getId();
    }

}
