package org.handbook.crawler;

import java.io.File;
import java.util.regex.Pattern;

public class Main {

	public static void main(String[] args) {
		String s = "http://www.philips.com.cn/c-p/56PFL9954H_98/cinema-219-lcd-tv-with-ambilight-spectra-3-and-perfect-pixel-hd-engine";
		String s1 ="http://www.philips.com.cn/c-p/56pfl9954h_98/cinema-219-lcd-tv-with-ambilight-spectra-3-and-perfect-pixel-hd-engine/support";

		System.out.print(s1.startsWith(s));

	}
}
