package com.turbolessons.paymentservice.controller.setupintent;

import com.turbolessons.paymentservice.controller.BaseHandler;
import com.turbolessons.paymentservice.dto.SetupIntentData;
import com.turbolessons.paymentservice.service.setupintent.SetupIntentService;
import com.stripe.model.SetupIntent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class SetupIntentHandlerImpl extends BaseHandler implements SetupIntentHandler {

    private final SetupIntentService setupIntentService;

    public SetupIntentHandlerImpl(SetupIntentService setupIntentService) {
        this.setupIntentService = setupIntentService;
    }

    @Override
    public Mono<ServerResponse> listAll(ServerRequest r) {

        return handleList(r,
                          request -> this.setupIntentService.listSetupIntents(),
                          new ParameterizedTypeReference<>() {
                          });
    }

    //    Retrieve a SetupIntent
    @Override
    public Mono<ServerResponse> retrieve(ServerRequest r) {
        return handleRetrieve(r,
                              request -> this.setupIntentService.retrieveSetupIntent(id(request)),
                              SetupIntent.class);
    }

    //    Create a SetupIntent
    @Override
    public Mono<ServerResponse> create(ServerRequest r) {
        return handleCreate(r,
                            requestBody -> requestBody.flatMap(this.setupIntentService::createSetupIntent),
                            SetupIntentData.class,
                            SetupIntent.class);
    }

    //    Confirm a SetupIntent
    @Override
    public Mono<ServerResponse> confirm(ServerRequest r) {
        return setupIntentService.confirmSetupIntent(id(r))
                .then(ServerResponse.noContent()
                              .build())
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build());
    }

    //    Update a SetupIntent
    @Override
    public Mono<ServerResponse> update(ServerRequest r) {
        String id = id(r);
        return handleUpdate(r,
                            ((idParam, requestBody) -> requestBody.flatMap(dto -> this.setupIntentService.updateSetupIntent(idParam,
                                                                                                                            dto))),
                            id,
                            SetupIntentData.class);
    }

    //    Cancel a SetupIntent
    @Override
    public Mono<ServerResponse> cancel(ServerRequest r) {

        return handleDelete(r,
                            request -> this.setupIntentService.cancelSetupIntent(id(r)));
    }
}
