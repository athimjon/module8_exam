package org.example.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.example.repo.AttachmentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Optional;


@RequestMapping("/attachment")
@Controller
public class Attachment {

    private final AttachmentRepository attachmentRepository;

    public Attachment(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @GetMapping("/view/{attachmentId}")
    public void getImageContent(HttpServletResponse response, @PathVariable Integer attachmentId) {
        Optional<org.example.entity.Attachment> attachment = attachmentRepository.findById(attachmentId);
        try {
            response.getOutputStream().write(attachment.get().getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
