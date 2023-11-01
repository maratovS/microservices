package org.example.db.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String login;
    @JsonIgnore
    private String password;
    private boolean enabled;
    private Long companyId;
}
