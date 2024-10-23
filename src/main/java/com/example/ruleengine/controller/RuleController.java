package com.example.ruleengine.controller;

import com.example.ruleengine.ast.Node;
import com.example.ruleengine.ast.RuleParser;
import com.example.ruleengine.entity.Rule;
import com.example.ruleengine.services.RuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@RestController
@RequestMapping("/rule")
public class RuleController {

    private final RuleService ruleService;
    private final RuleParser parser;

    @PostMapping("/createrule")
    public ResponseEntity<Rule> createRule(@RequestBody String ruleString) {
        Node ast = ruleService.createRule(ruleString);
        // Save rule to database
        Rule rule = new Rule();
        rule.setRuleString(ruleString);
        rule.setAstJson(ruleService.serializeNode(ast)); // Serialize Node to JSON
        return ResponseEntity.ok(ruleService.saveRule(rule));
    }

    @PostMapping("/evaluate")
    public ResponseEntity<Boolean> evaluateRule(@RequestBody Map<String, Object> request) {

        String rule = (String) request.get("rule");
        System.out.println("rule" + rule);

        Map<String, Object> attributes = (Map<String, Object>) request.get("attributes");
        attributes.values().removeIf(Objects::isNull);
        System.out.println(attributes);

        try {
            // Parse the rule into a Node structure
            Node ruleNode = parser.parseRule(rule);
            // Evaluate the rule against the provided attributes
            boolean result = parser.evaluateNode(ruleNode, attributes);
            return ResponseEntity.ok(result); // Return the evaluation result
        } catch (IllegalArgumentException e) {
            // Handle invalid rule format
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rule> updateRule(@PathVariable Long id, @RequestBody String newRuleString) {
        Rule existingRule = ruleService.findRuleById(id);
        Node newAst = ruleService.createRule(newRuleString); // Create a new AST from the new rule string
        existingRule.setRuleString(newRuleString);
        existingRule.setAstJson(ruleService.serializeNode(newAst)); // Update AST JSON

        return ResponseEntity.ok(ruleService.saveRule(existingRule)); // Save updated rule
    }

    @GetMapping("/allrules")
    public ResponseEntity<List<Rule>> getAllRules() {
        return ResponseEntity.ok(ruleService.getAllRule());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rule> getRuleById(@PathVariable Long id) {
        Rule rule = ruleService.findRuleById(id);
        return ResponseEntity.ok(rule);
    }
}
