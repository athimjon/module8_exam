package org.example.entity;

import jakarta.persistence.Column;
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
public class Status extends BaseEntity {
    @Column(unique = true,nullable = false)
    private String name;
    private Boolean isActive=true;
    private Integer positionNumber;

}
