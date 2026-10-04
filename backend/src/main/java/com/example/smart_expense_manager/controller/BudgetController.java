package com.example.smart_expense_manager.controller;

import com.example.smart_expense_manager.entity.Budget;
import com.example.smart_expense_manager.repository.BudgetRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/budgets")
@CrossOrigin
public class BudgetController {

    @Autowired
    private BudgetRepository budgetRepository;

    // Save budget
    @PostMapping
    public Budget addBudget(@RequestBody Budget budget) {
        return budgetRepository.save(budget);
    }

    // Get budgets of a user
    @GetMapping("/user/{userId}")
    public List<Budget> getUserBudgets(@PathVariable Long userId) {
        return budgetRepository.findByUserId(userId);
    }

    // Delete budget
    @DeleteMapping("/{id}")
    public void deleteBudget(@PathVariable Long id) {
        budgetRepository.deleteById(id);
    }
}