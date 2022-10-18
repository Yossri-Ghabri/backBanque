package com.example.backbanque.services;


import com.example.backbanque.entities.BankAccount;
import com.example.backbanque.entities.CurrentAccount;
import com.example.backbanque.entities.SavingAccount;
import com.example.backbanque.repositorys.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class BankService {
    @Autowired
    private BankAccountRepository bankAccountRepository;
    public void consulter(){
        BankAccount bankAccount = bankAccountRepository.findById("2203b912-7837-45b0-845d-186ca196ecc4").orElse(null);
        if (bankAccount != null) {
            System.out.println("*********************************************************");
            System.out.println("getId -> " + bankAccount.getId());
            System.out.println("getBalance   -> " + bankAccount.getBalance());
            System.out.println("getStatus  -> " + bankAccount.getStatus());
            System.out.println("getCreatedAt  -> " + bankAccount.getCreatedAt());
            System.out.println("getCreatedAt  -> " + bankAccount.getCreatedAt());
            System.out.println("getCustomerGetId  -> " + bankAccount.getCustomer().getId());
            System.out.println("getCustomerGetId  -> " + bankAccount.getCustomer().getId());
            System.out.println("getSimpleName  -> "+bankAccount.getClass().getSimpleName());
            if (bankAccount instanceof CurrentAccount) {
                System.out.println("over Draft ->  " + ((CurrentAccount) bankAccount).getOverDraft());
            } else if (bankAccount instanceof SavingAccount) {
                System.out.println("Rate -> " + ((SavingAccount) bankAccount).getInterestRate());
            }
            bankAccount.getAccountOperationList().forEach(x -> {
                System.out.println(x.getType() + "\t" + x.getOperationDate() + "\t" + x.getAmount());
            });
        }
    }
}
