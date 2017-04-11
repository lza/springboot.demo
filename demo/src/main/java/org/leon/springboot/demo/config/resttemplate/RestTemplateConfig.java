package org.leon.springboot.demo.config.resttemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.Consts;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.CodingErrorAction;
import java.util.List;

/**
 * Created by leon on 2016/11/15.
 */
@Configuration
public class RestTemplateConfig {
    @Value("${restTemplate.connection.max-total-connections}")
    private int maxTotalConnections;
    @Value("${restTemplate.connection.max-connections-per-route}")
    private int maxConnectionsPerRoute;

    @Value("${restTemplate.request.connect-timeout}")
    private int connectTimeout;
    @Value("${restTemplate.request.connection-request-timeout}")
    private int connectionRequestTimeout;
    @Value("${restTemplate.request.socket-timeout}")
    private int socketTimeout;

    @Value("${restTemplate.socket.tcp-no-delay}")
    private boolean tcpNoDelay;

    @Value("${restTemplate.message.max-header-count}")
    private int maxHeaderCount;
    @Value("${restTemplate.message.max-line-length}")
    private int maxLineLength;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
        List<HttpMessageConverter<?>> converters = restTemplate
                .getMessageConverters();

        for (HttpMessageConverter<?> converter : converters) {
            if (converter instanceof MappingJackson2HttpMessageConverter) {
                MappingJackson2HttpMessageConverter jsonConverter = (MappingJackson2HttpMessageConverter) converter;
                jsonConverter.setObjectMapper(objectMapper);
            }
        }

        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    @Bean
    public HttpClient httpClient() {
        HttpClient httpClient = HttpClientBuilder.create()
                .setConnectionManager(connectionManager())
                .setDefaultRequestConfig(requestConfig())
                .build();
        return httpClient;
    }

    @Bean
    public HttpClientConnectionManager connectionManager(){
        /*SSLContext sslContext = SSLContexts.custom().useTLS().build();
        sslContext.init(null,
                new TrustManager[] { new X509TrustManager() {

                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(
                            X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(
                            X509Certificate[] certs, String authType) {
                    }
                }}, null);
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sslContext))
                .build();

        PoolingHttpClientConnectionManager connectionManager
                = new PoolingHttpClientConnectionManager(socketFactoryRegistry);*/

        PoolingHttpClientConnectionManager connectionManager
                = new PoolingHttpClientConnectionManager();

        connectionManager.setMaxTotal(maxTotalConnections);
        connectionManager.setDefaultMaxPerRoute(maxConnectionsPerRoute);
        connectionManager.setDefaultSocketConfig(socketConfig());
        ConnectionConfig connectionConfig = ConnectionConfig.custom()
                .setMalformedInputAction(CodingErrorAction.IGNORE)
                .setUnmappableInputAction(CodingErrorAction.IGNORE)
                .setCharset(Consts.UTF_8)
                .setMessageConstraints(messageConstraints())
                .build();
        connectionManager.setDefaultConnectionConfig(connectionConfig);
        /*connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost(
                "facebook.com")), 20);
        connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost(
                "twitter.com")), 20);
        connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost(
                "linkedin.com")), 20);
        connectionManager.setMaxPerRoute(new HttpRoute(new HttpHost(
                "viadeo.com")), 20);*/

        return connectionManager;
    }

    @Bean
    public SocketConfig socketConfig(){
        return SocketConfig.custom()
                .setTcpNoDelay(tcpNoDelay)
                .build();
    }

    @Bean
    public MessageConstraints messageConstraints(){
        return MessageConstraints.custom()
                .setMaxHeaderCount(maxHeaderCount)
                .setMaxLineLength(maxLineLength)
                .build();
    }

    @Bean
    public RequestConfig requestConfig(){
        return RequestConfig.custom().setConnectTimeout(connectTimeout)
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
    }

    public void setMaxTotalConnections(int maxTotalConnections) {
        maxTotalConnections = maxTotalConnections;
    }

    public void setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public void setTcpNoDelay(boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public void setMaxHeaderCount(int maxHeaderCount) {
        this.maxHeaderCount = maxHeaderCount;
    }

    public void setMaxLineLength(int maxLineLength) {
        this.maxLineLength = maxLineLength;
    }
}
