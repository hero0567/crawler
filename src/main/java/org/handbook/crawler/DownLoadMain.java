package org.handbook.crawler;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownLoadMain {
 
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException{

		
		ExecutorService threadPool = Executors.newFixedThreadPool(5);		  
		Connection conn = null;
		String url = "jdbc:mysql://localhost:3306/wmanual?"
				+ "user=root&password=root&useUnicode=true&characterEncoding=UTF8";

		Class.forName("com.mysql.jdbc.Driver");
		conn = DriverManager.getConnection(url);
		Statement stmt = conn.createStatement();
		String sql = "select id, name,url, owner from handbook_download where owner != 'shuomingshuku' and status = 0";
		ResultSet sets = stmt.executeQuery(sql);
		String name= null, downloadlink1=null, owner = "";
		String[] splits = null;
		int id = 0;
		while (sets.next()){			
			try {
				id = sets.getInt("id");
				splits = sets.getString("name").split(">");
				name = id + "_"+ splits[splits.length - 1].trim() + ".pdf";
				name = name.replaceAll("/", "_").replaceAll("\\*", "-");
				downloadlink1 = sets.getString("url");
				owner = sets.getString("owner");
				threadPool.execute(new DownLoadWork(id, name, downloadlink1, owner));				
			} catch (Exception e) {
				System.out.println("Download Failed:" + id + " : " + name + ":" + downloadlink1);
				e.printStackTrace();
			}	
		}
		conn.close();
	}

}