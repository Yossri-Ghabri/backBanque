package com.example.backbanque.services;

import com.example.backbanque.dtos.CustomerDTO;
import com.example.backbanque.entities.*;
import com.example.backbanque.enumes.OprerationType;
import com.example.backbanque.exceptions.BalanceNotSufficientException;
import com.example.backbanque.exceptions.BankAccountNotFoundException;
import com.example.backbanque.exceptions.CustomerNotFoundException;
import com.example.backbanque.mappers.BankAccountMapperImpl;
import com.example.backbanque.repositorys.AccountOperationRepository;
import com.example.backbanque.repositorys.BankAccountRepository;
import com.example.backbanque.repositorys.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional                      // a chaque fois qui fair l'appel l'operation le spring va demarer transaction si la methode genere une exception fait call back si nn fait commit
//@AllArgsConstructor
//@Slf4j    // lambomk pour les msg Logger declare par lambok


public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository bankAccountRepository;
    private AccountOperationRepository accountOperationRepository;
    private BankAccountMapperImpl dtoMapper;

    Logger log = LoggerFactory.getLogger(this.getClass().getName());  // declare manuellement ls msg Logger


    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new client");
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public CurrentAccount saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Client not found");

        CurrentAccount currentAccount = new CurrentAccount();

        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
        return savedBankAccount;
    }

    @Override
    public SavingAccount saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Client not found");

        SavingAccount savingAccount = new SavingAccount();

        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return savedBankAccount;
    }


    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers =   customerRepository.findAll();
        List<CustomerDTO> collect =  customers
                .stream().
                        map(customer -> dtoMapper.fromCustomer(customer))
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new BankAccountNotFoundException("BankAccount Not Found"));
        return bankAccount;
    }

    @Override
    public void debit(String accountID, double amount, String description) throws BankAccountNotFoundException, BalanceNotSufficientException {
        BankAccount bankAccount= getBankAccount(accountID);
        if(bankAccount.getBalance()<amount) throw new BalanceNotSufficientException("Sold Not Sufficient");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OprerationType.DEBIT);
        accountOperation.setAmount(amount);
       // accountOperation.setId(bankAccount.getId());;
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()-amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountID, double amount, String description) throws BankAccountNotFoundException {
        BankAccount bankAccount= getBankAccount(accountID);
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OprerationType.CREDIT);
        accountOperation.setAmount(amount);
        accountOperation.setDescription(description);
        accountOperation.setOperationDate(new Date());
        accountOperationRepository.save(accountOperation);
        bankAccount.setBalance(bankAccount.getBalance()+amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BalanceNotSufficientException {
        debit(accountIdSource,amount,"Transfer to "+accountIdDestination);
        credit(accountIdDestination,amount,"Transfer from "+accountIdSource);
    }

    @Override
    public List<BankAccount> bankAccountList(){
        return bankAccountRepository.findAll();
    }

    @Override
    public CustomerDTO getCustomer(Long customerId) throws CustomerNotFoundException {
      Customer customer=  customerRepository.findById(customerId).orElseThrow(()-> new CustomerNotFoundException("customer not found"));
        return dtoMapper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("Saving new client");
        Customer customer=dtoMapper.fromCustomerDTO(customerDTO);
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId){
        customerRepository.deleteById(customerId);
    }













    public BankAccountServiceImpl(CustomerRepository customerRepository,
                                  BankAccountRepository bankAccountRepository,
                                  AccountOperationRepository accountOperationRepository,
                                  BankAccountMapperImpl dtoMapper) {
        this.customerRepository = customerRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.accountOperationRepository = accountOperationRepository;
        this.dtoMapper = dtoMapper;

    }

}
