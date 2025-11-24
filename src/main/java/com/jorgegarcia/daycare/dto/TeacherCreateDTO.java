package com.jorgegarcia.daycare.dto;
import lombok.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Data // auto-generates getters, setters, equals, toString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeacherCreateDTO {
  @NotBlank(message = "First name cannot be blank")
  private String firstName;

  @NotBlank(message = "Last name cannot be blank")
  private String lastName;
  
  @Email(message = "Invalid email format")
  @NotBlank(message = "Email cannot be blank")
  private String email;
  
}
