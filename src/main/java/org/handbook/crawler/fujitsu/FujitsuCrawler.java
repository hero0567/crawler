package org.handbook.crawler.fujitsu;

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

public class FujitsuCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz))$");

	private final static Pattern DOC_FILTERS = Pattern.compile(".*(css|js|gif|mp3|zip|gz|/|html)$");

	private List<String> urls = new ArrayList<String>();

	private FileOutputStream fs = null;
	private PrintStream p = null;

	public FujitsuCrawler() {
		urls.add("http://www.fujitsu-general.com/cn/support/downloads/".toLowerCase());
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
	 * by your program. é?˜å‚šæ™¢: æ¤‹ç‚²åŸ„å¨´ï¿½ é?‚å›¦æ¬¢ç»«è¯²ç€·: PDF é?‚å›¦æ¬¢æ¾¶Ñƒï¿½?: 324.17 KB æ¶“å©?ç´¶é?ƒå •æ£¿: 2012-05-02
	 * 16:33:21 é?‚å›¦æ¬¢é??ï¿ ç?™: 7188D0015E6D2DF4549C1095C5C52E15 æ¶“å¬­æµ‡ç¼?ç†»î…¸: 2715
	 * 
	 * Home > Industrial> > Processors> Linear LTC3676 LTC3676-1 Processors
	 * Datasheet Company: File format: PDF File size: 477.02 KB MDS Checksum:
	 * 2014-07-09 10:23:20 File MD5: Downloads: 1545
	 */
	@Override
	public void visit(Page page) {
		String url = page.getWebURL().getURL();
//		System.out.println("URL: " + url);
		StringBuilder b = new StringBuilder();
		StringBuilder jpgURLs = new StringBuilder();
		StringBuilder headers = new StringBuilder();
		
		if (page.getParseData() instanceof HtmlParseData) {
			HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
			Set<WebURL> links = htmlParseData.getOutgoingUrls();

			boolean doc = false;
			for (WebURL link : links) {
				String l = link.getURL();
				doc = !DOC_FILTERS.matcher(l).matches();
				System.out.println("  link: " + l);
				if (doc) {
					
					if (l.startsWith("http://olympus-imaging.cn/support/m_dl.php")) {
						p.println(url + ";" + l + ";");
					}
				}
			}

		}

	}
}