package com.muthahar.customer_management.controller;

import com.muthahar.customer_management.model.Customer;
import com.muthahar.customer_management.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService service;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable UUID id) {
        Optional<Customer> customer = service.getCustomerById(id);
        return customer.map(c -> ResponseEntity.ok(c)).orElse(ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<?> getByQuery(@RequestParam(required = false) String name,
                                        @RequestParam(required = false) String email) {
        if (name != null) return service.getCustomerByName(name).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        if (email != null) return service.getCustomerByEmail(email).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
        return ResponseEntity.ok(service.getAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Customer customer) {
        return service.updateCustomer(id, customer).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        service.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

}
