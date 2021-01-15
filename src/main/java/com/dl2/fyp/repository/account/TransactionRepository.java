package com.dl2.fyp.repository.account;

import com.dl2.fyp.entity.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    @Query(value = "select * from t_transaction where account_in_id = ?1 or account_out_id = ?1", nativeQuery = true)
    public Optional<List<Transaction>> getAllTransactionByAccountId(Long accountId);
}
