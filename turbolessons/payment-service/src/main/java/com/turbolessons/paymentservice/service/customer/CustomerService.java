package com.turbolessons.paymentservice.service.customer;

import com.turbolessons.paymentservice.dto.CustomerData;
import com.stripe.model.Customer;
import com.stripe.model.StripeCollection;
import reactor.core.publisher.Mono;

public interface CustomerService {
    //     List All Customers
    Mono<StripeCollection<Customer>> listAllCustomers();

    //    Retrieve a Customer
    Mono<CustomerData> retrieveCustomer(String id);

    Mono<CustomerData> searchCustomerBySystemId(String id);

    //    Create a Customer
    Mono<CustomerData> createCustomer(CustomerData customerData);

    //    Update a Customer
    Mono<Void> updateCustomer(String id, CustomerData customerData);

    //    Delete a Customer
    Mono<Void> deleteCustomer(String id);
}
