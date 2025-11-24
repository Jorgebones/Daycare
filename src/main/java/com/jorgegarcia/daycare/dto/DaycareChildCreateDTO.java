package com.jorgegarcia.daycare.dto;

import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DaycareChildCreateDTO {
  private String firstName;
  private String lastName;
  private Integer age;
  private Long classroomId;
}
