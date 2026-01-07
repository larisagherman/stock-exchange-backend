package com.stock_exchange_backend.stock_exchange_backend.repository;

import com.stock_exchange_backend.stock_exchange_backend.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
