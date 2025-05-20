package com.turbolessons.videoservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = "spring.config.name=application-test")
@ActiveProfiles("test")
class VideoServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
