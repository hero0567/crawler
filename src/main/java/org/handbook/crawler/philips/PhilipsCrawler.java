package org.handbook.crawler.philips;

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

public class PhilipsCrawler extends WebCrawler {

	private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|gif|jpg|png|mp3|mp3|zip|gz))$");

	private final static Pattern DOC_FILTERS = Pattern.compile(".*(css|js|gif|png|mp3|zip|gz|/|html)$");

	private List<String> urls = new ArrayList<String>();

	private FileOutputStream fs = null;
	private PrintStream p = null;

	public PhilipsCrawler() {
		urls.add("http://www.philips.com.cn/c-p/56PFL9954H_98/cinema-219-lcd-tv-with-ambilight-spectra-3-and-perfect-pixel-hd-engine".toLowerCase());
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
	 * by your program. 厂商: 飞利浦 文件类型: PDF 文件大小: 324.17 KB 上传时间: 2012-05-02
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
					if (l.endsWith("dfu_zhs.pdf")) {
						hasPDF = true;
						pdfURL = l;
					}
					if (l.indexOf("images.philips.com") > 0) {
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