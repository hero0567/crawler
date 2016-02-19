package org.handbook.crawler.galanz;

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
			
			File data = new File("./shuomingshu/olympus");
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
			InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
			BufferedReader reader = new BufferedReader(read);
			String line;
			String url = null, title = null, pdfurl = null, jpgURLs = null;
			String info = null;
			String sql = "insert into crawler_olympus (url, title, pdfurl, jpgurl, info)  values(";
			StringBuffer buffer = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				String[] splits = line.split(";");
				if (splits.length < 2) {
					System.out.println("Skip :" + line);
					continue;
				}
//				b.append(url).append(";").append(title.trim()).append(";").append(pdfURL).append(";")
//				.append(jpgURLs.toString()).append(";").append(headers.toString()).append(";");
				url = splits[0].trim();
				pdfurl = splits[1].trim();

				buffer.setLength(0);
				buffer.append(sql).append("\"").append(url).append("\",\"").append(title).append("\",\"").append(pdfurl)
						.append("\",\"").append(jpgURLs).append("\",\"").append(info).append("\")");

//				System.out.println("Data:" + line);
//				System.out.println("SQL:" + buffer.toString());
//				stmt.addBatch(sql);
				stmt.executeUpdate(buffer.toString());
			}
//			stmt.executeBatch();
			reader.close();
			read.close();
		}
	}
}