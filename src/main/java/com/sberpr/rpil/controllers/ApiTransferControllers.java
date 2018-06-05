package com.sberpr.rpil.controllers;

import com.sberpr.rpil.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Created by defabey on 24-Mar-18.
 */

@RestController
@RequestMapping("/api/transfers")
public class ApiTransferControllers {

    @Autowired
    private AccountService accountService;

    @RequestMapping(method = RequestMethod.PUT, path = "/decrease")
    public boolean decrease(@RequestParam UUID id, @RequestParam Integer sum) {
        return accountService.changeAccountBalance(id, -1 * sum);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/increase")
    public boolean increase(@RequestParam UUID id, @RequestParam Integer sum) {
        return accountService.changeAccountBalance(id, sum);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/transfer")
    public boolean transfer(@RequestParam UUID source, @RequestParam UUID dest, @RequestParam Integer sum) {
        return accountService.transfer(source, dest, sum);
    }
}
