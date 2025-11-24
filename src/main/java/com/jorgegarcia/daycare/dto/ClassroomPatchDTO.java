package com.jorgegarcia.daycare.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassroomPatchDTO {
    private String className;
    private Long teacherId; // optional
}
