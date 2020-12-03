package repoimgur;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author J
 */
public class ImgurUploader {

	public static String toBase64(File file) throws Exception {
		try {
			byte[] b = new byte[(int) file.length()];
			FileInputStream fs = new FileInputStream(file);
			fs.read(b);
			fs.close();
			return URLEncoder.encode(DatatypeConverter.printBase64Binary(b), "UTF-8");
		} catch (IOException e) {
			throw new Exception();

		}
	}

	public static String getImgurContent(String clientID, String fileString) throws Exception {

//         clientID = "(edited out)";

		URL url;

		url = new URL("https://api.imgur.com/3/image");
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

//		String data = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder
//				.encode("https://indianochka.ru/wp-content/uploads/2018/09/10-ganesh-1-750x450-1-750x400.jpg", "UTF-8");

		String data = URLEncoder.encode("image", "UTF-8") + "=" + fileString;

		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Authorization", "Client-ID " + clientID);
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		conn.connect();
		StringBuilder stb = new StringBuilder();
		OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(data);
		wr.flush();

		// Get the response
		BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String line;
		while ((line = rd.readLine()) != null) {
			stb.append(line).append("\n");
		}
		wr.close();
		rd.close();

		System.out.println("Uploaded");
//		System.out.println(stb.toString());

		return stb.toString();
	}

}