package com.muthahar.customer_management.service;

import com.muthahar.customer_management.exception.CustomerNotFoundException;
import com.muthahar.customer_management.model.Customer;
import com.muthahar.customer_management.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerService service;

    private UUID customerId;
    private Customer mockCustomer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerId = UUID.randomUUID();
        mockCustomer = new Customer();
        mockCustomer.setId(customerId);
        mockCustomer.setName("John Doe");
        mockCustomer.setEmail("john@example.com");
        mockCustomer.setAnnualSpend(BigDecimal.valueOf(1200));
        mockCustomer.setLastPurchaseDate(LocalDateTime.now().minusMonths(6));
    }

    @Test
    void testCreateCustomer() {
        Customer toSave = new Customer();
        toSave.setName("Jane Doe");
        toSave.setEmail("jane@example.com");
        when(repository.save(any(Customer.class))).thenReturn(mockCustomer);
        Customer result = service.createCustomer(toSave);
        assertNotNull(result);
        verify(repository).save(toSave);
    }

    @Test
    void testGetCustomerById_Success() {
        when(repository.findById(customerId)).thenReturn(Optional.of(mockCustomer));
        Optional<Customer> result = service.getCustomerById(customerId);
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
    }

    @Test
    void testGetCustomerById_NotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(CustomerNotFoundException.class, () -> service.getCustomerById(id));
    }

    @Test
    void testGetCustomerByName_Success() {
        when(repository.findByName("John Doe")).thenReturn(Optional.of(mockCustomer));
        Optional<Customer> result = service.getCustomerByName("John Doe");
        assertTrue(result.isPresent());
        assertEquals("john@example.com", result.get().getEmail());
    }

    @Test
    void testGetCustomerByEmail_Success() {
        when(repository.findByEmail("john@example.com")).thenReturn(Optional.of(mockCustomer));
        Optional<Customer> result = service.getCustomerByEmail("john@example.com");
        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
    }

    @Test
    void testUpdateCustomer_Success() {
        Customer updated = new Customer();
        updated.setName("Updated Name");
        updated.setEmail("updated@example.com");
        updated.setAnnualSpend(BigDecimal.valueOf(2000));
        updated.setLastPurchaseDate(LocalDateTime.now());

        when(repository.findById(customerId)).thenReturn(Optional.of(mockCustomer));
        when(repository.save(any(Customer.class))).thenReturn(updated);

        Optional<Customer> result = service.updateCustomer(customerId, updated);
        assertTrue(result.isPresent());
        assertEquals("Updated Name", result.get().getName());
    }

    @Test
    void testDeleteCustomer_Success() {
        when(repository.existsById(customerId)).thenReturn(true);
        doNothing().when(repository).deleteById(customerId);
        assertDoesNotThrow(() -> service.deleteCustomer(customerId));
        verify(repository).deleteById(customerId);
    }
    @Test
    void testDeleteCustomer_NotFound() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);
        assertThrows(CustomerNotFoundException.class, () -> service.deleteCustomer(id));
    }

    @Test
    void testCalculateTier_Platinum() {
        mockCustomer.setAnnualSpend(BigDecimal.valueOf(15000));
        mockCustomer.setLastPurchaseDate(LocalDateTime.now().minusMonths(3));
        assertEquals("Platinum", service.calculateTier(mockCustomer));
    }

    @Test
    void testCalculateTier_Gold() {
        mockCustomer.setAnnualSpend(BigDecimal.valueOf(5000));
        mockCustomer.setLastPurchaseDate(LocalDateTime.now().minusMonths(9));
        assertEquals("Gold", service.calculateTier(mockCustomer));
    }

    @Test
    void testCalculateTier_Silver() {
        mockCustomer.setAnnualSpend(BigDecimal.valueOf(500));
        assertEquals("Silver", service.calculateTier(mockCustomer));
    }

    @Test
    void testCalculateTier_NullSpend() {
        mockCustomer.setAnnualSpend(null);
        assertEquals("Silver", service.calculateTier(mockCustomer));
    }


}