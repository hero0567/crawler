package org.handbook.crawler.shuomingshuku;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.Statement;

public class TransferToDB {
	public static void main(String[] args) throws Exception {
		Connection conn = null;
		String url = "jdbc:mysql://localhost:3306/wmanual?"
				+ "user=root&password=root&useUnicode=true&characterEncoding=UTF8";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			
			File data = new File("./shuomingshuku");
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
			String url = null, name = null, company = null, filetype = null;
			String filesize = null, updatetime = null, filemd5 = null;
			String downloadcount = null, downloadlink1 = null, downloadlink2 = null;
			String sql = "insert into crawler_data  (url, name, company, filetype, filesize,"
					+ " updatetime, filemd5, downloadcount,downloadlink1,downloadlink2)  values(";
			StringBuffer buffer = new StringBuffer();
			while ((line = reader.readLine()) != null) {
				String[] splits = line.split(";");
				if (splits.length < 10) {
					System.out.println("Skip :" + line);
					continue;
				}
				url = splits[0].trim();
				name = splits[1].trim().replaceAll("\"", "\\\\\"");
				company = splits[2].trim().replaceAll("\"", "\\\\\"");
				filetype = splits[3].trim().replaceAll("\"", "\\\\\"");
				filesize = splits[4].trim();
				updatetime = splits[5].trim();
				filemd5 = splits[6].trim();
				downloadcount = splits[7].trim();
				downloadlink1 = splits[8].trim();
				downloadlink2 = splits[9].trim().replaceAll("\"", "\\\\\"");

				buffer.setLength(0);
				buffer.append(sql).append("\"").append(url).append("\",\"").append(name).append("\",\"").append(company)
						.append("\",\"").append(filetype).append("\",\"").append(filesize).append("\",\"").append(updatetime)
						.append("\",\"").append(filemd5).append("\",\"").append(downloadcount).append("\",\"")
						.append(downloadlink1).append("\",\"").append(downloadlink2).append("\")");
//
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