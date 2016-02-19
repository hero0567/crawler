package org.handbook.crawler.joyoung;

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

public class JoyoungPostMain {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		String fname = "./joyoung";
		FileOutputStream fs = new FileOutputStream(new File(fname));
		PrintStream p = new PrintStream(fs);
		
		URL url = new URL("http://kf.joyoung.com/downloadCenter_null_null.html");
		Map<String, String> params = new HashMap<String, String>();

		for (int i=1;i<145;i++){
			params.put("page.curPage", String.valueOf(i));
			if (i > 1){
				params.put("page.fen", "10");
				params.put("page.nextPage", String.valueOf(i));
			}
			params.put("page.zongNumsStr", "4191");
			params.put("page.zongPageStr", "142");
			params.put("page.rzong", "1419");
			String result = sendPostMessage(url, params, "utf-8");
			parseHTML(result, p);
		}
	}

	public static void parseHTML(String json, PrintStream p) throws Exception {

		
		Parser parser = new Parser(json);
		
        /* 
         * ���˵��ı�ǩ���� 
       * NodeFilter filter = new TagNameFilter(div); 
        */  
        /* 
         * ���������Ե�HTML 
         * NodeFilter[] nodeFilters = new NodeFilter[1]; 
         * nodeFilters[0] = new AndFilter(new TagNameFilter(div),new HasAttributeFilter(className,classValue)); 
        */  
        NodeFilter nodeFilter  = new LinkStringFilter("pdf");
//        NodeFilter nodeFilter = new AndFilter(new TagNameFilter(div),new HasAttributeFilter(className,classValue)); 
        
        
          
        /** 
         * ���в�ѯƥ�� 
         */  
        NodeList nodeList = parser.extractAllNodesThatMatch(nodeFilter);  
          
        /** 
         * ��ִ�ж�ι����� 
         * ��NodeList��ִ�й�����ʱ���ڶ�������ΪTrue 
         */  
//        nodeList = nodeList.extractAllNodesThatMatch(new TagNameFilter("dl"),true);  
//        nodeList = nodeList.extractAllNodesThatMatch(new TagNameFilter("dt"),true);       
//        nodeList = nodeList.extractAllNodesThatMatch(new AndFilter(new TagNameFilter("a"),new HasAttributeFilter(href)),true);  
          
        //�õ�һ��Node����  
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
			System.out.println("http://kf.joyoung.com" + ((LinkTag) n).getLink());
			
			p.println(version + ";" + name + ";" + "http://kf.joyoung.com" + ((LinkTag) n).getLink() + ";");
        }
//		System.out.println(json);
		

	}

	public static String sendPostMessage(URL url, Map<String, String> params,
			String encode) throws MalformedURLException {

		StringBuffer stringBuffer = new StringBuffer();

		if (params != null && !params.isEmpty()) {
			for (Map.Entry<String, String> entry : params.entrySet()) {
				try {
					stringBuffer
							.append(entry.getKey())
							.append("=")
							.append(URLEncoder.encode(entry.getValue(), encode))
							.append("&");

				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// ɾ�����һ�� & �ַ�
			stringBuffer.deleteCharAt(stringBuffer.length() - 1);

			try {
				HttpURLConnection httpURLConnection = (HttpURLConnection) url
						.openConnection();
				httpURLConnection.setConnectTimeout(3000);
				httpURLConnection.setDoInput(true);// �ӷ�������ȡ����
				httpURLConnection.setDoOutput(true);// �������д������

				// ����ϴ���Ϣ���ֽڴ�С������
				byte[] mydata = stringBuffer.toString().getBytes();
				// ���������������
				httpURLConnection.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				httpURLConnection.setRequestProperty("Content-Lenth",
						String.valueOf(mydata.length));

				// ������������������������
				OutputStream outputStream = (OutputStream) httpURLConnection
						.getOutputStream();
				outputStream.write(mydata);

				// ��÷�������Ӧ�Ľ����״̬��
				int responseCode = httpURLConnection.getResponseCode();
				if (responseCode == 200) {

					// ������������ӷ������˻������
					InputStream inputStream = (InputStream) httpURLConnection
							.getInputStream();
					return (changeInputStream(inputStream, encode));

				}

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return "";
	}

	/*
	 * // �Ѵ�������InputStream��ָ�������ʽencode����ַ���String
	 */
	public static String changeInputStream(InputStream inputStream,
			String encode) {

		// ByteArrayOutputStream һ������ڴ���
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