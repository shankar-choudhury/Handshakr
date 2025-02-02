package com.SWE2025.project.prototype.transaction;

import com.SWE2025.project.prototype.transaction.Transaction;
import com.SWE2025.project.prototype.user.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends CrudRepository<Transaction,Long> {
    List<Transaction> findByCustomer(User customer);
    List<Transaction> findByVendor(User vendor);
}
