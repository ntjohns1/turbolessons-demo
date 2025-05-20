package com.turbolessons.paymentservice.controller.paymentmethod;


import com.turbolessons.paymentservice.controller.BaseHandler;
import com.turbolessons.paymentservice.service.paymentmethod.PaymentMethodService;
import com.stripe.model.PaymentMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class PaymentMethodHandlerImpl extends BaseHandler implements PaymentMethodHandler {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodHandlerImpl(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {

        return handleRetrieve(r,
                              request -> paymentMethodService.retrievePaymentMethod(id(request)),
                              PaymentMethod.class);
    }

    @Override
    public Mono<ServerResponse> retrieveByCustomer(ServerRequest r) {

        return handleList(r,
                          request -> paymentMethodService.retrieveCustomerPaymentMethods(id(request)),
                          new ParameterizedTypeReference<>() {
                          });
    }

    @Override
    public Mono<ServerResponse> attach(ServerRequest r) {

        String customerId = r.pathVariable("customerId");

        return paymentMethodService.attachPaymentMethod(id(r),
                                                        customerId)
                .then(ServerResponse.noContent()
                              .build())
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }


    @Override
    public Mono<ServerResponse> detach(ServerRequest r) {

        return paymentMethodService.detachPaymentMethod(id(r))
                .then(ServerResponse.noContent()
                              .build())
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }
}