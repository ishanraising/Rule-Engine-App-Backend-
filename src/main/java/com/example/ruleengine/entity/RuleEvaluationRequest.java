package com.example.ruleengine.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RuleEvaluationRequest {
    private Long ruleId;
    private int age;
    private String department;
    private double salary;
    private int experience;
}
