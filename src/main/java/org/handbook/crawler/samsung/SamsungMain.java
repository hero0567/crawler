package org.handbook.crawler.samsung;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class SamsungMain {
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
//        controller.addSeed("http://mall.midea.com");
        controller.addSeed("http://www.samsung.com/cn/consumer/mobile-phones/smart-phone");
        controller.addSeed("http://www.samsung.com/cn/consumer/mobile-phones/tablets");
        controller.addSeed("http://www.samsung.com/cn/consumer/mobile-phones/gear");
        controller.addSeed("http://www.samsung.com/cn/consumer/mobile-phones/gear");
        controller.addSeed("http://www.samsung.com/cn/consumer/tv-audio-video/home-audio");
        controller.addSeed("http://www.samsung.com/cn/consumer/computers-office/ultra-mobile-pc");
        controller.addSeed("http://www.samsung.com/cn/consumer/computers-office/monitors");
        controller.addSeed("http://www.samsung.com/cn/consumer/computers-office/monitors");
        controller.addSeed("http://www.samsung.com/cn/consumer/computers-office/camera-lens");
        controller.addSeed("http://www.samsung.com/cn/consumer/home-appliances/refrigerators");
        controller.addSeed("http://www.samsung.com/cn/consumer/home-appliances/washers-dryers");
        controller.addSeed("http://www.samsung.com/cn/consumer/home-appliances/air-conditioners");
        controller.addSeed("http://www.samsung.com/cn/consumer/home-appliances/plasma-air-purifier");
        controller.addSeed("http://www.samsung.com/cn/consumer/home-appliances/rambo");
        controller.addSeed("http://www.samsung.com/cn/consumer/memory/ssd");
        controller.addSeed("http://www.samsung.com/cn/consumer/memory/usb");
        
        
        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(SamsungCrawler.class, numberOfCrawlers);
    }
}