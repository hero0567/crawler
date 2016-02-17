package org.handbook.crawler.huawei;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class HuaweiMain {
	public static void main(String[] args) throws Exception {

		StringBuffer str = new StringBuffer();
		String s;
		try {
			//指南
			//http://consumer.huawei.com/cn/support/search/index.htm?keywords=%E6%8C%87%E5%8D%97
			URL url = new URL("http://consumer.huawei.com/support/services/service/isearch/list?"
					+ "jsonp=jQuery17109234153782610038_1455701380086&siteCode=cn&APP_NUM=A59848115&queryMatch=support%3Amanual&tagMatch="
					+ "support%3Amanual%2Csoftware%2Cfaqs&topMatch=support_product%3Ano&mcCurPage=1&"
					+ "pageSize=1&ssUserText=%25E6%258C%2587%25E5%258D%2597&_=1455701383394");
			InputStream is = url.openStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			while ((s = br.readLine()) != null){
				str.append(s);
			}
			
			
			String json = str.toString();
			
			System.out.println(json);
			System.out.println(json.indexOf('('));
			json = json.substring(json.indexOf('(') + 1);
			json = json.substring(0, json.length() - 1);
			
//			JSONObject object;
			
			System.out.println(json);
			br.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}