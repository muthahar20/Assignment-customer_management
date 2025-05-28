package com.muthahar.customer_management;

import com.muthahar.customer_management.model.Customer;
import com.muthahar.customer_management.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CustomerManagementApplicationTests {

	private CustomerService service;

	@BeforeEach
	void setup() {
		service = new CustomerService();
	}
	@Test
	void contextLoads() {
	}

	@Test
	void testTierCalculation() {
		Customer c1 = new Customer();
		c1.setAnnualSpend(BigDecimal.valueOf(15000.0));
		c1.setLastPurchaseDate(LocalDateTime.now().minusMonths(3));
		assertEquals("Platinum", service.calculateTier(c1));

		Customer c2 = new Customer();
		c2.setAnnualSpend(BigDecimal.valueOf(2000.0));
		c2.setLastPurchaseDate(LocalDateTime.now().minusMonths(8));
		assertEquals("Gold", service.calculateTier(c2));

		Customer c3 = new Customer();
		c3.setAnnualSpend(BigDecimal.valueOf(500.0));
		assertEquals("Silver", service.calculateTier(c3));
	}


}
