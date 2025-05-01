package org.example.component;

import org.example.entity.Attachment;
import org.example.repo.AttachmentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;

@Component
public class Runner implements CommandLineRunner {
    private final AttachmentRepository attachmentRepository;

    public Runner(AttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
//
//        byte[] fileContent = Files.readAllBytes(path);
//
//        String fileName = path.getFileName().toString();
//
//// Build the AttachmentController object using the file name and content
//        AttachmentController attachment = AttachmentController.builder()
//                .name(fileName)  // Use the file name, not the content
//                .content(fileContent)  // Use the byte content of the image
//                .build();
//        attachmentRepository.save(attachment);
    }
}
