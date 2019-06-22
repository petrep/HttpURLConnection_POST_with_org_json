package com.test;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

// Working version. To import dependenies: right-click on the Eclipse project, select:
// Build Path / configure build path / libraries / add jars  and select your .jar files.
public class ClientWithAuthAndJson3 {

	private static final String UTF_8 = "UTF-8";

	public static void main(String[] args) throws JSONException, IOException {
		// HttpURLConnection urlConnection;
		// String url;
		// String data = json;
		// String result = null;
		try {
			String username = "test@liferay.com";
			String password = "test";

			String auth2 = new String(username + ":" + password);
			byte[] data1 = auth2.getBytes(UTF_8);
			// String base64 = Base64.encodeToString(data1, Base64.);
			String base64 = Base64.getEncoder().withoutPadding().encodeToString(data1);
			// String urlBasePath =
			// "http://localhost:8080/api/jsonws/journal.journalarticle/add-article";
			// String urlBasePath =
			// "http://localhost:8080/api/jsonws/dlapp/add-file-entry";
			// String urlBasePath =
			// "http://localhost:8080/api/jsonws/company/get-companies";
			// String urlBasePath =
			// "http://localhost:8080/api/jsonws/user/get-user-id-by-screen-name";
			// String urlBasePath = "http://localhost:8080/api/jsonws/user";
//			 String query = "https://ptsv2.com/t/et4ro-1560440878/post";
//			 String query = "https://postman-echo.com/post";
			String query = "http://validate.jsontest.com/";

			// companyId = 20115


			// create the connection
			URL url = new URL(query);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			conn.setRequestProperty("charset", "utf-8");

			// set up the data to send ("json=foobar")
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("companyId", 20115);
			jsonObject.put("screenName", "test");
			String urlParameters = "json=" + jsonObject.toString(); // <-- use this one for validate.jsontest.com
//			String urlParameters = jsonObject.toString(); // <-- use this one for ptsv2.com
			byte[] postData = urlParameters.getBytes("UTF-8");
			int postDataLength = postData.length;
			conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
			conn.connect();

			// write the postdata to the connection.
			OutputStream osw = conn.getOutputStream();
			osw.write(postData);
			osw.flush();
			osw.close();

			// read the response
			InputStream in = new BufferedInputStream(conn.getInputStream());


			String result = convertStreamToString(in);
//			System.out.println("results on ptsv2: " + result);

			JSONObject jsonObjectres = new JSONObject(result);
			// System.out.println("results " + jsonObjectres);
			HashMap<String, Object> hashMap = new HashMap<>(Utility.jsonToMap(jsonObjectres));
			Iterator it = hashMap.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = (Map.Entry) it.next();
				System.out.println(pair.getKey() + " = " + pair.getValue());
				it.remove(); // avoids a ConcurrentModificationException
			}

			in.close();
			conn.disconnect();
		} finally {
		}
	}

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line).append('\n');
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}