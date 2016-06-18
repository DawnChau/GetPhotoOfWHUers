package photo;

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
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.ws.Response;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Photo {

	public static final String mainURL = "http://202.114.74.136/cet/Default.aspx";
	public static final String realURL = "http://202.114.74.136/cet/cjcx.aspx";
	public static final String photoURL = "http://202.114.74.136/cet/getphoto.aspx";
	
	public static final String cookie = "ASP.NET_SessionId=gvvzd555lu2z4z55fkzu2445";
	public static final String checkCode = "ab4a";

	public void PostInfo(String id, String name) {
		try {
			URL url = new URL(mainURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setInstanceFollowRedirects(false);
			conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("Cookie", cookie);
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");
			conn.setDoOutput(true);
			conn.setUseCaches(false);

			StringBuffer params = new StringBuffer();
			String encodeName = java.net.URLEncoder.encode(name, "utf-8");
			params.append("__VIEWSTATE=%2FwEPDwUJNzk0Mjc1ODYxZGTA2JHtu%2F6dMRKjxx4BTOvIwSiodw%3D%3D&"
					+ "__VIEWSTATEGENERATOR=EEDA54F8&"
					+ "__EVENTVALIDATION=%2FwEWBwKB463wCgLs0bLrBgLs0fbZDALs0Yq1BQLs0e58AoznisYGArursYYIRM5sLK%2F%2FaqCBB6vcyOYfSaHnYlk%3D&"
					+ "TextBox1=" + id + "&TextBox2=&TextBox3=" + encodeName
					+ "&TextBox4="
					+ checkCode
					+ "&Button1=%E6%9F%A5%E8%AF%A2");
			byte[] bytes = params.toString().getBytes();
			conn.getOutputStream().write(bytes);

			int responseCode = conn.getResponseCode();

			System.out.println(responseCode);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getReal(String name,String school) {
		try {
			URL url = new URL(realURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Referer", "http://202.114.74.136/cet/Default.aspx");
			conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Cookie", cookie);
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");

			int responseCode = conn.getResponseCode();
			System.out.println(responseCode);
			int length = conn.getContentLength();
			if(length<2000)
				System.out.println("没考四级");
			else
				this.getPhoto(name,school);

//			InputStream in = conn.getInputStream();
//			InputStreamReader inputStreamReader = new InputStreamReader(in, "UTF-8");
//			BufferedReader reader = new BufferedReader(inputStreamReader);
//			String tempLine;
//			StringBuffer sb = new StringBuffer();
//			while ((tempLine = reader.readLine()) != null) {
//				sb.append(tempLine);
//			}
			//System.out.println(sb.toString());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void getPhoto(String name,String school) {
		try {
			URL url = new URL(photoURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept-Encoding", "gzip, deflate, sdch");
			conn.setRequestProperty("Connection", "keep-alive");
			conn.setRequestProperty("Referer", "http://202.114.74.136/cet/Default.aspx");
			conn.setRequestProperty("Cookie", cookie);
			conn.setRequestProperty("User-Agent",
					"Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.102 Safari/537.36");

			int responseCode = conn.getResponseCode();
			System.out.println(responseCode);

			InputStream in = conn.getInputStream();
			byte[] data = new byte[1024];
			int len = 0;
			FileOutputStream fileOutputStream = null;

			File dir = new File("D:\\四六级图片\\" +school + File.separator);
			if(!dir.exists()){
				dir.mkdirs();
			}
			File file = new File("D:\\四六级图片\\" +school + "\\" +  name + ".jpg");
			if(file.exists())
				return ;
			fileOutputStream = new FileOutputStream(file);
			while ((len = in.read(data)) != -1) {
				fileOutputStream.write(data, 0, len);
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		File file = new File("2014_all.xls");
		Photo p = new Photo();
		try {
			InputStream in = new FileInputStream(file);
			Workbook workbook = Workbook.getWorkbook(in);
			// 获取第一张Sheet表
			Sheet sheet = workbook.getSheet(0);

			Cell[] cells0 = sheet.getColumn(0);
			Cell[] cells1 = sheet.getColumn(1);
			Cell[] cells2 = sheet.getColumn(2);

			int i = 2400;
			while (i < cells0.length) {
				String id = cells0[i].getContents();
				String name = cells1[i].getContents();
				String school = cells2[i].getContents();

				p.PostInfo(id, name);
				p.getReal(name,school);
				i++;
				System.out.println("已经扫描----------------------->" + (i*1.0/cells0.length)*100);
				//Thread.sleep(100);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
