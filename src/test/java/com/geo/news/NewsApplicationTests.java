package com.geo.news;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"gnews.key=test-gnews-key",
		"unsplash.key=test-unsplash-key",
		"google.api.key="
})
class NewsApplicationTests {
	@Test
	void contextLoads() {
		// Verifies Spring application context bootstraps with test properties.
	}

}
