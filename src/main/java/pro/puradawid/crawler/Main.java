package pro.puradawid.crawler;

import java.util.Set;

class Main {

    public static void main(String[] args) throws Exception {
        String host = "https://kociamadka.pl";
        Set<String> knownUrls;
        long time = System.currentTimeMillis();
        if (args.length > 1 && "sync".equals(args[0])) {
            knownUrls = new SyncCrawl().allPages(host, "/");
        } else {
            knownUrls = new AsyncCrawl().allPages(host, "/");
        }
        for (String path : knownUrls) {
            System.out.println(host + path);
        }
        System.out.println("It took " + ((System.currentTimeMillis() - time) / 1000) + " seconds.");
    }


}