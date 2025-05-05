package org.example.component;

import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import org.example.entity.Attachment;
import org.example.entity.Role;
import org.example.repo.AttachmentRepository;
import org.example.repo.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Component
public class Runner implements CommandLineRunner {
    private final AttachmentRepository attachmentRepository;
    private final RoleRepository roleRepository;

    public Runner(AttachmentRepository attachmentRepository, RoleRepository roleRepository) {
        this.attachmentRepository = attachmentRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        List<Role> roles = roleRepository.findAll();
        if (roles.isEmpty()) roleRepository.insertRolesToDB();
    }
}
