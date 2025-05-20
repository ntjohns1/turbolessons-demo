package com.turbolessons.paymentservice;

import com.turbolessons.paymentservice.config.TestStripeConfig;
import com.turbolessons.paymentservice.util.MockEventServiceClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@Import({TestStripeConfig.class, MockEventServiceClientConfig.class})
class PaymentServiceApplicationTests {

    @Test
    void contextLoads() {
    }
}
