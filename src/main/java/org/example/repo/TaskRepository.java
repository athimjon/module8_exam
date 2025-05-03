package org.example.repo;

import org.example.entity.Attachment;
import org.example.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findAllByUserId(Integer userId);

    boolean existsByAttachment(Attachment userAttachment);
}