package com.miraclestudio.mcpreposerver.config.github;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    @Value("${github.api.url:https://api.github.com}")
    private String githubApiUrl;

    @Value("${github.api.token:}")
    private String githubApiToken;

    /**
     * GitHub API 호출을 위한 WebClient 빈 등록
     */
    @Bean
    public WebClient githubWebClient() {
        // 메모리 제한 설정 (16MB)
        final int size = 16 * 1024 * 1024;
        final ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(size))
                .build();

        // HTTP 클라이언트 설정 (타임아웃, 연결 설정 등)
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000) // 연결 타임아웃 10초
                .responseTimeout(Duration.ofSeconds(10)) // 응답 타임아웃 10초
                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(10, TimeUnit.SECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(10, TimeUnit.SECONDS)));

        // WebClient 생성
        WebClient.Builder builder = WebClient.builder()
                .baseUrl(githubApiUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .exchangeStrategies(exchangeStrategies)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.ACCEPT, "application/vnd.github.v3+json")
                .filter(logRequest());

        // GitHub API 토큰이 설정되어 있으면 인증 헤더 추가
        if (githubApiToken != null && !githubApiToken.isEmpty()) {
            builder.defaultHeader(HttpHeaders.AUTHORIZATION, "token " + githubApiToken);
        }

        return builder.build();
    }

    /**
     * 요청 로깅 필터
     */
    private ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            // 로그 레벨이 디버그일 때만 로깅
            if (org.slf4j.LoggerFactory.getLogger(WebClientConfig.class).isDebugEnabled()) {
                StringBuilder sb = new StringBuilder("Request: \n");
                sb.append("Method: ").append(clientRequest.method()).append("\n");
                sb.append("URL: ").append(clientRequest.url()).append("\n");
                clientRequest.headers().forEach((name, values) -> {
                    if (!name.equalsIgnoreCase(HttpHeaders.AUTHORIZATION)) {
                        values.forEach(value -> sb.append(name).append(": ").append(value).append("\n"));
                    } else {
                        sb.append(name).append(": ").append("**hidden**").append("\n");
                    }
                });
                org.slf4j.LoggerFactory.getLogger(WebClientConfig.class).debug(sb.toString());
            }
            return Mono.just(clientRequest);
        });
    }
}