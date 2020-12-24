package com.dl2.fyp.repository;

import com.dl2.fyp.entity.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
}
