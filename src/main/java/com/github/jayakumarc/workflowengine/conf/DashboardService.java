package com.github.jayakumarc.workflowengine.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DashboardService {

	@Value("${dashboardServiceHost}")
	private String host;

	@Value("${dashboardServicePort}")
	private String port;

	public String baseUrl() {
		return "http://" + host + ":" + port;
	}

	@Bean
	public RestTemplate createRestTemplate() {
		return new RestTemplate();
	}

}
