package com.example.backbanque.mappers;

import com.example.backbanque.dtos.CustomerDTO;
import com.example.backbanque.entities.Customer;
import com.fasterxml.jackson.databind.util.BeanUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {

    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        // ou manuelle
        BeanUtils.copyProperties(customer, customerDTO);
        // ou static
        //  customerDTO.setId(customer.getId());
        // customerDTO.setName(customer.getName());
        //customerDTO.setEmail((customer.getEmail()));

        return customerDTO;
    }


    public Customer fromCustomerDTO(CustomerDTO CustomerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customer, CustomerDTO);
        return customer;
    }

}
