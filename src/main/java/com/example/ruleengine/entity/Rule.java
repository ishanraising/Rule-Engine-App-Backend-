package com.example.ruleengine.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Rule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private String ruleString; // original rule string

    @Lob
    @Column(name = "ast_json", columnDefinition = "LONGTEXT")
    private String astJson; // to store serialized AST as JSON

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
