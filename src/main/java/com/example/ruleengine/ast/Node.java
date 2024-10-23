package com.example.ruleengine.ast;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


@Getter
@Setter
public class Node {

    private String type; // "operator" or "operand"
    private Node left; // left child
    private Node right; // right child for operators
    private Object value;

    public Node() {
    }

    @JsonCreator
    public Node(@JsonProperty("type") String type, @JsonProperty("value") Object value) {
        this.type = type;
        this.value = value;
    }
}
