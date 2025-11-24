package com.jorgegarcia.daycare.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DaycareChildDTO {
    private Long id;                // Útil para GETs
    private String firstName;
    private String lastName;
    private Integer age;
    private ClassroomSummaryDTO classroom;
  
}
