package com.jorgegarcia.daycare.dto;

import jakarta.validation.constraints.Email;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherPatchDTO {
  private String firstName;
  private String lastName;
  
  @Email(message = "Invalid email format")
  private String email;
  
}
