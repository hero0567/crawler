package org.handbook.crawler.littleswan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class LittleswanMain {
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
        	
//        controller.addSeed("http://www.littleswan.com/FrontLoading/show/51.html");
        addSeed(controller, "http://www.littleswan.com/FullAutomatic/show/");
        addSeed(controller, "http://www.littleswan.com/TwinTub/show/");
        addSeed(controller, "http://www.littleswan.com/Fridge/show/");
        addSeed(controller, "http://www.littleswan.com/Airconditioner/show/");
        addSeed(controller, "http://www.littleswan.com/Dryer/show/");        
        addSeed(controller, "http://www.littleswan.com/FrontLoading/show/");
        
        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(LittleswanCrawler.class, numberOfCrawlers);
    }
    
    public static void addSeed(CrawlController controller, String url) throws Exception{
    	for (int i =0; i < 100; i++){
    		controller.addSeed(url + String.valueOf(i) + ".html");
    	}    	
    }
    
    
    
}