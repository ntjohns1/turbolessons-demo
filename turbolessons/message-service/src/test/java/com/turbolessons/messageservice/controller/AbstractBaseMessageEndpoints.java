package com.turbolessons.messageservice.controller;

import com.turbolessons.messageservice.model.Msg;
import com.turbolessons.messageservice.repository.MsgRepo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@Log4j2
@WebFluxTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(
        locations = "classpath:application-test.yml",
        properties = { "spring.config.name=application-test" }
)
public abstract class AbstractBaseMessageEndpoints {

    private final WebTestClient client;
    @MockBean
    private MsgRepo repository;

    public AbstractBaseMessageEndpoints(WebTestClient client) {
        this.client = client;
    }

    final String time = new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date());

    @Test
    @WithMockUser
    public void testGetAll() {
        log.info("running  " + this.getClass().getName());

        Mockito.when(this.repository.findAll())
                .thenReturn(Flux.just(new Msg("1", "A", "A", "A", time),
                        new Msg("2", "B", "B", "B", time)));

        this.client.mutateWith(csrf()).get()
                .uri("/api/messages").accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.[0].id")
                .isEqualTo("1")
                .jsonPath("$.[0].sender").isEqualTo("A")
                .jsonPath("$.[0].msg").isEqualTo("A")
                .jsonPath("$.[0].timestamp").isEqualTo(time);
    }

    @Test
    @WithMockUser
    public void testGetById() {
        Msg data = new Msg(
                UUID.randomUUID().toString(),
                "tjames",
                "tjefferson",
                "hello",
                time
        );

        Mockito.when(this.repository.findById(data.getId())).thenReturn(Mono.just(data));

        this.client.mutateWith(mockJwt()).get()
                .uri("/api/messages/" + data.getId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(data.getId())
                .jsonPath("$.sender").isEqualTo(data.getSender())
                .jsonPath("$.msg").isEqualTo(data.getMsg())
                .jsonPath("$.timestamp").isEqualTo(data.getTimestamp());
    }

    @Test
    @WithMockUser
    public void testGetBySender() {
        String sender = "rhenderson";

        Mockito.when(this.repository
                .findBySender(sender))
                .thenReturn(Flux.just(new Msg(UUID.randomUUID().toString(), sender, "jhoward", "hello", time),
                        new Msg(UUID.randomUUID().toString(), sender, "djacobs", "hello", time)));

        this.client.mutateWith(mockJwt()).get()
                .uri("/api/messages/sender/" + sender)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.[0].sender").isEqualTo(sender)
                .jsonPath("$.[0].msg").isEqualTo("hello")
                .jsonPath("$.[0].timestamp").isEqualTo(time)
                .jsonPath("$.[1].sender").isEqualTo(sender)
                .jsonPath("$.[1].msg").isEqualTo("hello")
                .jsonPath("$.[1].timestamp").isEqualTo(time);
    }

    @Test
    @WithMockUser
    public void testGetByRecipient() {
        String recipient = "swilliams";

        Mockito.when(this.repository.findByRecipient(recipient))
                .thenReturn(Flux.just(new Msg(UUID.randomUUID().toString(),"cstevens", recipient, "hello", time),
                        new Msg(UUID.randomUUID().toString(), "gmorgan", recipient, "hello", time)));

        this.client.mutateWith(mockJwt()).get()
                .uri("/api/messages/recipient/" + recipient)
                .accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.[0].recipient").isEqualTo(recipient)
                .jsonPath("$.[0].msg").isEqualTo("hello")
                .jsonPath("$.[0].timestamp").isEqualTo(time)
                .jsonPath("$.[1].recipient").isEqualTo(recipient)
                .jsonPath("$.[1].msg").isEqualTo("hello")
                .jsonPath("$.[1].timestamp").isEqualTo(time);
    }

    @Test
    @WithMockUser
    public void testGetBySenderAndRecipient() {
        String recipient = "cjacobs";
        String sender = "fmartin";

        Mockito.when(this.repository.findBySenderAndRecipient(sender, recipient))
                .thenReturn(Flux.just(new Msg(UUID.randomUUID().toString(), sender, recipient, "hello", time),
                        new Msg(UUID.randomUUID().toString(), sender, recipient, "hello", time)));

        this.client.mutateWith(mockJwt()).get()
                .uri("/api/messages/" + sender + "/to/" + recipient)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader()
                .contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.[0].sender").isEqualTo(sender)
                .jsonPath("$.[0].recipient").isEqualTo(recipient)
                .jsonPath("$.[0].msg").isEqualTo("hello")
                .jsonPath("$.[0].timestamp").isEqualTo(time)
                .jsonPath("$.[0].sender").isEqualTo(sender)
                .jsonPath("$.[1].recipient").isEqualTo(recipient)
                .jsonPath("$.[1].msg").isEqualTo("hello")
                .jsonPath("$.[1].timestamp").isEqualTo(time);
    }

    @Test
    @WithMockUser
    public void testSendAll() {
        Msg data = new Msg("1", "njenkins","ALL", "hello", time);
        Mockito.when(this.repository.save(Mockito.any(Msg.class))).thenReturn(Mono.just(data));
        MediaType jsonUtf8 = MediaType.APPLICATION_JSON;
        this.client.mutateWith(csrf()).post()
                .uri("/api/messages")
                .contentType(jsonUtf8).body(Mono.just(data), Msg.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader()
                .contentType(jsonUtf8);
    }

    @Test
    @WithMockUser
    public void testSend() {
        String sender = UUID.randomUUID().toString();
        String recipient = UUID.randomUUID().toString();
        Msg data = new Msg(UUID.randomUUID().toString(), sender, recipient, "hello", time);
        Mockito.when(this.repository.save(Mockito.any(Msg.class))).thenReturn(Mono.just(data));
        MediaType jsonUtf8 = MediaType.APPLICATION_JSON;
        this.client.mutateWith(csrf()).post()
                .uri("/api/messages/" + recipient)
                .contentType(jsonUtf8)
                .body(Mono.just(data), Msg.class)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader()
                .contentType(jsonUtf8);
    }

    @Test
    @WithMockUser
    public void delete() {
        Msg data = new Msg(UUID.randomUUID().toString(), "sdavis01", "jhammond", "hello", time);
        Mockito.when(this.repository.findById(data.getId())).thenReturn(Mono.just(data));
        Mockito.when(this.repository.deleteById(data.getId())).thenReturn(Mono.empty());
        this.client.mutateWith(csrf()).delete()
                .uri("/api/messages/" + data.getId())
                .exchange()
                .expectStatus().isOk();
    }

}
