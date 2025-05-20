package com.turbolessons.paymentservice.controller.invoice;

import com.turbolessons.paymentservice.dto.InvoiceData;
import com.turbolessons.paymentservice.dto.LineItemData;
import com.turbolessons.paymentservice.service.invoice.InvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.mockJwt;

@WebFluxTest
@Import({InvoiceHandlerImpl.class, InvoiceEndpointConfig.class})
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public class InvoiceHandlerTests {

    @MockBean
    private InvoiceService invoiceService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private InvoiceHandlerImpl invoiceHandler;

    @BeforeEach
    public void setUp() {
        RouterFunction<ServerResponse> routerFunction = new InvoiceEndpointConfig(invoiceHandler).invoiceRoutes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .build();
    }

    @Test
    public void shouldHandleListAllInvoices() {
        List<InvoiceData> invoices = List.of(createInvoiceData("inv_123"),
                                             createInvoiceData("inv_456"));
        when(invoiceService.listAllInvoices()).thenReturn(Mono.just(invoices));

        webTestClient.get()
                .uri("/api/payments/invoice")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(InvoiceData.class)
                .hasSize(2);
    }

    @Test
    public void shouldHandleListAllInvoicesByCustomer() {
        List<InvoiceData> invoices = List.of(createInvoiceData("inv_789"));
        when(invoiceService.listAllInvoiceByCustomer(anyString())).thenReturn(Mono.just(invoices));

        webTestClient.get()
                .uri("/api/payments/invoice/cus_123/customer")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(InvoiceData.class)
                .hasSize(1);
    }

    @Test
    public void shouldHandleListAllInvoicesBySubscription() {
        List<InvoiceData> invoices = List.of(createInvoiceData("inv_789"));
        when(invoiceService.listAllInvoiceBySubscription(anyString())).thenReturn(Mono.just(invoices));

        webTestClient.get()
                .uri("/api/payments/invoice/sub_123/subscription")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(InvoiceData.class)
                .hasSize(1);
    }

    @Test
    public void shouldHandleRetrieveInvoice() {
        InvoiceData invoice = createInvoiceData("inv_123");
        when(invoiceService.retrieveInvoice(anyString())).thenReturn(Mono.just(invoice));

        webTestClient.get()
                .uri("/api/payments/invoice/inv_123")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo("inv_123");
    }

    @Test
    public void shouldHandleRetrieveUpcomingInvoice() {
        InvoiceData invoice = createInvoiceData("inv_987");
        when(invoiceService.retrieveUpcomingInvoice(anyString())).thenReturn(Mono.just(invoice));

        webTestClient.get()
                .uri("/api/payments/invoice/cus_123/upcoming")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo("inv_987");
    }

    @Test
    public void shouldHandleCreateInvoice() {
        InvoiceData invoice = createInvoiceData("inv_321");
        when(invoiceService.createInvoice(any())).thenReturn(Mono.just(invoice));

        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/payments/invoice")
                .body(Mono.just(invoice),
                      InvoiceData.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo("inv_321");
    }

    @Test
    public void shouldHandleUpdateInvoice() {
        InvoiceData invoice = createInvoiceData("inv_654");
        when(invoiceService.updateInvoice(anyString(),
                                          any())).thenReturn(Mono.empty());

        webTestClient.mutateWith(mockJwt())
                .put()
                .uri("/api/payments/invoice/inv_654")
                .body(Mono.just(invoice),
                      InvoiceData.class)
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    public void shouldHandleDeleteDraftInvoice() {
        when(invoiceService.deleteDraftInvoice(anyString())).thenReturn(Mono.empty());

        webTestClient.mutateWith(mockJwt())
                .delete()
                .uri("/api/payments/invoice/inv_123")
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    public void shouldHandleFinalizeInvoice() {
        when(invoiceService.finalizeInvoice(anyString())).thenReturn(Mono.empty());

        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/payments/invoice/sub_456/finalize")
                .exchange()
                .expectStatus()
                .isNoContent();
    }

    @Test
    public void shouldHandlePayInvoice() {
        InvoiceData invoice = createInvoiceData("inv_999");
        when(invoiceService.payInvoice(anyString())).thenReturn(Mono.just(invoice));

        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/payments/invoice/inv_999/pay")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void shouldHandlePayInvoiceError() {
        when(invoiceService.payInvoice(anyString())).thenReturn(Mono.error(new RuntimeException("Payment failed")));

        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/payments/invoice/inv_999/pay")
                .exchange()
                .expectStatus()
                .isEqualTo(500);
    }

    @Test
    public void shouldHandleVoidInvoice() {
        InvoiceData invoice = createInvoiceData("inv_888");
        when(invoiceService.voidInvoice(anyString())).thenReturn(Mono.just(invoice));

        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/payments/invoice/inv_888/void")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void shouldHandleVoidInvoiceError() {
        when(invoiceService.voidInvoice(anyString())).thenReturn(Mono.error(new RuntimeException("Void failed")));

        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/payments/invoice/inv_888/void")
                .exchange()
                .expectStatus()
                .isEqualTo(500);
    }

    @Test
    public void shouldHandleMarkInvoiceUncollectible() {
        InvoiceData invoice = createInvoiceData("inv_777");
        when(invoiceService.markInvoiceUncollectible(anyString())).thenReturn(Mono.just(invoice));

        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/payments/invoice/inv_777/mark_uncollectible")
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    public void shouldHandleMarkInvoiceUncollectibleError() {
        when(invoiceService.markInvoiceUncollectible(anyString())).thenReturn(Mono.error(new RuntimeException("Marking uncollectible failed")));

        webTestClient.mutateWith(mockJwt())
                .post()
                .uri("/api/payments/invoice/inv_777/mark_uncollectible")
                .exchange()
                .expectStatus()
                .isEqualTo(500);
    }

    @Test
    public void shouldHandleRetrieveLineItems() {
        List<LineItemData> lineItems = List.of(createLineItemData("item_123"),
                                               createLineItemData("item_456"));
        when(invoiceService.getLineItems(anyString())).thenReturn(Mono.just(lineItems));

        webTestClient.post()
                .uri("/api/payments/invoice/inv_555/line_items")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(LineItemData.class)
                .hasSize(2);
    }

    @Test
    public void shouldHandleRetrieveLineItemsError() {
        when(invoiceService.getLineItems(anyString())).thenReturn(Mono.error(new RuntimeException("Line items retrieval failed")));

        webTestClient.post()
                .uri("/api/payments/invoice/inv_555/line_items")
                .exchange()
                .expectStatus()
                .isEqualTo(500);
    }

    @Test
    public void shouldHandleRetrieveUpcomingLineItems() {
        List<LineItemData> upcomingLineItems = List.of(createLineItemData("item_789"));
        when(invoiceService.getUpcomingLineItems(anyString())).thenReturn(Mono.just(upcomingLineItems));

        webTestClient.post()
                .uri("/api/payments/invoice/cus_999/upcoming/line_items")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(LineItemData.class)
                .hasSize(1);
    }

    @Test
    public void shouldHandleRetrieveUpcomingLineItemsError() {
        when(invoiceService.getUpcomingLineItems(anyString())).thenReturn(Mono.error(new RuntimeException("Upcoming line items retrieval failed")));

        webTestClient.post()
                .uri("/api/payments/invoice/cus_999/upcoming/line_items")
                .exchange()
                .expectStatus()
                .isEqualTo(500);
    }

    // Helper method to create mock LineItemData
    private LineItemData createLineItemData(String id) {
        return new LineItemData(id,
                                5000,
                                "Test Item",
                                1620000000L,
                                1625000000L,
                                "price_123",
                                "inv_555");
    }

    // Helper method to create mock InvoiceData
    private InvoiceData createInvoiceData(String id) {
        return new InvoiceData(id,
                               "Test Account",
                               "cus_123",
                               10000,
                               5000,
                               5000,
                               1,
                               false,
                               System.currentTimeMillis() / 1000,
                               (System.currentTimeMillis() + 86400000) / 1000,
                               System.currentTimeMillis() / 1000,
                               0,
                               (System.currentTimeMillis() + 43200000) / 1000,
                               false);
    }
}
