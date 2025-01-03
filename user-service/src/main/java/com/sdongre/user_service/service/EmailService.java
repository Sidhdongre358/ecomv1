package com.sdongre.user_service.service;
import com.sdongre.user_service.constant.AppConstant;
import com.sdongre.user_service.model.dto.request.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EmailService {
    private final WebClient.Builder webClientBuilder;

    @Autowired
    public EmailService(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    public String sendMail(EmailDetails emailDetails) {
        // Use block() to wait until completion and get the result value
        return webClientBuilder.baseUrl(AppConstant.DiscoveredDomainsApi.API_GATEWAY_HOST).build()
                .post()
                .uri("/api/email/sendMail")
                .bodyValue(emailDetails)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}

