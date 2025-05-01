package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class StatusDTO {

    private Integer id;
    private String name;
    private Boolean isActive;
    private Integer positionNumber;

}
