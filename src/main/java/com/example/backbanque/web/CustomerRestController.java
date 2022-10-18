package com.example.backbanque.web;

import com.example.backbanque.dtos.CustomerDTO;
import com.example.backbanque.entities.Customer;
import com.example.backbanque.exceptions.CustomerNotFoundException;
import com.example.backbanque.services.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@AllArgsConstructor
@RestController
public class CustomerRestController {
    private BankAccountService bankAccountService;

    @GetMapping("/customers")
    public List<CustomerDTO> customers() {
        return bankAccountService.listCustomers();
    }


    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomerDTO (@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException {

        return bankAccountService.getCustomer(customerId);
    }

    @PostMapping("/saveCustomer")
    public CustomerDTO saveNewCustomer(@RequestBody CustomerDTO customerDTO){
        return bankAccountService.saveCustomer(customerDTO);
    }
    @PutMapping("/updateCustomer/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable Long customerId,@RequestBody CustomerDTO customerDTO){
        customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerDTO);
    }

    @DeleteMapping("/deleteCustomer/{id}")
    public void deleteCustomer(@PathVariable Long id){
        bankAccountService.deleteCustomer(id);
    }






    public CustomerRestController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }
}
