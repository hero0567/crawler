package org.handbook.crawler.galanz;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.LinkStringFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class GalanzPageMain {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		String fname = "./galanz";
		FileOutputStream fs = new FileOutputStream(new File(fname));
		PrintStream p = new PrintStream(fs);
		
		Map<String, String> params = new HashMap<String, String>();

		for (int i=1;i<22;i++){
			URL url = new URL("http://www.galanz.com.cn/pages/srv_5.aspx?catid=18|215&p=" + i);
			String result = sendGetMessage(url, params, "utf-8");
			parseHTML(result, p);
		}
	}

	public static void parseHTML(String json, PrintStream p) throws Exception {

		Parser parser = new Parser(json);
		
        /* 
         * 过滤到的标签过滤 
       * NodeFilter filter = new TagNameFilter(div); 
        */  
        /* 
         * 过滤有属性的HTML 
         * NodeFilter[] nodeFilters = new NodeFilter[1]; 
         * nodeFilters[0] = new AndFilter(new TagNameFilter(div),new HasAttributeFilter(className,classValue)); 
        */  
       
//        NodeFilter nodeFilter = new AndFilter(new TagNameFilter(div),new HasAttributeFilter(className,classValue)); 
        
        
          
        /** 
         * 进行查询匹配 
         */  
		NodeFilter nodeFilter  = new LinkStringFilter("rar");
        NodeList nodeList = parser.extractAllNodesThatMatch(nodeFilter);  
          
        /** 
         * 可执行多次过滤器 
         * 在NodeList中执行过滤器时，第二个参数为True 
         */  
//        nodeList = nodeList.extractAllNodesThatMatch(new TagNameFilter("dl"),true);  
//        nodeList = nodeList.extractAllNodesThatMatch(new TagNameFilter("dt"),true);       
//        nodeList = nodeList.extractAllNodesThatMatch(new AndFilter(new TagNameFilter("a"),new HasAttributeFilter(href)),true);  
          
        //得到一个Node数组  
        Node[] node = nodeList.toNodeArray();
        for (Node n : node){
        	NodeList nl = n.getParent().getParent().getChildren();
        	int i = 0;
        	String content = "";
        	String name = "";
        	String version = "";
        	for (Node nd : nl.toNodeArray()){    
        		content = nd.toPlainTextString();
        		if (content.trim().length() > 0 && i < 3){
        			i++;
        			version =  i == 2 ?  content : version;
        			name =  i == 3 ?  content : name;
        		}
        		
        	}
        	System.out.println(version);
        	System.out.println(name);
			System.out.println("http://www.galanz.com.cn" + ((LinkTag) n).getLink());
			
			p.println(version + ";" + name + ";" + "http://www.galanz.com.cn" + ((LinkTag) n).getLink() + ";");
        }
//		System.out.println(json);
		

	}

	public static String sendGetMessage(URL url, Map<String, String> params,
			String encode) throws IOException {

		StringBuffer str = new StringBuffer();
		String s;
		InputStream is = url.openStream();
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");
		
		BufferedReader br = new BufferedReader(isr);
		while ((s = br.readLine()) != null){
			str.append(s);
		}

		return str.toString();
	}

	/*
	 * // 把从输入流InputStream按指定编码格式encode变成字符串String
	 */
	public static String changeInputStream(InputStream inputStream,
			String encode) {

		// ByteArrayOutputStream 一般叫做内存流
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		byte[] data = new byte[1024];
		int len = 0;
		String result = "";
		if (inputStream != null) {
			try {
				while ((len = inputStream.read(data)) != -1) {
					byteArrayOutputStream.write(data, 0, len);

				}
				result = new String(byteArrayOutputStream.toByteArray(), encode);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
}