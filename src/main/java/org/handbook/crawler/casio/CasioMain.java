package org.handbook.crawler.casio;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class CasioMain {
    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "./crawler";
        int numberOfCrawlers = 10;

        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
//        config.setPolitenessDelay(1000);

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
        controller.addSeed("http://support.casio.com/cn/manual/manuallist.php?cid=001");
        controller.addSeed("http://support.casio.com/cn/manual/manuallist.php?cid=002");
        controller.addSeed("http://support.casio.com/cn/manual/manuallist.php?cid=003");
        controller.addSeed("http://support.casio.com/cn/manual/manuallist.php?cid=004");
        controller.addSeed("http://support.casio.com/cn/manual/manuallist.php?cid=005");
        controller.addSeed("http://support.casio.com/cn/manual/manuallist.php?cid=006");
        controller.addSeed("http://support.casio.com/cn/manual/manuallist.php?cid=007");
        controller.addSeed("http://support.casio.com/cn/manual/manuallist.php?cid=008");
        controller.addSeed("http://support.casio.com/cn/manual/manuallist.php?cid=009");
        controller.addSeed("http://support.casio.com/cn/manual/manuallist.php?cid=010");
        controller.addSeed("http://support.casio.com/cn/manual/manuallist.php?cid=011");
        
        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(CasioCrawler.class, numberOfCrawlers);
    }
}