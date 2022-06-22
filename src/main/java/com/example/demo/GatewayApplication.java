package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableRedisRepositories
@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayApplication.class, args);
	}

	//curl http://localhost:8080/config/topics/JOB_QUEUE -H "topic: JOB_QUEUE"
//	@Bean
//	public RouteLocator myRoutes(RouteLocatorBuilder builder, CustomerRoutePredicateFactory factory) {
//		return builder.routes()
//				.route(r -> r
//						.path("/get")
//						.filters(f -> f.addRequestHeader("Hello", "World"))
//						.uri("http://httpbin.org:80"))
////				.route(r -> r.path("/config/topics/JOB_QUEUE")
////						.uri("https://hg-candidate-service-dev.apps.adp.ec1.aws.aztec.cloud.allianz"))
////				.route(r -> r.path("/config/topics/SHAPE_CODE")
////						.uri("https://gbmf-fleet-management-dev.apps.adp.ec1.aws.aztec.cloud.allianz"))
//				.route(r -> r.path("/config/**")
//						.and().header(TOPIC_HEADER_NAME, Topics.JOB_QUEUE.getCode())
//						.uri(Topics.JOB_QUEUE.getServiceName()))
//				.route(r -> r.path("/config/**")
//						.and().header(TOPIC_HEADER_NAME, Topics.SHAPE_CODE.getCode())
//						.uri(Topics.SHAPE_CODE.getServiceName()))
////				.route("predicate_route", r -> r.predicate(exchange -> {
////							System.out.println("predicate");
////							System.out.println(exchange.getRequest().getPath());
////							return true;
////						})
////						.filters(f -> f.changeRequestUri(exchange -> {
////							System.out.println("filter");
////
////							System.out.println(exchange.getRequest().getPath());
////
//////							Map<String, String> uriVariables = ServerWebExchangeUtils.getUriTemplateVariables(exchange);
//////							System.out.println(uriVariables);
//////
//////							String topicName = uriVariables.get("topicName");
//////							System.out.println(topicName);
//////
//////							System.out.println(exchange.getAttributes());
//////							System.out.println(
//////									(String) exchange.getAttribute(ServerWebExchangeUtils.URI_TEMPLATE_VARIABLES_ATTRIBUTE));
////
////							return Optional.of(URI.create("http://httpbin.org:80"));
////						}))
////						.uri("http://httpbin.org:80")
////				)
//				.build();
//	}

//		return builder.routes()
//				.route(r -> r.path("/config/**")
//						.and().header(TOPIC_HEADER_NAME, Topics.JOB_QUEUE.getCode())
//						.uri(Topics.JOB_QUEUE.getServiceName()))
//				.route(r -> r.path("/config/**")
//						.and().header(TOPIC_HEADER_NAME, Topics.SHAPE_CODE.getCode())
//						.uri(Topics.SHAPE_CODE.getServiceName()))
//				.build();

//	@Bean
//	public CustomerRoutePredicateFactory customerRoutePredicateFactory() {
//		return new CustomerRoutePredicateFactory(Config.class);
//	}
}
