package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.entity.abs.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@SuperBuilder
public class Task extends BaseEntity {

    private String title;
    private String description;
    @ManyToOne
    private Status status;
    @ManyToOne
    private Attachment attachment;
    @ManyToOne
    private User user;
    @OneToMany
    private List<Comment> comments=new ArrayList<>();

}
