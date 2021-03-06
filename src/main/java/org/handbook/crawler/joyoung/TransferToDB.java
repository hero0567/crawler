package org.handbook.crawler.joyoung;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;


public class TransferToDB {
	public static void main(String[] args) throws Exception {
		Connection conn = null;
		String url = "jdbc:mysql://localhost:3306/crawler?"
				+ "user=root&password=root&useUnicode=true&characterEncoding=UTF8";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			
			File data = new File("./shuomingshu/joyoung");
			File[] fs = data.listFiles();
			for (int i = 0; i < fs.length; i++) {
				System.out.println(fs[i].getAbsolutePath());
				saveFileToDB(stmt, fs[i]);
				conn.commit();	
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}
	}

	public static void saveFileToDB(Statement stmt, File file) throws Exception {
		if (file.isFile() && file.exists()) {
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), "GBK");
			BufferedReader reader = new BufferedReader(read);
			String line;
			String url = null, title = null, info = null, jpgURLs = null;
			String headers = null;
			String sql = "insert into crawler_joyoung  (info, title, pdfurl)  values(";
			StringBuffer buffer = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				String[] splits = line.split(";");
				if (splits.length < 3) {
					System.out.println("Skip :" + line);
					continue;
				}
//				b.append(url).append(";").append(title.trim()).append(";").append(pdfURL).append(";")
//				.append(jpgURLs.toString()).append(";").append(headers.toString()).append(";");
				url = splits[2].trim();
				title = splits[1].trim();
				info = splits[0].trim();		

				buffer.setLength(0);
				buffer.append(sql).append("\"").append(url).append("\",\"").append(title).append("\",\"").append(info).append("\")");

				System.out.println("Data:" + line);
				System.out.println("SQL:" + buffer.toString());
//				stmt.addBatch(sql);
				stmt.executeUpdate(buffer.toString());
			}
//			stmt.executeBatch();
			reader.close();
			read.close();
		}
	}
}