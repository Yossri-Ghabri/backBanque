package com.example.backbanque;

import com.example.backbanque.dtos.CustomerDTO;
import com.example.backbanque.entities.*;
import com.example.backbanque.enumes.AccountStatus;
import com.example.backbanque.enumes.OprerationType;
import com.example.backbanque.exceptions.BalanceNotSufficientException;
import com.example.backbanque.exceptions.BankAccountNotFoundException;
import com.example.backbanque.exceptions.CustomerNotFoundException;
import com.example.backbanque.repositorys.AccountOperationRepository;
import com.example.backbanque.repositorys.BankAccountRepository;
import com.example.backbanque.repositorys.CustomerRepository;
import com.example.backbanque.services.BankAccountService;
import com.example.backbanque.services.BankService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class BackBanqueApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackBanqueApplication.class, args);
        System.out.println("hello yossri");
    }
/*
    @Bean
    CommandLineRunner commandLineRunner(BankService bankService){
        return args -> {
            bankService.consulter();
        };
    }
  */

/*
    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
        return args -> {
            Stream.of("ali", "alex", "jhon", "salah").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "gmail.com");
                bankAccountService.saveCustomer(customer);
            });
        };
    }
*/


    @Bean
    CommandLineRunner commandLineRunner(BankAccountService bankAccountService) {
        return args -> {
            Stream.of("ali", "alex", "jhon", "salah").forEach(name -> {
                CustomerDTO customerDto = new CustomerDTO();
                customerDto.setName(name);
                customerDto.setEmail(name + "gmail.com");
                bankAccountService.saveCustomer(customerDto);
            });
            bankAccountService.listCustomers().forEach(client -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random() * 10000, 9000, client.getId());
                    bankAccountService.saveSavingBankAccount(Math.random() * 123000, 3.1, client.getId());
                    List<BankAccount> bankAccounts = bankAccountService.bankAccountList();
                    for (BankAccount bankAccount : bankAccounts) {
                        for (int i = 0; i < 10; i++) {
                            try {
                                bankAccountService.credit(bankAccount.getId(), 10200030 + Math.random() * 120000, "Credit");
                                bankAccountService.debit(bankAccount.getId(), 1000 + Math.random() * 900, "Credit");
                            } catch (BankAccountNotFoundException | BalanceNotSufficientException e) {
                                e.printStackTrace();
                            }
                        }
                    }

/*
                    bankAccountService.bankAccountList().forEach(account->{
                        for(int i=0; i<10; i++){
                            try {
                                bankAccountService.credit(account.getId(),10200030+Math.random()*120000, "Credit");
                            } catch (BankAccountNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    });
*/


                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }
            });
        };
    }


    //   @Bean
    CommandLineRunner start(CustomerRepository customerRepository,
                            BankAccountRepository bankAccountRepository,
                            AccountOperationRepository accountOperationRepository) {
        return args -> {
            Stream.of("Salah", "Ali", "Mohammed").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name + "@gamil.com");
                customerRepository.save(customer);
            });

            customerRepository.findAll().forEach(cust -> {
                CurrentAccount currentAccount = new CurrentAccount();
                currentAccount.setId(UUID.randomUUID().toString());
                currentAccount.setBalance(Math.random() * 900);
                currentAccount.setCreatedAt(new Date());
                currentAccount.setStatus(AccountStatus.CREATED);
                currentAccount.setCustomer(cust);
                currentAccount.setOverDraft(90000);
                bankAccountRepository.save(currentAccount);


                SavingAccount savingAccount = new SavingAccount();
                savingAccount.setId(UUID.randomUUID().toString());
                savingAccount.setBalance(Math.random() * 900);
                savingAccount.setCreatedAt(new Date());
                savingAccount.setStatus(AccountStatus.CREATED);
                savingAccount.setCustomer(cust);
                savingAccount.setInterestRate(7.5);
                bankAccountRepository.save(savingAccount);
            });
            //   System.out.println("bankAccount" + bankAccountRepository.findAll());


            bankAccountRepository.findAll().forEach(acc -> {
                AccountOperation accountOperation = new AccountOperation();

                for (int i = 0; i < 20; i++) {
                    accountOperation.setOperationDate(new Date());
                    accountOperation.setAmount(Math.random() * 12000);
                    accountOperation.setType(Math.random() > 0.5 ? OprerationType.DEBIT : OprerationType.CREDIT);
                    accountOperation.setBankAccount(acc);
                    accountOperationRepository.save(accountOperation);
                }
            });
        };
    }

}
