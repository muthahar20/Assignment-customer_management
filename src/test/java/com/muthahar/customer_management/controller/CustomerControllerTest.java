package com.muthahar.customer_management.controller;

import com.muthahar.customer_management.model.Customer;
import com.muthahar.customer_management.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private UUID customerId;
    private Customer customer;

    @BeforeEach
    void setUp() {
        customerId = UUID.randomUUID();
        customer = new Customer();
        customer.setId(customerId);
        customer.setName("test_Customer");
        customer.setEmail("test_Customer@example.com");
    }

    @Test
    void testGetById_Success() {
        when(customerService.getCustomerById(customerId)).thenReturn(Optional.of(customer));

        ResponseEntity<?> response = customerController.getById(customerId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(customer, response.getBody());
    }

    @Test
    void testGetById_NotFound() {
        when(customerService.getCustomerById(customerId)).thenReturn(Optional.empty());

        ResponseEntity<?> response = customerController.getById(customerId);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetByQuery_NameFound() {
        when(customerService.getCustomerByName("test_Customer")).thenReturn(Optional.of(customer));

        ResponseEntity<?> response = customerController.getByQuery("test_Customer", null);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(customer, response.getBody());
    }

    @Test
    void testUpdate_Success() {
        when(customerService.updateCustomer(eq(customerId), any(Customer.class))).thenReturn(Optional.of(customer));

        ResponseEntity<?> response = customerController.update(customerId, customer);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(customer, response.getBody());
    }

    @Test
    void testDelete_Success() {
        doNothing().when(customerService).deleteCustomer(customerId);

        ResponseEntity<?> response = customerController.delete(customerId);

        assertEquals(204, response.getStatusCodeValue());
    }

}