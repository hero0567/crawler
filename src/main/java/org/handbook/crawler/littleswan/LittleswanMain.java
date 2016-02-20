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
        int numberOfCrawlers = 40;

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
        	
        controller.addSeed("http://www.littleswan.com/FrontLoading/show/51.html");
        controller.addSeed("http://www.littleswan.com/FullAutomatic/");
        controller.addSeed("http://www.littleswan.com/TwinTub/");
        controller.addSeed("http://www.littleswan.com/Fridge/");
        controller.addSeed("http://www.littleswan.com/Airconditioner/show/29.html");
        controller.addSeed("http://www.littleswan.com/Dryer/show/1.html");
        controller.addSeed("http://www.littleswan.com/Dryer/show/2.html");
        
//        addSeed(controller);
        
        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */
        controller.start(LittleswanCrawler.class, numberOfCrawlers);
    }
    
    public static void addSeed(CrawlController controller) throws Exception{
    	
    	File data = new File("./shuomingshu/sony/seed");
		File[] fs = data.listFiles();
		for (int i = 0; i < fs.length; i++) {
			System.out.println(fs[i].getAbsolutePath());
			if (fs[i].isFile() && fs[i].exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(fs[i]), "UTF-8");
				BufferedReader reader = new BufferedReader(read);
				String line;
				while ((line = reader.readLine()) != null) {
					controller.addSeed(line);
//					System.out.println(line);
				}
				reader.close();
				read.close();
			}
		}
    }
    
    
    
}