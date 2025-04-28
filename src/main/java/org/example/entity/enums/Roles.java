package org.example.entity.enums;

import lombok.Getter;

@Getter
public enum Roles {
    ROLE_PROGRAMMER("PROGRAMMER"),
    ROLE_MAINTAINER("MAINTAINER"),
    ROLE_ADMIN("ADMIN"),
    ROLE_USER("USER"),;

    private String userRoleName;

    Roles(String userRoleName) {
        this.userRoleName = userRoleName;
    }
}
