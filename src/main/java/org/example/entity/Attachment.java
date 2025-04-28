package org.example.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.entity.abs.BaseEntity;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@SuperBuilder
public class Attachment extends BaseEntity {
    private String name;
    private byte[] content;
}
