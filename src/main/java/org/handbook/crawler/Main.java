package org.handbook.crawler;

import java.io.File;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {
		String s = "asdfasdf/asdfasd/asdfa/sdf/asdf".replaceAll("/", "_");
		System.out.println(s);
	}
}
