package com.turbolessons.messageservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = "spring.config.name=application-test")
@ActiveProfiles("test")
class MessageServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
