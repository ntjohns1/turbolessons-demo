package com.turbolessons.paymentservice.service.subscription;

import com.stripe.StripeClient;
import com.stripe.model.StripeCollection;
import com.stripe.model.SubscriptionItem;
import com.stripe.param.SubscriptionItemCreateParams;
import com.stripe.param.SubscriptionItemListParams;
import com.stripe.param.SubscriptionItemUpdateParams;
import com.turbolessons.paymentservice.dto.SubscriptionItemData;
import com.turbolessons.paymentservice.service.StripeClientHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class SubscriptionItemServiceImpl implements SubscriptionItemService {

    private final StripeClient stripeClient;
    private final StripeClientHelper stripeClientHelper;

    public SubscriptionItemServiceImpl(StripeClient stripeClient, StripeClientHelper stripeClientHelper) {
        this.stripeClient = stripeClient;
        this.stripeClientHelper = stripeClientHelper;
    }

    private SubscriptionItemData mapSubscriptionItemToDto(SubscriptionItem subscriptionItem) {
        return new SubscriptionItemData(
                subscriptionItem.getId(),
                subscriptionItem.getSubscription(),
                subscriptionItem.getPrice().getId(),
                subscriptionItem.getQuantity().intValue(),
                null,
                null,
                null,
                null,
                subscriptionItem.getMetadata() != null ? subscriptionItem.getMetadata().toString() : null
        );
    }

    // Create a subscription item
    @Override
    public Mono<SubscriptionItemData> createSubscriptionItem(SubscriptionItemData subscriptionItemData) {
        SubscriptionItemCreateParams params = SubscriptionItemCreateParams.builder()
                .setSubscription(subscriptionItemData.subscription())
                .setPrice(subscriptionItemData.price())
                .setQuantity(subscriptionItemData.quantity() != null ? subscriptionItemData.quantity().longValue() : null)
                .build();

        return stripeClientHelper.executeStripeCall(() -> stripeClient.subscriptionItems().create(params))
                .map(this::mapSubscriptionItemToDto);
    }

    // Retrieve a subscription item by id
    @Override
    public Mono<SubscriptionItem> retrieveSubscriptionItem(String id) {
        return stripeClientHelper.executeStripeCall(() -> stripeClient.subscriptionItems().retrieve(id));
    }

    // Update a subscription item
    @Override
    public Mono<Void> updateSubscriptionItem(String id, SubscriptionItemData subscriptionItemData) {
        SubscriptionItemUpdateParams.Builder paramsBuilder = SubscriptionItemUpdateParams.builder();
        
        if (subscriptionItemData.price() != null) {
            paramsBuilder.setPrice(subscriptionItemData.price());
        }
        
        if (subscriptionItemData.quantity() != null) {
            paramsBuilder.setQuantity(subscriptionItemData.quantity().longValue());
        }
        
        if (subscriptionItemData.proration_date() != null) {
            paramsBuilder.setProrationDate(subscriptionItemData.proration_date());
        }
        
        if (subscriptionItemData.tax_rates() != null && subscriptionItemData.tax_rates().length > 0) {
            // Convert String[] to List<String> for the setTaxRates method
            List<String> taxRatesList = Arrays.asList(subscriptionItemData.tax_rates());
            paramsBuilder.setTaxRates(taxRatesList);
        }
        
        SubscriptionItemUpdateParams params = paramsBuilder.build();
        
        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.subscriptionItems().update(id, params));
    }

    // Delete a subscription item
    @Override
    public Mono<Void> deleteSubscriptionItem(String id) {
        return stripeClientHelper.executeStripeVoidCall(() -> stripeClient.subscriptionItems().delete(id));
    }

    // List all subscription items for a subscription
    @Override
    public Mono<StripeCollection<SubscriptionItem>> listSubscriptionItems(String subscriptionId) {
        SubscriptionItemListParams params = SubscriptionItemListParams.builder()
                .setSubscription(subscriptionId)
                .build();
                
        return stripeClientHelper.executeStripeCall(() -> stripeClient.subscriptionItems().list(params));
    }
}
