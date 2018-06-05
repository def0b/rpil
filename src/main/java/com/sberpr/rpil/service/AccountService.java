package com.sberpr.rpil.service;

import com.sberpr.rpil.entity.AccountEntity;
import com.sberpr.rpil.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.UUID;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public AccountEntity getAccountById(UUID id) {
        return accountRepository.getOne(id);
    }

    @Transactional
    public boolean changeAccountBalance(UUID id, int d) {
        AccountEntity entity = getAccountById(id);
        int newBalance = entity.getBalance() + d;
        if (newBalance >= 0) {
            entity.setBalance(newBalance);
            accountRepository.save(entity);
            return true;
        }
        return false;
    }

    @Transactional
    // Перевод отрицательной суммы дело нормальное. Если недопустимо по требованиям, то проверять в контроллере
    public boolean transfer(UUID source, UUID dest, int d) {
        boolean t = changeAccountBalance(source, -1 * d);
        t &= changeAccountBalance(dest, d);
        if (!t) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return t;
    }
}
