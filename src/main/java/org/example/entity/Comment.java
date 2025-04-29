package org.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.entity.abs.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@SuperBuilder
public class Comment extends BaseEntity {
    @Column(nullable = false)
    private String comment;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @ManyToOne
    private User user;

}
