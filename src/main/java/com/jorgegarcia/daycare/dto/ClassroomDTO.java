package com.jorgegarcia.daycare.dto;
import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassroomDTO {
  private Long id;
  private String className;
  private TeacherSummaryDTO teacher;
  private List<DaycareChildSummaryDTO> children;
  
}
