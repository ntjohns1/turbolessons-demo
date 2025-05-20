package com.turbolessons.messageservice.repository;

import com.turbolessons.messageservice.model.Msg;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface MsgRepo extends ReactiveMongoRepository<Msg, String> {
    Flux<Msg> findBySender(String sender);
    Flux<Msg> findByRecipient(String recipient);
    Flux<Msg> findBySenderAndRecipient(String senderId, String recipientId);


}