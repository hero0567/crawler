package org.handbook.crawler;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownLoadMain {
	

	
	public static void main(String[] args) throws SQLException, ClassNotFoundException{

		
		ExecutorService threadPool = Executors.newFixedThreadPool(3);		  
		Connection conn = null;
		String url = "jdbc:mysql://localhost:3306/crossp?"
				+ "user=root&password=root&useUnicode=true&characterEncoding=UTF8";

		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(url);
		Statement stmt = conn.createStatement();
		String sql = "select id, name,downloadlink1 from crawler_data where (name like '首页 > 家用电器%' or name like 'Home > Electronics%') and download is false";
		ResultSet sets = stmt.executeQuery(sql);
		String name= null, downloadlink1=null;
		String[] splits = null;
		int id = 0;
		while (sets.next()){			
			try {
				id = sets.getInt("id");
				splits = sets.getString("name").split(">");
				name = id + "_"+ splits[splits.length - 1].trim() + ".pdf";
				name = name.replaceAll("/", "_");
				downloadlink1 = sets.getString("downloadlink1");
				threadPool.execute(new DownLoadWork(id, name, downloadlink1));					
			} catch (Exception e) {
				System.out.println("Download Failed:" + id + " : " + name + ":" + downloadlink1);
				e.printStackTrace();
			}	
		}
		conn.close();
	}

}