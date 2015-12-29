package org.handbook.crawler;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class ShuoMingShuKuMain {
    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "./crawler";
        int numberOfCrawlers = 20;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);

        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);

        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed("http://www.shuomingshuku.com/");
        controller.addSeed("http://www.shuomingshuku.com/s/1-0-0-1-0-0.html");
        controller.addSeed("http://www.shuomingshuku.com/s/2-0-0-1-0-0.html");
        controller.addSeed("http://www.shuomingshuku.com/s/3-0-0-1-0-0.html");
        controller.addSeed("http://www.shuomingshuku.com/s/4-0-0-1-0-0.html");
        controller.addSeed("http://www.shuomingshuku.com/s/5-0-0-1-0-0.html");
        controller.addSeed("http://www.shuomingshuku.com/s/6-0-0-1-0-0.html");
        controller.addSeed("http://www.shuomingshuku.com/s/7-0-0-1-0-0.html");
        controller.addSeed("http://www.shuomingshuku.com/s/8-0-0-1-0-0.html");
        
        controller.addSeed("http://www.manuallib.com//");
        controller.addSeed("http://www.manuallib.com//s/1-0-0-1-0-0.html");
        controller.addSeed("http://www.manuallib.com//s/2-0-0-1-0-0.html");
        controller.addSeed("http://www.manuallib.com//s/3-0-0-1-0-0.html");
        controller.addSeed("http://www.manuallib.com//s/4-0-0-1-0-0.html");
        controller.addSeed("http://www.manuallib.com//s/5-0-0-1-0-0.html");
        controller.addSeed("http://www.manuallib.com//s/6-0-0-1-0-0.html");
        controller.addSeed("http://www.manuallib.com//s/7-0-0-1-0-0.html");
        controller.addSeed("http://www.manuallib.com//s/8-0-0-1-0-0.html");
        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(ShuoMingShuKuCrawler.class, numberOfCrawlers);
    }
}