package com.dl2.fyp.repository.account;

import com.dl2.fyp.entity.Account;
import com.dl2.fyp.enums.AccountCategory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {

    @Query(value = "select id, amount, category from t_account where user_id = ?1", nativeQuery = true)
    Optional<List<Account>> findAllAccount(Long userId);

    @Query(value = "select * from t_account where user_id = ?1 and category=?2", nativeQuery = true)
    Optional<Account> findAccount(Long userId, AccountCategory accountCategory);
}
