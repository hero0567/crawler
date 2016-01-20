package org.handbook.crawler;

import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {
		String s = "http://www.shuomingshuku.com/s/2-0-0-\\d*-37-0.html";
		String s1 ="http://www.shuomingshuku.com/s/2-0-0-11-37-0.html";

		Pattern FILTERS = Pattern.compile(s);
		
		boolean filter = FILTERS.matcher(s1).matches();
		
		System.out.println(filter);
	}
}
