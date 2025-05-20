package com.turbolessons.paymentservice.controller.paymentintent;

import com.turbolessons.paymentservice.controller.BaseHandler;
import com.turbolessons.paymentservice.dto.PaymentIntentData;
import com.turbolessons.paymentservice.service.paymentintent.PaymentIntentService;
import com.stripe.model.PaymentIntent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


@Slf4j
@Service
public class PaymentIntentHandlerImpl extends BaseHandler implements PaymentIntentHandler {

    private final PaymentIntentService paymentIntentService;

    public PaymentIntentHandlerImpl(PaymentIntentService paymentIntentService) {
        this.paymentIntentService = paymentIntentService;
    }

    @Override
    public Mono<ServerResponse> listAll(ServerRequest r) {
        return handleList(r,
                             request -> this.paymentIntentService.listAllPaymentIntents(),
                          new ParameterizedTypeReference<>() {
                             });
    }

    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {

        return handleRetrieve(r,
                              request -> this.paymentIntentService.retrievePaymentIntent(id(request)),
                              PaymentIntent.class);
    }

    @Override
    public Mono<ServerResponse> searchByCustomer(ServerRequest r) {
        return handleSearch(r,
                            request -> this.paymentIntentService.searchPaymentIntentByCustomer(id(request)),
                            new ParameterizedTypeReference<>() {
                            });
    }

    @Override
    public Mono<ServerResponse> create(ServerRequest r) {
        return handleCreate(r,
                            requestBody -> requestBody.flatMap(this.paymentIntentService::createPaymentIntent),
                            PaymentIntentData.class,
                            PaymentIntent.class);
    }

    @Override
    public Mono<ServerResponse> update(ServerRequest r) {
        String id = id(r);
        return handleUpdate(r,
                            (idParam, requestBody) -> requestBody.flatMap(dto -> this.paymentIntentService.updatePaymentIntent(idParam,
                                                                                                                          dto)),
                            id,
                            PaymentIntentData.class);
    }

    @Override
    public Mono<ServerResponse> capture(ServerRequest r) {
        return handleCapture(r,
                             request -> this.paymentIntentService.capturePaymentIntent(id(request)),
                             PaymentIntent.class);
    }

    @Override
    public Mono<ServerResponse> cancel(ServerRequest r) {
        return handleDelete(r,
                            request -> this.paymentIntentService.cancelPaymentIntent(id(request)));
    }
}
