package com.jorgegarcia.daycare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassroomCreateDTO {
  @NotBlank
  private String className;
  @NotNull
  private Long teacherId;
}
