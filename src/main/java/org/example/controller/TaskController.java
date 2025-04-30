package org.example.controller;

import lombok.SneakyThrows;
import org.example.entity.Attachment;
import org.example.entity.Status;
import org.example.entity.Task;
import org.example.entity.User;
import org.example.repo.AttachmentRepository;
import org.example.repo.StatusRepository;
import org.example.repo.TaskRepository;
import org.example.repo.UserRepository;
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

    public TaskController(TaskRepository taskRepository, AttachmentRepository attachmentRepository, StatusRepository statusRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.attachmentRepository = attachmentRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
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
    public String changeTaskStatusToRight(@RequestParam   Integer taskId){
        Task task = taskRepository.findById(taskId).get();
        Integer posNumber = task.getStatus().getPositionNumber();
        List<Status> statuses = statusRepository.getStatusRight(posNumber);
        task.setStatus(statuses.get(0));
        taskRepository.save(task);
        return "redirect:/";
    }
    @PostMapping("/update/left")
    public String changeTaskStatusToLeft(@RequestParam   Integer taskId){
        Task task = taskRepository.findById(taskId).get();
        Integer posNumber = task.getStatus().getPositionNumber();
        List<Status> statuses = statusRepository.getStatusLeft(posNumber);
        task.setStatus(statuses.get(0));
        taskRepository.save(task);
        return "redirect:/";
    }

    @GetMapping("/update")
     public String updateTaskPage(@RequestParam Integer taskId, Model model) {
        model.addAttribute("task", taskRepository.findById(taskId).get());
        return "task-update";
    }

}
