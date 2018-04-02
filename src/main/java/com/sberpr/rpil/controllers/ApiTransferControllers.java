package com.sberpr.rpil.controllers;

import com.sberpr.rpil.entity.AccountEntity;
import com.sberpr.rpil.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.UUID;




@RestController
@RequestMapping("/api/transfers")
public class ApiTransferControllers {
    @Autowired
    private AccountRepository accountRepository;


    @RequestMapping(method = RequestMethod.PUT, path = "/decrease")
    public boolean decrease(@RequestParam UUID id, @RequestParam Integer sum) {
        try {
            AccountEntity accountEntity = accountRepository.findById(id).orElseThrow(EntityNotFoundException::new);
            if (accountEntity.getBalance() < sum) {
                return false;
            }
            accountEntity.setBalance(accountEntity.getBalance() - sum);
            accountRepository.save(accountEntity);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }


    }

    @RequestMapping(method = RequestMethod.PUT, path = "/increase")
    public boolean increase(@RequestParam UUID id, @RequestParam Integer sum) {
        try {
            AccountEntity accountEntity = accountRepository.findById(id).orElseThrow(EntityNotFoundException::new);

            accountEntity.setBalance(accountEntity.getBalance() + sum);
            accountRepository.save(accountEntity);
            return true;
        } catch (EntityNotFoundException e) {
            return false;
        }
    }

    @Transactional
    @RequestMapping(method = RequestMethod.PUT, path = "/transfer")
    public boolean transfer(@RequestParam UUID source, @RequestParam UUID dest, @RequestParam Integer sum) {
        Object savepoint = TransactionAspectSupport.currentTransactionStatus().createSavepoint();
        if (!decrease(source, sum)) {
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savepoint);
            return false;
        }
        if (!increase(dest, sum) ){
            TransactionAspectSupport.currentTransactionStatus().rollbackToSavepoint(savepoint);
            return false;

        }
        TransactionAspectSupport.currentTransactionStatus().flush();
        return true;
    }
}
