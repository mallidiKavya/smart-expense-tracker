package com.example.smart_expense_manager.controller;

import com.example.smart_expense_manager.entity.Transaction;
import com.example.smart_expense_manager.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    // Add transaction
    @PostMapping
    public Transaction addTransaction(@RequestBody Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    // Get all transactions of a user
    @GetMapping("/user/{userId}")
    public List<Transaction> getUserTransactions(@PathVariable Long userId) {
        return transactionRepository.findByUserId(userId);
    }

    // Delete transaction
    @DeleteMapping("/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        transactionRepository.deleteById(id);
    }

    // Update transaction
    @PutMapping("/{id}")
    public Transaction updateTransaction(
            @PathVariable Long id,
            @RequestBody Transaction transaction) {

        Transaction existingTransaction =
                transactionRepository.findById(id).orElse(null);

        if (existingTransaction == null) {
            throw new RuntimeException("Transaction not found");
        }

        existingTransaction.setType(transaction.getType());
        existingTransaction.setAmount(transaction.getAmount());
        existingTransaction.setCategory(transaction.getCategory());
        existingTransaction.setDescription(transaction.getDescription());
        existingTransaction.setTransactionDate(transaction.getTransactionDate());

        return transactionRepository.save(existingTransaction);
    }
}