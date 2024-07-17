package com.app.Config;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class RateLimitFilter implements WebFilter {

    private static final Logger log = LogManager.getLogger(RateLimitFilter.class);

    private final Supplier<BucketConfiguration> bucketConfiguration;
    private final ProxyManager<String> proxyManager;

    @Autowired
    public RateLimitFilter(Supplier<BucketConfiguration> bucketConfiguration, ProxyManager<String> proxyManager) {
        this.bucketConfiguration = bucketConfiguration;
        this.proxyManager = proxyManager;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String key = Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress();
        Bucket bucket = proxyManager.builder().build(key, bucketConfiguration);

        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            log.info(">>>>>>>>remainingTokens: {}", probe.getRemainingTokens());
            return chain.filter(exchange);
        } else {
            log.info("TOO_MANY_REQUESTS");
            exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
            exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
            exchange.getResponse().getHeaders().add("X-Rate-Limit-Retry-After-Seconds",
                    String.valueOf(TimeUnit.NANOSECONDS.toSeconds(probe.getNanosToWaitForRefill())));
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap("Too many requests".getBytes());
            return exchange.getResponse().writeWith(Mono.just(buffer));
        }
    }
}
