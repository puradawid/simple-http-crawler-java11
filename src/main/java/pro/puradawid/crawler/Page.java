package pro.puradawid.crawler;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Page {

    static Pattern hrefPattern = Pattern.compile("href=([\"'])([^ \"]+)\\1");

    private final String content;
    private final String host;

    Page(String content, String host) {
        this.content = content;
        this.host = host;
    }

    List<String> links() {
        LinkedList<String> list = new LinkedList<>();
        Matcher result = hrefPattern.matcher(content);
        while (result.find()) {
            String url = result.group(2);
            if (url.startsWith("/") && !url.startsWith("//")) {
                list.add(url);
            } else if (url.startsWith(host)) {
                list.add(url.replaceFirst(host, ""));
            }
        }
        return list;
    }

}