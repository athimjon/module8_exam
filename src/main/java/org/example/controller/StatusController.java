
package org.example.controller;

import org.example.entity.Status;
import org.example.repo.StatusRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/status")
@Controller
public class StatusController {
    private final StatusRepository statusRepository;

    public StatusController(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @PostMapping("/create")
    public String createStatus(@ModelAttribute Status status) {
        status.setName(status.getName().toUpperCase());
        statusRepository.save(status);
        return "redirect:/";
    }
}
