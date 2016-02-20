package org.handbook.crawler.littleswan;

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

public class LittleswanCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz))$");

	private final static Pattern DOC_FILTERS = Pattern.compile(".*(css|js|gif|mp3|zip|gz|/)$");

	private List<String> urls = new ArrayList<String>();
	private List<Pattern> urlsPattern = new ArrayList<Pattern>();

	private FileOutputStream fs = null;
	private PrintStream p = null;

	public LittleswanCrawler() {
		urls.add("http://www.littleswan.com/FrontLoading/show/51.html".toLowerCase());
		        
        urlsPattern.add(Pattern.compile("http://www.littleswan.com/FullAutomatic/show/".toLowerCase()));
        urlsPattern.add(Pattern.compile("http://www.littleswan.com/TwinTub/show/".toLowerCase()));
        urlsPattern.add(Pattern.compile("http://www.littleswan.com/Fridge/show/".toLowerCase()));
        urlsPattern.add(Pattern.compile("http://www.littleswan.com/Airconditioner/show/".toLowerCase()));
        urlsPattern.add(Pattern.compile("http://www.littleswan.com/Dryer/show/".toLowerCase()));        
        urlsPattern.add(Pattern.compile("http://www.littleswan.com/FrontLoading/show/".toLowerCase()));
        
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
//			for (String u : urls) {
//				if (href.toLowerCase().startsWith(u)) {
//					return true;
//				}
//			}
			for(Pattern u : urlsPattern ){
       		 if (u.matcher(href).matches()){
       			 return true;
       		 }
       	 }
		}
		return false;
	}

	/**
	 * This function is called when a page is fetched and ready to be processed
	 * by your program. 鍘傚�?: 椋炲埄娴�? 鏂囦欢绫诲�??: PDF 鏂囦欢澶у�??: 324.17 KB 涓婁紶鏃堕棿: 2012-05-02
	 * 16:33:21 鏂囦欢鏍￠獙: 7188D0015E6D2DF4549C1095C5C52E15 涓嬭浇缁熻: 2715
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
			//Frontloading/down/id
			for (WebURL link : links) {
				String l = link.getURL().toLowerCase();
				doc = !DOC_FILTERS.matcher(l).matches();
				System.out.println("        "+l);
				if (doc) {
					if (l.toLowerCase().indexOf("/down/id") > 0) {
						hasPDF = true;
						pdfURL = l;
					}
					if (l.endsWith("jpg")) {
						jpgURLs.append(l).append(",");
					}
				}
			}

			if (hasPDF) {
				String title = htmlParseData.getTitle();				
				b.append(url).append(";").append(title.trim()).append(";").append(pdfURL).append(";").append(jpgURLs.toString()).append(";");
				p.println(b);
			}
		}

	}
}