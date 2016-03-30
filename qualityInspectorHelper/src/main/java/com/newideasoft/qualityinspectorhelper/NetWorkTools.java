package com.newideasoft.qualityinspectorhelper;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetWorkTools {
	/**
	 * 
	 * @param urlPath 服务器的地址
	 * @param data 请求时传输的参数数据
	 * @return post请求的连接，已经传递了参数
	 * @throws Exception
	 */
	public static HttpURLConnection getConnection(String urlPath, byte[] data) throws Exception {
		URL url = new URL(urlPath);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setConnectTimeout(5000);
		connection.setReadTimeout(10000);
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		connection.setRequestProperty("Content-Length", data.length + "");
		OutputStream os = connection.getOutputStream();
		os.write(data);
		os.close();
		return connection;
	}
	/**
	 * 
	 * @param connection 已经向服务器发出请求的连接对象
	 * @return 服务器返回的数据流
	 * @throws Exception
	 */
	public static ByteArrayOutputStream geResponseData(HttpURLConnection connection) throws Exception {
		ByteArrayOutputStream baos = null;
		int responseCode = connection.getResponseCode();
		if (responseCode == 200) {
			InputStream is = connection.getInputStream();
			baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			baos.close();
			is.close();
		}
		return baos;
	}
}
