package com.jorgegarcia.daycare.dto;
import java.util.List;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherDTO {
   private Long id;
   private String firstName;
   private String lastName;
   private String email;
   private List<ClassroomSummaryDTO> classrooms;
  
}
