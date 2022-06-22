package com.example.demo.client;

import com.example.demo.common.AuthenticatedUserData;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserManagementClient {

    private final WebClient webClient;

    // TODO application.yml
    public UserManagementClient() {
        webClient = WebClient.builder().baseUrl("https://gbmf-user-management-dev.apps.adp.ec1.aws.aztec.cloud.allianz")
            .defaultHeaders(httpHeaders -> {
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
                httpHeaders.setBasicAuth(
                    "hexaliteservice",
                    "hexaliteservicepassword");
            }).build();
    }

    public Mono<AuthenticatedUserData> getUserDetails(final String userName) {
        return webClient.get()
            .uri(
                uriBuilder -> uriBuilder
                    .path("/internal/parties/username/{userName}")
                    .build(userName))
            .retrieve()
            .bodyToMono(AuthenticatedUserData.class);
    }

}
