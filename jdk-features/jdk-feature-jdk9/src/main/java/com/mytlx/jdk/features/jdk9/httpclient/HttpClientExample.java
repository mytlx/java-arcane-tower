package com.mytlx.jdk.features.jdk9.httpclient;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * JDK 9 引入了 java.net.http.HttpClient，支持异步请求
 *
 * @author TLX
 * @version 1.0.0
 * @since 2025-03-08 13:06
 */
public class HttpClientExample {

    public static void main(String[] args) throws Exception {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://www.baidu.com"))
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("response.body() = " + response.body());
        }
    }

}
