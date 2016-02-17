package org.handbook.crawler.huawei;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HuaweiMain {
	public static void main(String[] args) throws Exception {

		String fname = "./huawei";
		FileOutputStream fs = new FileOutputStream(new File(fname));
		PrintStream p = new PrintStream(fs);
		StringBuffer str = new StringBuffer();
		String s;
		try {
			//鎸囧崡
			//http://consumer.huawei.com/cn/support/search/index.htm?keywords=%E6%8C%87%E5%8D%97
			URL url = new URL("http://consumer.huawei.com/support/services/service/isearch/list?"
					+ "jsonp=jQuery17109234153782610038_1455701380086&siteCode=cn&APP_NUM=A59848115&queryMatch=support%3Amanual&tagMatch="
					+ "support%3Amanual%2Csoftware%2Cfaqs&topMatch=support_product%3Ano&mcCurPage=1&"
					+ "pageSize=1000&ssUserText=%25E6%258C%2587%25E5%258D%2597&_=1455701383394");
			InputStream is = url.openStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			
			BufferedReader br = new BufferedReader(isr);
			while ((s = br.readLine()) != null){
				str.append(s);
			}
//			
//			String json = "{\"list\":[{\"productlist\":[],\"totalNum\":538,\"keywords\":\"指南\",\"sort\":\"relevance\",\"alllist\":[{\"RESERVE_FIELD22\":\"manual\",\"autn_section\":\"0\",\"DRETITLE\":\"华为Mate8 快速<font color=red>指南</font>(NXT-AL10&NXT-TL00&NXT-CL00&NXT-DL00, V1.0, 中文)\",\"DREDATE_YEAR_MONTH\":\"\",\"RESERVE_FIELD25\":\"\",\"RESERVE_FIELD26\":\"\",\"RESERVE_FIELD33\":\"http://download-c.huawei.com/download/downloadCenter?downloadId=59438&version=193516&siteCode=cn\",\"RESERVE_FIELD32\":\"\",\"autn_summary\":\"\",\"autn_links\":\"指南,指南,指南,指南\",\"DOC_DESCRIPTION\":\"华为Mate 8 快速<font color=red>指南</font>\",\"autn_title\":\"华为Mate8 快速指南(NXT-AL10&NXT-TL00&NXT-CL00&NXT-DL00, V1.0, 中文)\",\"autn_database\":\"WWW_DEVICE_CN\",\"autn_id\":\"840794\",\"RESERVE_FIELD39\":\"NXT-AL10B\",\"RESERVE_FIELD38\":[{\"id\":\"2\",\"name\":\"简体中文\"}],\"RESERVE_FIELD37\":\"1.03 MB\",\"RESERVE_FIELD36\":\"\",\"RESERVE_FIELD35\":\"\",\"RESERVE_FIELD34\":\"\",\"RESERVE_FIELD11\":\"2015-11-26\",\"DOC_ID\":\"59438\",\"autn_reference\":\"consumer_A59848115_manual_59438.idx\",\"DREDATE\":\"1448570707\",\"RESERVE_FIELD40\":\"\",\"RESERVE_FIELD6\":\"\",\"DOC_URL\":\"http://consumer.huawei.com/cn/support/manuals/detail/index.htm?id=59438\",\"autn_weight\":\"80.18\",\"RESERVE_FIELD2\":\"support\"}],\"totalPageNum\":538,\"tagValue\":{\"manual\":470,\"software\":2,\"support\":538,\"faqs\":66,\"all\":538},\"mcCurPage\":1}]}";
			String json = str.toString();
			
			System.out.println(json);
			System.out.println(json.indexOf('('));
			json = json.substring(json.indexOf('(') + 1);
			json = json.substring(0, json.length() - 1);
			
			ObjectMapper mapper = new ObjectMapper();  
            JsonNode rootNode = mapper.readTree(json);
            
            Iterator<JsonNode> list = rootNode.path("list").elements();            
            
            while(list.hasNext()){
            	JsonNode l = list.next();
            	Iterator<JsonNode> alllist = l.path("alllist").elements(); 
            	 while(alllist.hasNext()){
                 	JsonNode al = alllist.next();
                 	//autn_title
                 	String name = al.path("autn_title").asText();
                 	String link = al.path("RESERVE_FIELD33").asText();
                 	String date = al.path("RESERVE_FIELD11").asText();
                 	String size = al.path("RESERVE_FIELD37").asText();
                 	String version = al.path("RESERVE_FIELD39").asText();
                 	
                 	System.out.println(name);
                 	System.out.println(link);
                 	p.println(name + ";" + link+ ";" + date+ ";" + size+ ";" + version);
                 }
            }
            
            
            
			
//			System.out.println(json);
			br.close();
			p.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}
}