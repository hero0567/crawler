package org.handbook.crawler.samsung;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class SamsungCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz))$");

	private final static Pattern DOC_FILTERS = Pattern.compile(".*(css|js|gif|mp3|zip|gz|/|html)$");

	private List<String> urls = new ArrayList<String>();

	private FileOutputStream fs = null;
	private PrintStream p = null;

	public SamsungCrawler() {
		urls.add("http://www.samsung.com/cn/consumer/mobile-phones/smart-phone".toLowerCase());
//		urls.add("http://www.samsung.com/cn/consumer/mobile-phones/tablets");
//        urls.add("http://www.samsung.com/cn/consumer/mobile-phones/gear");
//        urls.add("http://www.samsung.com/cn/consumer/mobile-phones/gear");
//        urls.add("http://www.samsung.com/cn/consumer/tv-audio-video/home-audio");
//        urls.add("http://www.samsung.com/cn/consumer/computers-office/ultra-mobile-pc");
//        urls.add("http://www.samsung.com/cn/consumer/computers-office/monitors");
//        urls.add("http://www.samsung.com/cn/consumer/computers-office/monitors");
//        urls.add("http://www.samsung.com/cn/consumer/computers-office/camera-lens");
//        urls.add("http://www.samsung.com/cn/consumer/home-appliances/refrigerators");
//        urls.add("http://www.samsung.com/cn/consumer/home-appliances/washers-dryers");
//        urls.add("http://www.samsung.com/cn/consumer/home-appliances/air-conditioners");
//        urls.add("http://www.samsung.com/cn/consumer/home-appliances/plasma-air-purifier");
//        urls.add("http://www.samsung.com/cn/consumer/home-appliances/rambo");
//        urls.add("http://www.samsung.com/cn/consumer/memory/ssd");
//        urls.add("http://www.samsung.com/cn/consumer/memory/usb");
        
		try {
			String fname = "./crasler" + System.nanoTime();
			fs = new FileOutputStream(new File(fname));
			p = new PrintStream(fs);

			System.out.println(fname);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method receives two parameters. The first parameter is the page in
	 * which we have discovered this new url and the second parameter is the new
	 * url. You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic). In this example,
	 * we are instructing the crawler to ignore urls that have css, js, git, ...
	 * extensions and to only accept urls that start with
	 * "http://www.ics.uci.edu/". In this case, we didn't need the referringPage
	 * parameter to make the decision.
	 */
	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL().toLowerCase();
		boolean filter = !FILTERS.matcher(href).matches();
		if (filter) {
			for (String u : urls) {
				if (href.toLowerCase().startsWith(u)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program. 厂商: 飞利�? 文件类型: PDF 文件大小: 324.17 KB 上传时间: 2012-05-02
	 * 16:33:21 文件校验: 7188D0015E6D2DF4549C1095C5C52E15 下载统计: 2715
	 * 
	 * Home > Industrial> > Processors> Linear LTC3676 LTC3676-1 Processors
	 * Datasheet Company: File format: PDF File size: 477.02 KB MDS Checksum:
	 * 2014-07-09 10:23:20 File MD5: Downloads: 1545
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
		System.out.println("URL: " + url);
		StringBuilder b = new StringBuilder();
		StringBuilder jpgURLs = new StringBuilder();
		
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			Set<WebURL> links = htmlParseData.getOutgoingUrls();

			boolean doc = false;
			boolean hasPDF = false;
			String pdfURL = "";
			for (WebURL link : links) {
				String l = link.getURL().toLowerCase();
				doc = !DOC_FILTERS.matcher(l).matches();
				if (doc) {
					System.out.println("  link: " + l);
					if (l.endsWith("pdf")) {
						hasPDF = true;
						pdfURL = l;
					}
					if (l.endsWith("png") || l.endsWith("jpg")) {
						jpgURLs.append(l).append(",");
					}
				}
			}

			if (hasPDF) {
				String title = htmlParseData.getTitle();
				b.append(url).append(";").append(title.trim()).append(";").append(pdfURL).append(";")
						.append(jpgURLs.toString()).append(";");
				p.println(b);
			}
		}

	}
}