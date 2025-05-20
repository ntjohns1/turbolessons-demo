package com.turbolessons.paymentservice.controller.subscription;

import com.stripe.model.SubscriptionItem;
import com.turbolessons.paymentservice.controller.BaseHandler;
import com.turbolessons.paymentservice.dto.SubscriptionItemData;
import com.turbolessons.paymentservice.service.subscription.SubscriptionItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class SubscriptionItemHandlerImpl extends BaseHandler implements SubscriptionItemHandler {

    private final SubscriptionItemService subscriptionItemService;

    public SubscriptionItemHandlerImpl(SubscriptionItemService subscriptionItemService) {
        this.subscriptionItemService = subscriptionItemService;
    }

    @Override
    public Mono<ServerResponse> listAll(ServerRequest r) {
        // Extract subscription ID from query parameters
        String subscriptionId = r.queryParam("subscriptionId")
                .orElseThrow(() -> new IllegalArgumentException("Subscription ID is required"));
        
        return handleList(r,
                request -> subscriptionItemService.listSubscriptionItems(subscriptionId),
                new ParameterizedTypeReference<>() {
                });
    }

    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {
        return handleRetrieve(r,
                request -> subscriptionItemService.retrieveSubscriptionItem(id(request)),
                SubscriptionItem.class);
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest r) {
        return handleCreate(r,
                requestBody -> requestBody.flatMap(this.subscriptionItemService::createSubscriptionItem),
                SubscriptionItemData.class,
                SubscriptionItemData.class);
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest r) {
        String id = id(r);
        return handleUpdate(r,
                (idParam, requestBody) -> requestBody.flatMap(dto -> 
                        subscriptionItemService.updateSubscriptionItem(idParam, dto)),
                id,
                SubscriptionItemData.class);
    }

    @Override
    public Mono<ServerResponse> delete(ServerRequest r) {
        return handleDelete(r,
                request -> subscriptionItemService.deleteSubscriptionItem(id(request)));
    }
}
