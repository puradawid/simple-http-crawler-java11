package pro.puradawid.crawler;

import java.util.Set;

public interface Crawling {

    Set<String> allPages(String host, String path);

}
