package com.muthahar.customer_management.service;

import com.muthahar.customer_management.model.Customer;
import com.muthahar.customer_management.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository repository;

    public Customer createCustomer( Customer customer){
        customer.setId(null);
        return repository.save(customer);
    }

    public Optional<Customer> getCustomerById(UUID id){
        return repository.findById(id);
    }

    public Optional<Customer> getCustomerByName(String name) {
        return repository.findByName(name);
    }

    public Optional<Customer> getCustomerByEmail(String email) {
        return repository.findByEmail(email);
    }

    public List<Customer> getAll() {
        return repository.findAll();
    }

    public Optional<Customer> updateCustomer(UUID id, Customer customer){
        return repository.findById(id).map( existing -> {
            existing.setName(customer.getName());
            existing.setEmail((customer.getEmail()));
            existing.setAnnualSpend(customer.getAnnualSpend());
            existing.setLastPurchaseDate(customer.getLastPurchaseDate());
            return repository.save(existing);
        });
    }
    public void deleteCustomer(UUID id) {
        repository.deleteById(id);
    }

    public String calculateTier(Customer customer) {
        if (customer.getAnnualSpend() == null) return "Silver";

        LocalDateTime cutoffDate = LocalDateTime.now();
        if (customer.getAnnualSpend().compareTo(BigDecimal.valueOf(10000)) >= 0 &&
                customer.getLastPurchaseDate() != null &&
                customer.getLastPurchaseDate().isAfter(cutoffDate.minusMonths(6))) {
            return "Platinum";
        } else if (customer.getAnnualSpend().compareTo(BigDecimal.valueOf(1000)) >= 0 &&
                customer.getLastPurchaseDate() != null &&
                customer.getLastPurchaseDate().isAfter(cutoffDate.minusMonths(12))) {
            return "Gold";
        }
        return "Silver";
    }




}
