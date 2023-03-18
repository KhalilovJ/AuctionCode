package az.code.auctionbackend.controllers;


import az.code.auctionbackend.entities.Account;
import az.code.auctionbackend.entities.Lot;
import az.code.auctionbackend.services.interfaces.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/open/api/account")
@RequiredArgsConstructor
@Log4j2
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {

        return ResponseEntity.ok(accountService.getAccountDetails(id));
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAccountById() {

        return ResponseEntity.ok(accountService.getAllAccounts());
    }
}