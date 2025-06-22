package com.turbolessons.paymentservice.controller.product;

import com.turbolessons.paymentservice.dto.CustomerData;
import com.turbolessons.paymentservice.dto.ProductData;
import com.turbolessons.paymentservice.service.product.ProductService;
import com.stripe.model.Product;
import com.stripe.model.StripeCollection;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Log4j2
@WebFluxTest
@Import(ProductHandlerImpl.class)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.yml", properties = {"spring.config.name=application-test"})
public class ProductHandlerTests {

    @MockBean
    private ProductService productService;

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ProductHandler productHandler;

    @BeforeEach
    public void setUp() {
        RouterFunction<ServerResponse> routerFunction = new ProductEndpointConfig(productHandler).productRoutes();
        this.webTestClient = WebTestClient.bindToRouterFunction(routerFunction)
                .build();
    }

    @Test
    void shouldHandleListAllProducts() {

        StripeCollection<Product> mockProducts = createMockStripeCollection();
        when(productService.listAllProducts()).thenReturn(Mono.just(mockProducts));
        webTestClient.get()
                .uri("/api/payments/product")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.object")
                .isEmpty()
                .jsonPath("$.data")
                .isNotEmpty();
    }

    @Test
    void shouldHandleRetrieveProduct() {

        Product product = createMockProduct("prod_123",
                                            "Test Product",
                                            "Test Description");

        when(productService.retrieveProduct(anyString())).thenReturn(Mono.just(product));
        webTestClient.get()
                .uri("/api/payments/product/prod_123")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo(product.getId())
                .jsonPath("$.name")
                .isEqualTo(product.getName())
                .jsonPath("$.description")
                .isEqualTo(product.getDescription());
    }

    @Test
    void shouldHandleCreateProduct() {
        Product product = createMockProduct("prod_123",
                                            "Test Product",
                                            "Test Description");
        ProductData dto = createMockProductDto("prod_123",
                                               "Test Product",
                                               "Test Description");
        when(productService.createProduct(dto)).thenReturn(Mono.just(product));

        webTestClient
                .post()
                .uri("/api/payments/product")
                .body(Mono.just(dto),
                      CustomerData.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody()
                .jsonPath("$.id")
                .isEqualTo(product.getId())
                .jsonPath("$.name")
                .isEqualTo(product.getName())
                .jsonPath("$.description")
                .isEqualTo(product.getDescription());

    }

    @Test
    void shouldHandleUpdateProduct() {
        ProductData dto = createMockProductDto("prod_456",
                                               "Updated Test Product",
                                               "Updated Test Description");
        when(productService.updateProduct(anyString(),
                                          any(ProductData.class))).thenReturn(Mono.empty());

        webTestClient
                .put()
                .uri("/api/payments/product/prod_123")
                .body(Mono.just(dto),
                      CustomerData.class)
                .exchange()
                .expectStatus()
                .isNoContent();

    }

    @Test
    void shouldHandleDeleteProduct() {
        when(productService.deleteProduct(anyString())).thenReturn(Mono.empty());

        webTestClient
                .delete()
                .uri("/api/payments/product/prod_123")
                .exchange()
                .expectStatus()
                .isNoContent();


    }

    private StripeCollection<Product> createMockStripeCollection() {

        StripeCollection<Product> products = new StripeCollection<>();
        Product product1 = createMockProduct("prod_123",
                                             "product1",
                                             "Test Product 1");
        Product product2 = createMockProduct("prod_456",
                                             "product2",
                                             "Test Product 2");

        List<Product> productList = Arrays.asList(product1,
                                                  product2);
        products.setData(productList);
        return products;
    }

    private Product createMockProduct(String id, String name, String description) {
        Product product = Mockito.mock(Product.class);
        when(product.getId()).thenReturn(id);
        when(product.getName()).thenReturn(name);
        when(product.getDescription()).thenReturn(description);
        return product;
    }

    private ProductData createMockProductDto(String id, String name, String description) {
        return new ProductData(id,
                               name,
                               description);
    }
}
