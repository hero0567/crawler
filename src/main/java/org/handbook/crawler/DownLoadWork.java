package org.handbook.crawler;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;

public class DownLoadWork extends Thread {
	
	private int id; 
	private String name; 
	private String downloadlink1;
	
	public DownLoadWork(int id, String name, String downloadlink1){
		this.id = id;
		this.name = name;
		this.downloadlink1 = downloadlink1;
	}
	
	/**
	 * 从网络Url中下载文件
	 * 
	 * @param urlStr
	 * @param fileName
	 * @param savePath
	 * @throws IOException
	 */
	public void downLoadFromUrl(String urlStr, String fileName, String savePath) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(3 * 1000);
		conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

		InputStream inputStream = conn.getInputStream();
		byte[] getData = readInputStream(inputStream);

		File saveDir = new File(savePath);
		if (!saveDir.exists()) {
			saveDir.mkdir();
		}
		File file = new File(saveDir + File.separator + fileName);
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(getData);
		if (fos != null) {
			fos.close();
		}
		if (inputStream != null) {
			inputStream.close();
		}

		System.out.println("info:" + url + " download success");

	}

	public byte[] readInputStream(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}


	@Override
	public void run() {
		
		System.out.println("start a new task for id :" + id);
		
		Connection conn = null;
		String url = "jdbc:mysql://localhost:3306/crossp?"
				+ "user=root&password=root&useUnicode=true&characterEncoding=UTF8";

			
		PreparedStatement pstmt = null;		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url);
			String updateSql = "update crawler_data set download = ?, filename= ? where id = ?";	
			pstmt = (PreparedStatement) conn.prepareStatement(updateSql);
			downLoadFromUrl(downloadlink1, name, "download/");			
			pstmt.setInt(1, 1);
	        pstmt.setString(2, name);
	        pstmt.setInt(3, id);
	        pstmt.executeUpdate();	
	        System.out.println("finished task for id :" + id);
		} catch (Exception e) {
			System.out.println("Download Failed:" + id + " : " + name + ":" + downloadlink1);
			e.printStackTrace();
		}finally{
			if (pstmt != null){
				try {
					pstmt.close();
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		
	}
}