package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class UserProfileDTO {
    private String firstName;
    private String lastName;
    private Integer userId;

}
