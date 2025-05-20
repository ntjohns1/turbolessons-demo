package com.turbolessons.messageservice.service;

import com.turbolessons.messageservice.model.Msg;
import com.turbolessons.messageservice.repository.MsgRepo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Predicate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;

@Log4j2
public class MsgServiceTest {

    private MsgService service;
    private MsgRepo repository;

    private ApplicationEventPublisher publisher;

    @BeforeEach
    public void setUp() {
        repository = mock(MsgRepo.class);
        publisher = mock(ApplicationEventPublisher.class);
        service = new MsgService(publisher, repository);
        setUpMsgRepositoryMock();
    }

    @Test
    public void testGetAll() {
        String testSender = "bholiday";
        String testRecipient = "tmiller";
        String alternateUser = "dhagel";

        Flux<Msg> composite = service.all();

        StepVerifier.create(composite)
                .expectNextMatches(msg -> testSender.equalsIgnoreCase(msg.getSender())
                        && testRecipient.equalsIgnoreCase(msg.getRecipient()))
                .expectNextMatches(msg -> testSender.equalsIgnoreCase(msg.getSender())
                        && testRecipient.equalsIgnoreCase(msg.getRecipient()))
                .expectNextMatches(msg -> testSender.equalsIgnoreCase(msg.getSender())
                        && testRecipient.equalsIgnoreCase(msg.getRecipient()))
                .expectNextMatches(msg -> testSender.equalsIgnoreCase(msg.getSender())
                        && alternateUser.equalsIgnoreCase(msg.getRecipient()))
                .expectNextMatches(msg -> alternateUser.equalsIgnoreCase(msg.getSender())
                        && testRecipient.equalsIgnoreCase(msg.getRecipient()))
                .verifyComplete();
    }
    @Test
    public void testGetById() {

        Mono<Msg> composite = (Mono<Msg>) this.service.get("testId");
        StepVerifier.create(composite)
                .expectNextMatches(msg -> "dhagel".equalsIgnoreCase(msg.getSender())
                        && "tmiller".equalsIgnoreCase(msg.getRecipient()))
                .verifyComplete();
    }


    @Test
    public void testGetBySender() {

        Flux<Msg> composite = service.getBySender("bholiday");

        Predicate<Msg> match = msg -> msg.getSender().equals("bholiday");

        StepVerifier.create(composite)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .verifyComplete();
    }



    @Test
    public void testGetByRecipient() {

        // Retrieve messages by the test sender
        Flux<Msg> composite = service.getByRecipient("tmiller");

        Predicate<Msg> match = msg -> msg.getRecipient().equals("tmiller");

        StepVerifier.create(composite)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .verifyComplete();
    }

    @Test
    public void testGetBySenderAndRecipient() {

        // Retrieve messages by the test sender and recipient
        Flux<Msg> composite = service.getBySenderAndRecipient("bholiday", "tmiller");

        Predicate<Msg> match = msg -> msg.getSender().equals("bholiday") &&
                msg.getRecipient().equals("tmiller");

        StepVerifier.create(composite)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .expectNextMatches(match)
                .verifyComplete();
    }

    @Test
    public void save() {
        Mono<Msg> msgMono = this.service.create("gharold","hthompson", "Hello");
        StepVerifier.create(msgMono)
                .expectNextMatches(saved -> StringUtils.hasText(saved.getId()))
                .verifyComplete();
    }

    @Test
    public void delete() {
        Mono<Msg> deleted = this.service.create("dhagel", "tmiller", "test")
                .flatMap(saved -> this.service.delete(saved.getId()));
        StepVerifier
                .create(deleted)
                .expectNextMatches(msg -> msg.getSender().equalsIgnoreCase("dhagel")
                        && msg.getRecipient().equalsIgnoreCase("tmiller"))
                .verifyComplete();
    }

    private void setUpMsgRepositoryMock() {
        final String time = new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date());

        String testSender = "bholiday";
        String testRecipient = "tmiller";
        String alternateUser = "dhagel";
        Msg msg1 = new Msg(UUID.randomUUID().toString(),testSender, testRecipient, "test", time);
        Msg msg2 = new Msg(UUID.randomUUID().toString(),testSender, testRecipient, "test", time);
        Msg msg3 = new Msg(UUID.randomUUID().toString(),testSender, testRecipient, "test", time);
        Msg msg4 = new Msg(UUID.randomUUID().toString(),testSender, alternateUser, "test", time);
        Msg msg5 = new Msg(UUID.randomUUID().toString(),alternateUser, testRecipient, "test", time);

        Flux<Msg> msgFlux = Flux.just(msg1,msg2,msg3,msg4,msg5);
        Flux<Msg> msgBySenderFlux = Flux.just(msg1, msg2, msg3, msg4);  // messages sent by 'testSender'
        Flux<Msg> msgByReceiverFlux = Flux.just(msg1, msg2, msg3, msg5);  // messages sent by 'testSender'
        Flux<Msg> msgBySenderReceiverFlux = Flux.just(msg1, msg2, msg3);  // messages sent by 'testSender'
        Mockito.when(this.repository.findAll()).thenReturn(msgFlux);
        Mockito.when(this.repository.findById(anyString())).thenReturn(Mono.just(msg5));
        Mockito.when(this.repository.findBySender(testSender)).thenReturn(msgBySenderFlux);
        Mockito.when(this.repository.findByRecipient(testRecipient)).thenReturn(msgByReceiverFlux);
        Mockito.when(this.repository.findBySenderAndRecipient(testSender,testRecipient)).thenReturn(msgBySenderReceiverFlux);
        Mockito.when(this.repository.save(any())).thenReturn(Mono.just(msg5));
        Mockito.when(this.repository.deleteById(anyString())).thenReturn(Mono.empty());

        Mockito.doNothing().when(publisher).publishEvent(any());
    }

}
