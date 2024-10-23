package com.example.ruleengine.services.impl;

import com.example.ruleengine.ast.Node;
import com.example.ruleengine.entity.Rule;
import com.example.ruleengine.repository.RuleRepository;
import com.example.ruleengine.services.RuleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Stack;

@Service
@RequiredArgsConstructor
public class RuleServiceImpl implements RuleService {

    private final RuleRepository ruleRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Node createRule(String ruleString) {
        Stack<Node> nodeStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        String[] tokens = ruleString.split(" ");
        for (String token : tokens) {
            if (isOperator(token)) {
                // Handle operator
                while (!operatorStack.isEmpty() && precedence(token) <= precedence(operatorStack.peek())) {
                    String op = operatorStack.pop();
                    Node right = nodeStack.pop();
                    Node left = nodeStack.pop();
                    Node operatorNode = new Node("operator", op);
                    operatorNode.setLeft(left);
                    operatorNode.setRight(right);
                    nodeStack.push(operatorNode);
                }
                operatorStack.push(token);
            } else {
                // Handle operand
                Node operandNode = new Node("operand", token);
                nodeStack.push(operandNode);
            }
        }

        while (!operatorStack.isEmpty()) {
            String op = operatorStack.pop();
            Node right = nodeStack.pop();
            Node left = nodeStack.pop();
            Node operatorNode = new Node("operator", op);
            operatorNode.setLeft(left);
            operatorNode.setRight(right);
            nodeStack.push(operatorNode);
        }

        return nodeStack.pop();
    }

    @Override
    public Node combineRules(List<Node> rules) {
        if (rules.isEmpty()) return null;

        Node combinedRoot = rules.get(0);

        for (int i = 1; i < rules.size(); i++) {
            Node currentRule = rules.get(i);
            // Combine with a logical operator (AND) - adjust as needed
            Node operatorNode = new Node("operator", "AND");
            operatorNode.setLeft(combinedRoot);
            operatorNode.setRight(currentRule);
            combinedRoot = operatorNode;
        }

        return combinedRoot; // Return the root of the combined AST
    }

    @Override
    public boolean evaluateRule(Node root, Map<String, Object> attributes) {
        if (root == null) return false;

        // Evaluate based on type of node
        if ("operand".equals(root.getType())) {
            String operand = (String) root.getValue();

            // Split operand and validate the parts
            String[] parts = operand.split(" ");
            if (parts.length < 3) {
                throw new IllegalArgumentException("Invalid operand format. Expected: 'attribute operator value'");
            }

            String attribute = parts[0]; // e.g., "age"
            String operator = parts[1];   // e.g., ">"
            String value = parts[2];       // e.g., "30"

            // Retrieve the actual value from attributes
            Object actualValue = attributes.get(attribute);

            // Perform the comparison based on the operator
            return compare(actualValue, operator, value);
        } else if ("operator".equals(root.getType())) {
            // Evaluate left and right child nodes
            boolean leftValue = evaluateRule(root.getLeft(), attributes);
            boolean rightValue = evaluateRule(root.getRight(), attributes);

            String operator = (String) root.getValue();
            if ("AND".equals(operator)) {
                return leftValue && rightValue;
            } else if ("OR".equals(operator)) {
                return leftValue || rightValue;
            }
        }

        return false;
    }

    @Override
    public String serializeNode(Node node) {
        if (node == null) {
            return null;
        }

        try {
            return objectMapper.writeValueAsString(node);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing Node to JSON", e);
        }
    }

    @Override
    public Node deserializeNode(String json) {
        try {
            return objectMapper.readValue(json, Node.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error deserializing JSON to Node", e);
        }
    }

    @Override
    public Rule saveRule(Rule rule) {
        return ruleRepository.save(rule);
    }

    @Override
    public Rule findRuleById(Long ruleId) {
        return ruleRepository.findById(ruleId).orElseThrow(() -> new RuntimeException("Rule not found with id " + ruleId));
    }

    @Override
    public List<Rule> getAllRule() {
        List<Rule> rules = ruleRepository.findAll();
        return rules;
    }

    public boolean isOperator(String token) {
        return token.equals("AND") || token.equals("OR") || token.equals(">") || token.equals("<") || token.equals("=");
    }

    public int precedence(String operator) {
        if (operator.equals("AND")) return 1;
        if (operator.equals("OR")) return 0;
        return -1; // non-operator
    }

    public boolean compare(Object actualValue, String operator, String expectedValue) {
        return switch (operator) {
            case ">" -> ((Comparable) actualValue).compareTo(Integer.parseInt(expectedValue)) > 0;
            case "<" -> ((Comparable) actualValue).compareTo(Integer.parseInt(expectedValue)) < 0;
            case "=" -> actualValue.equals(expectedValue);
            default -> throw new IllegalArgumentException("Unknown operator: " + operator);
        };
    }
}
