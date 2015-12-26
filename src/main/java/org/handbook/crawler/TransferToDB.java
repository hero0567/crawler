package org.handbook.crawler;

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
		String url = "jdbc:mysql://localhost:3306/crossp?"
				+ "user=root&password=root&useUnicode=true&characterEncoding=UTF8";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);
			Statement stmt = conn.createStatement();
			// test(stmt);
			saveFileToDB(stmt, "./crasler6740938526464");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			conn.close();
		}

	}

	public static void test(Statement stmt) throws SQLException {
		String sql = "select * from handbook";
		ResultSet rs = stmt.executeQuery(sql);
		while (rs.next()) {
			System.out.println(rs.getString(1) + "\t" + rs.getString(2));
		}
	}

	public static void saveFileToDB(Statement stmt, String filePath) throws Exception {

		File file = new File(filePath);
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
					continue;
				}
				url = splits[0];
				name = splits[1];
				company = splits[2];
				filetype = splits[3];
				filesize = splits[4];
				updatetime = splits[5];
				filemd5 = splits[6];
				downloadcount = splits[7];
				downloadlink1 = splits[8];
				downloadlink2 = splits[9];

				buffer.setLength(0);
				buffer.append(sql).append("'").append(url).append("','").append(name).append("','").append(company)
						.append("','").append(filetype).append("','").append(filesize).append("','").append(updatetime)
						.append("','").append(filemd5).append("','").append(downloadcount).append("','")
						.append(downloadlink1).append("','").append(downloadlink2).append("')");

				System.out.println(buffer.toString());
				stmt.executeUpdate(buffer.toString());
			}
			reader.close();
			read.close();
		}
	}
}