package com.example.backbanque.services;

import com.example.backbanque.dtos.CustomerDTO;
import com.example.backbanque.entities.BankAccount;
import com.example.backbanque.entities.CurrentAccount;
import com.example.backbanque.entities.Customer;
import com.example.backbanque.entities.SavingAccount;
import com.example.backbanque.exceptions.BalanceNotSufficientException;
import com.example.backbanque.exceptions.BankAccountNotFoundException;
import com.example.backbanque.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;

    SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;

    //List<CustomerDTO> listCustomers();
    List<CustomerDTO> listCustomers();

    BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException;

    void debit(String accountID, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException;

    void credit(String accountID, double amount, String description) throws BankAccountNotFoundException;

    void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException;

    List<BankAccount> bankAccountList();

    CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(Long customerId);
}
