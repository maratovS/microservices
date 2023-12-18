package org.example.db.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "COMPANY_DS")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String ogrn;
    private String description;
    private Long userId;
    private boolean deleted = Boolean.FALSE;
}
