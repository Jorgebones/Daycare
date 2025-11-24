package com.jorgegarcia.daycare.dto;

import lombok.*;
import jakarta.validation.constraints.Email;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherUpdateDTO {
  private String firstName;
  private String lastName;
  
  @Email(message = "Invalid email format")
  private String email;
}
