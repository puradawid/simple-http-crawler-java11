package pro.puradawid.crawler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

class AsyncMain {

    public static void main(String[] args) {
        HttpClient httpClient = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://puradawid.pro")).GET().build();
        CompletableFuture<HttpResponse<Stream<String>>>
            result = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofLines());
        HttpResponse<Stream<String>> response = result.join();
        System.out.println(response.body().findFirst().get());
    }

}