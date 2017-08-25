package com.bobocode.dao;

import com.bobocode.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountDao {
    long save(Account account);

    Optional<Account> find(Long id);

    List<Account> findAll();

    void update(Account account);
}
