package org.example.db.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CompanyDto {
    private Long id;
    private String name;
    private String ogrn;
    private String description;
    private Long userId;
}
