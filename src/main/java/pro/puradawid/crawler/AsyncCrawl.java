package pro.puradawid.crawler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

class AsyncCrawl implements Crawling {

    private HttpClient client = HttpClient.newBuilder().build();

    @Override
    public Set<String> allPages(String host, String path) {
        return getUrls(new HashSet<>(), host, path).join();
    }

    private CompletableFuture<Set<String>> getUrls(Set<String> known, String host, String path) {
        if (known.contains(path)) {
            return CompletableFuture.completedFuture(known);
        }
        Set<String> withCurrent = new HashSet<>(known);
        withCurrent.add(path);

        HttpRequest getRequest = HttpRequest.newBuilder(URI.create(host + path)).GET().build();
        return client.sendAsync(getRequest, HttpResponse.BodyHandlers.ofString()).thenApply(r -> {
               List<String> links = new Page(r.body(), host).links();
               return links.parallelStream()
                    .map(x -> {
                        Set<String> allBut = new HashSet<>(links);
                        allBut.remove(x);
                        allBut.addAll(withCurrent);
                        return getUrls(allBut, host, x);
                    })
                    .collect(() -> new HashSet<>(),
                        (a, b) -> a.addAll(b.join()),
                        (a, b) -> a.addAll(b));
            }
        );
    }
}