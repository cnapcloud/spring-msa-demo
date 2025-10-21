package com.cop.common.util;

import java.security.cert.X509Certificate;

import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InsecureRestTemplateFactory {

    public static RestTemplate create() {

        HttpComponentsClientHttpRequestFactory requestFactory = null;

        try {
            TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

            SSLContextBuilder sslContextBuilder = SSLContextBuilder.create();
            sslContextBuilder.loadTrustMaterial(acceptingTrustStrategy);

            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build(),
                    new String[] { "TLSv1.2" }, null, NoopHostnameVerifier.INSTANCE);

            HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                    .setSSLSocketFactory(sslSocketFactory).build();

            CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(connectionManager)
                    .setRetryStrategy(new DefaultHttpRequestRetryStrategy(3, TimeValue.ofSeconds(2))).build();

            requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        } catch (Exception e) {
            log.error("fail to create insecure RestTemplate []", e.getMessage());
        }

        return new RestTemplate(requestFactory);
    }
}
