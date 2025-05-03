package org.example.repo;

import org.example.entity.Attachment;
import org.example.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query(value = "select  * from  users  where id!=:userId", nativeQuery = true)
    List<User> findUsersThatDoesNotHaveId(Integer userId);

    boolean existsByAttachment(Attachment userAttachment);
}