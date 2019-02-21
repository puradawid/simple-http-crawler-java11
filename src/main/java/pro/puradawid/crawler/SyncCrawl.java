package pro.puradawid.crawler;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class SyncCrawl implements Crawling {

    private HttpClient client = HttpClient.newBuilder().build();

    @Override
    public Set<String> allPages(String host, String path) {
        return getUrls(new HashSet<>(), host, path);
    }

    private Set<String> getUrls(Set<String> known, String host, String path) {
        if (known.stream().anyMatch(x -> x.equals(path))) {
            return known;
        }

        try {
            HttpRequest getRequest = HttpRequest.newBuilder(URI.create(host + path)).GET().build();
            HttpResponse<String> response =
                client.send(getRequest, HttpResponse.BodyHandlers.ofString());
            Set<String> enhancedKnown = new HashSet<>(known);
            enhancedKnown.add(path);
            for(String link : new Page(response.body(), host).links()) {
                enhancedKnown.addAll(getUrls(enhancedKnown, host, link));
            }
            return enhancedKnown;

        } catch (IOException | InterruptedException ex) {
            return Collections.emptySet();
        }

    }
}