package com.example.demo.client;

import java.util.List;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class GuacClient {

  private final WebClient webClient;

  // TODO application.yml
  public GuacClient() {
    webClient = WebClient.builder().baseUrl("https://gbmf-guac-dev.apps.adp.ec1.aws.aztec.cloud.allianz")
        .defaultHeaders(httpHeaders -> {
          httpHeaders.setContentType(MediaType.APPLICATION_JSON);
          httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
        }).build();
  }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/oauth/check_token",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Mono<Map<String, Object>> checkToken(final String token) {
      return webClient.post()
          .uri(
              uriBuilder -> uriBuilder
                  .path("/oauth/check_token").queryParam("token", token)
                  .build())
          .retrieve()
          .bodyToMono(new ParameterizedTypeReference<>() {
          });
    }




}
