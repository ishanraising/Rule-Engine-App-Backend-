package com.example.ruleengine.services;

import com.example.ruleengine.ast.Node;
import com.example.ruleengine.entity.Rule;

import java.util.List;
import java.util.Map;

public interface RuleService {

    public Node createRule(String ruleString);
    public Node combineRules(List<Node> rules);
    public boolean evaluateRule(Node root, Map<String, Object> attributes);
    public String serializeNode(Node node);
    public Node deserializeNode(String json);

    Rule saveRule(Rule rule);

    Rule findRuleById(Long ruleId);

    List<Rule> getAllRule();
     boolean isOperator(String token);
    public int precedence(String operator);
    public boolean compare(Object actualValue, String operator, String expectedValue);
}
