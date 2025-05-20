package com.turbolessons.paymentservice.service.price;

import com.turbolessons.paymentservice.dto.PriceData;
import com.stripe.model.Price;
import com.stripe.model.StripeCollection;
import com.stripe.param.PriceCreateParams;
import reactor.core.publisher.Mono;

public interface PricingService {
//    void initializeStandardRate();
//
//    Mono<Price> getStandardRate();

    Mono<StripeCollection<Price>> listAllPrices();

    Mono<Price> retrievePrice(String id);

    Mono<Price> createPrice(PriceCreateParams params);

    Mono<Price> createPrice(PriceData priceData);

    Mono<Void> updatePrice(String id, PriceData priceData);
}
