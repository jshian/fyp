package com.dl2.fyp.repository.account;

import com.dl2.fyp.entity.Account;
import com.dl2.fyp.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {

    Optional<List<Account>> getAccountsByUser(Long id);
}
