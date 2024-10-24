package com.example.ruleengine.ast;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Stack;

@Component
public class RuleParser {

    public Node parseRule(String rule) {
        // Preserve spaces for easier parsing
        rule = rule.replaceAll("\\s+", " "); // Replace multiple spaces with a single space

        Stack<Node> nodeStack = new Stack<>();
        Stack<String> operatorStack = new Stack<>();

        // Split the rule into tokens (operands and operators)
        String[] tokens = rule.split("(?<=\\sAND\\s)|(?=\\sAND\\s)|(?<=\\sOR\\s)|(?=\\sOR\\s)|(?<=\\()|(?=\\()|(?<=\\))|(?=\\))");
        for(int i=0 ; i<tokens.length;i++){
            tokens[i]=tokens[i].trim();
        }
        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            System.out.println("Token: " + token); // Log each token for debugging

            if (token.equals("AND") || token.equals("OR")) {
                while (!operatorStack.isEmpty() && precedence(token) <= precedence(operatorStack.peek())) {
                    Node rightNode = nodeStack.pop();
                    Node leftNode = nodeStack.pop();
                    Node operatorNode = new Node();
                    operatorNode.setType("operator");
                    operatorNode.setValue(operatorStack.pop());
                    operatorNode.setLeft(leftNode);
                    operatorNode.setRight(rightNode);
                    nodeStack.push(operatorNode);
                }
                operatorStack.push(token);
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.peek().equals("(")) {
                    Node rightNode = nodeStack.pop();
                    Node leftNode = nodeStack.pop();
                    Node operatorNode = new Node();
                    operatorNode.setType("operator");
                    operatorNode.setValue(operatorStack.pop());
                    operatorNode.setLeft(leftNode);
                    operatorNode.setRight(rightNode);
                    nodeStack.push(operatorNode);
                }
                operatorStack.pop(); // Remove the '('
            } else {
                // Assume token is an operand (e.g., "age > 30")
                Node operandNode = new Node();
                operandNode.setType("operand");
                operandNode.setValue(token); // The entire operand
                nodeStack.push(operandNode);
            }
        }

        while (!operatorStack.isEmpty()) {
            Node rightNode = nodeStack.pop();
            Node leftNode = nodeStack.pop();
            Node operatorNode = new Node();
            operatorNode.setType("operator");
            operatorNode.setValue(operatorStack.pop());
            operatorNode.setLeft(leftNode);
            operatorNode.setRight(rightNode);
            nodeStack.push(operatorNode);
        }

        return nodeStack.pop(); // Return the root node
    }

    public int precedence(String operator) {
        if (operator.equals("AND")) return 1;
        if (operator.equals("OR")) return 0;
        return -1; // Not an operator
    }

    public boolean evaluateNode(Node ruleNode, Map<String, Object> attributes) {
        if (ruleNode == null) {
            throw new IllegalArgumentException("Node cannot be null");
        }
        System.out.println("Evaluating node: " + ruleNode.getValue());

        // Evaluate based on the type of node
        if ("operand".equals(ruleNode.getType())) {
            String operand = (String) ruleNode.getValue();
            System.out.println("Operand: " + operand);

            // Directly evaluate the comparison without checking the format
            String[] parts = operand.split(" "); // Expecting format "attribute operator value"
            String attribute = parts[0]; // e.g., "age"
            String operator = parts[1];   // e.g., ">"
            String value = parts[2];       // e.g., "30"

            // Retrieve the actual value from attributes
            Object actualValue = attributes.get(attribute);
            System.out.println("Actual Value: " + actualValue); // Debug log

            // Perform the comparison based on the operator
            return compare(actualValue, operator, value);
        } else if ("operator".equals(ruleNode.getType())) {
            // Evaluate left and right child nodes
            boolean leftValue = evaluateNode(ruleNode.getLeft(), attributes);
            boolean rightValue = evaluateNode(ruleNode.getRight(), attributes);

            String operator = (String) ruleNode.getValue();
            if ("AND".equals(operator)) {
                return leftValue && rightValue;
            } else if ("OR".equals(operator)) {
                return leftValue || rightValue;
            }
        }

        throw new IllegalArgumentException("Invalid node type: " + ruleNode.getType());
    }
    private boolean isValidOperator(String operator) {
        return operator.equals(">") || operator.equals("<") || operator.equals(">=") ||
                operator.equals("<=") || operator.equals("==") || operator.equals("=") ||
                operator.equals("!=");
    }

    private boolean compare(Object actualValue, String operator, String value) {
        if (actualValue == null) {
            return false; // or throw an exception based on your needs
        }

        Comparable<?> comparableValue;
        if (actualValue instanceof Number) {
            if (actualValue instanceof Integer) {
                comparableValue = Integer.valueOf(value);
            } else if (actualValue instanceof Double) {
                comparableValue = Double.valueOf(value);
            } else if (actualValue instanceof Float) {
                comparableValue = Float.valueOf(value);
            } else {
                throw new IllegalArgumentException("Unsupported number type: " + actualValue.getClass());
            }
        } else {
            comparableValue = value; // Assuming it's a String
        }

        // Comparison logic
        switch (operator) {
            case ">":
                return ((Comparable<Object>) actualValue).compareTo(comparableValue) > 0;
            case "<":
                return ((Comparable<Object>) actualValue).compareTo(comparableValue) < 0;
            case ">=":
                return ((Comparable<Object>) actualValue).compareTo(comparableValue) >= 0;
            case "<=":
                return ((Comparable<Object>) actualValue).compareTo(comparableValue) <= 0;
            case "==":
            case "=":
                return actualValue.equals(comparableValue);
            case "!=":
                return !actualValue.equals(comparableValue);
            default:
                throw new IllegalArgumentException("Invalid operator: " + operator);
        }
    }
}
