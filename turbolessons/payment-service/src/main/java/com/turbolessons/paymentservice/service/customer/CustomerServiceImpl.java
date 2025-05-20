package com.turbolessons.paymentservice.service.customer;

import com.stripe.StripeClient;
import com.stripe.model.Customer;
import com.stripe.model.StripeCollection;
import com.stripe.model.Subscription;
import com.stripe.param.*;
import com.turbolessons.paymentservice.dto.Address;
import com.turbolessons.paymentservice.dto.CustomerData;
import com.turbolessons.paymentservice.service.StripeClientHelper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

//You can perform searches on metadata that you’ve added to objects that support it.
//
// metadata search: metadata["<field>"]:"<value>".
//
// queries for records with a donation ID of “asdf-jkl”: metadata["donation-id"]:"asdf-jkl".
//
// You can query for the presence of a metadata key on an object. The following clause would match all records where donation-id is a metadata key. -metadata["donation-id"]:null

@Service
public class CustomerServiceImpl implements CustomerService {

    private final StripeClient stripeClient;
    private final StripeClientHelper stripeClientHelper;

    public CustomerServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
    }

    //     List All Customers
    @Override
    public Mono<StripeCollection<Customer>> listAllCustomers() {
        return stripeClientHelper.executeStripeCall(() -> stripeClient.customers()
                .list(CustomerListParams.builder()
                              .addExpand("data.subscriptions")
                              .build()));
    }

    //    Retrieve a Customer
    @Override
    public Mono<CustomerData> retrieveCustomer(String id) {
        CustomerRetrieveParams params = CustomerRetrieveParams.builder()
                .addExpand("subscriptions")
                .build();
        return stripeClientHelper.executeStripeCall(() -> this.stripeClient.customers()
                        .retrieve(id,
                                  params))
                .map(this::mapCustomerToDto);
    }

    //    Search Customers
    private CustomerData mapCustomerToDto(Customer customer) {
        List<String> subscriptionIds = new ArrayList<>();

        // Extract subscription IDs
        if (customer.getSubscriptions() != null && customer.getSubscriptions()
                .getData() != null) {
            for (Subscription subscription : customer.getSubscriptions()
                    .getData()) {
                subscriptionIds.add(subscription.getId());
            }
        }

        return new CustomerData(customer.getId(),
                                customer.getAddress() != null ? mapAddress(customer.getAddress()) : null,
                                customer.getEmail(),
                                customer.getName(),
                                customer.getPhone(),
                                customer.getInvoiceSettings() != null ? customer.getInvoiceSettings()
                                       .getDefaultPaymentMethod() : null,
                                customer.getDescription(),
                                customer.getMetadata(),
                                subscriptionIds
                                // Set subscription IDs
        );
    }

    private Address mapAddress(com.stripe.model.Address stripeAddress) {
        if (stripeAddress == null) {
            return null; // Handle cases where address is not set
        }

        return new Address(stripeAddress.getCity(),
                           stripeAddress.getCountry(),
                           stripeAddress.getLine1(),
                           stripeAddress.getLine2(),
                           stripeAddress.getPostalCode(),
                           stripeAddress.getState());
    }

    @Override
    public Mono<CustomerData> searchCustomerBySystemId(String id) {
        String query = String.format("metadata['okta_id']:'%s'",
                                     id);
        CustomerSearchParams params = CustomerSearchParams.builder()
                .addExpand("data.subscriptions")
                .setQuery(query)
                .build();

        return stripeClientHelper.executeStripeCall(() -> this.stripeClient.customers()
                        .search(params))
                .flatMap(customerSearchResult -> {
                    if (customerSearchResult.getData() != null && !customerSearchResult.getData()
                            .isEmpty()) {
                        Customer customer = customerSearchResult.getData()
                                .get(0);
                        return Mono.just(mapCustomerToDto(customer));
                    } else {
                        return Mono.empty();
                    }
                });
    }

    //    Create a Customer
    @Override
    public Mono<CustomerData> createCustomer(CustomerData customerData) {
        System.out.println("Received createCustomer Request:" + customerData);

        Address address = customerData.getAddress();
        CustomerCreateParams customerParams = CustomerCreateParams.builder()
                .setAddress(CustomerCreateParams.Address.builder()
                                    .setCity(address.getCity())
                                    .setCountry("US")
                                    .setLine1(address.getLine1())
                                    .setLine2(address.getLine2())
                                    .setState(address.getState())
                                    .setPostalCode(address.getPostalCode())
                                    .build())
                .setEmail(customerData.getEmail())
                .setName(customerData.getName())
                .setPhone(customerData.getPhone())
                .setMetadata(customerData.getMetadata())
                .build();
        System.out.println("CustomerCreateParams: " + customerParams);
        return stripeClientHelper.executeStripeCall(() -> this.stripeClient.customers()
                        .create(customerParams))
                .map(this::mapCustomerToDto);

    }

    //    Update a Customer
    @Override
    public Mono<Void> updateCustomer(String id, CustomerData customerData) {
        Address address = customerData.getAddress();
        CustomerUpdateParams customerParams = CustomerUpdateParams.builder()
                .setAddress(CustomerUpdateParams.Address.builder()
                                    .setCity(address.getCity())
                                    .setCountry("US")
                                    .setLine1(address.getLine1())
                                    .setLine2(address.getLine2())
                                    .setState(address.getState())
                                    .setPostalCode(address.getPostalCode())
                                    .build())
                .setEmail(customerData.getEmail())
                .setName(customerData.getName())
                .setPhone(customerData.getPhone())
//                .setInvoiceSettings(CustomerUpdateParams.InvoiceSettings.builder()
//                                            .setDefaultPaymentMethod(customerData.getDefaultPaymentMethod())
//                                            .build())
                .build();

        return stripeClientHelper.executeStripeVoidCall(() -> this.stripeClient.customers()
                .update(id,
                        customerParams));
    }

    //    Delete a Customer
    @Override
    public Mono<Void> deleteCustomer(String id) {
        return stripeClientHelper.executeStripeVoidCall(() -> this.stripeClient.customers()
                .delete(id));
    }
}
