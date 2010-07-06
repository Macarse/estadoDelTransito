package com.appspot.estadodeltransito.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import android.util.Log;

public class Request {

	static public String getJson(String tag, String gaeUrl) {
		return getJson(tag,gaeUrl,"ISO-8859-1");
	}

	static public String getJson(String tag, String gaeUrl, String charset) {

		String json = "";

		try {
			URL url = new java.net.URL(gaeUrl);

			InputStream in = url.openStream();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in, charset));

			StringBuilder str = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				str.append(line);
			}
			in.close();
			
			json = str.toString();
		} catch (Exception e) {
			Log.e(tag, "Error from server. "+ e);
			// Nothing to do here.
		}

		return json;
	}
}
