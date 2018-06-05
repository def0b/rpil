package com.sberpr.rpil.controllers;

import com.sberpr.rpil.entity.AccountEntity;
import com.sberpr.rpil.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by defabey on 24-Mar-18.
 */
@RestController
@RequestMapping("/api/account")
public class ApiAccountController {

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(method = RequestMethod.DELETE)
    public boolean delete(@RequestParam UUID id) {
        if (accountRepository.existsById(id)) {
            accountRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @RequestMapping(method = RequestMethod.POST)
    public UUID add() {
        AccountEntity accountEntity = new AccountEntity();
        accountRepository.save(accountEntity);
        return accountEntity.getId();
    }

    @RequestMapping(method = RequestMethod.GET)
    public Integer getBalance(@RequestParam UUID id) {
        return accountRepository.findById(id)
            .map(AccountEntity::getBalance).orElse(null);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/all")
    public List<UUID> getAll() {
        return accountRepository.findAll().stream()
            .map(AccountEntity::getId)
            .collect(Collectors.toList());
    }
}
