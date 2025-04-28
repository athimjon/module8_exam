package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.example.entity.abs.BaseEntity;
import org.example.entity.enums.Roles;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@SuperBuilder
@Table(name = "roles")
public class Role extends BaseEntity implements GrantedAuthority {

    @Enumerated(value = EnumType.STRING)
    private Roles roleName;

    @Override
    public String getAuthority() {
        return this.roleName.getUserRoleName();
    }
}
