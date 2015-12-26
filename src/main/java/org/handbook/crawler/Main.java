package org.handbook.crawler;

import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args){
		boolean x = Pattern.compile(".*(css|js|gif|jpg|png|mp3|mp3|zip|/|gz|html)$").matcher("http://www.facebook.com/").matches();
		System.out.println(x);
	}
}
