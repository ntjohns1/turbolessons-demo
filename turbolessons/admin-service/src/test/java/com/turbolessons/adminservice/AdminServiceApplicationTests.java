package com.turbolessons.adminservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = "spring.config.name=application-test")
@ActiveProfiles("test")
class AdminServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
