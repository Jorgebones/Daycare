package com.jorgegarcia.daycare.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ClassroomSummaryDTO {
  private Long id;
  private String className;
  
}
