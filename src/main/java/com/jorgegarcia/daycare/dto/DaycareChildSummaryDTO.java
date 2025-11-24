package com.jorgegarcia.daycare.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DaycareChildSummaryDTO {
    private Long id;
    private String fullName;
}
