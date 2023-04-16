package com.udacity.pricing;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.udacity.pricing.domain.price.Price;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class PricingServiceApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testPricingService() {
		ResponseEntity<Map> response =
				restTemplate.getForEntity("http://localhost:" + port + "/prices/2/", Map.class);
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(27500.0, response.getBody().get("price"));
	}

}
