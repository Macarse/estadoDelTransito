package com.appspot.estadodeltransito.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class ParserUtils {

    public static String getUrlContent(String url) throws MalformedURLException,
    UnsupportedEncodingException, IOException {

        StringBuffer buffer = new StringBuffer();

        URL pageUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) pageUrl
                .openConnection();

        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_2) AppleWebKit/535.7 (KHTML, like Gecko) Chrome/16.0.912.75 Safari/535.7");
        InputStream input = connection.getInputStream();
        InputStream gzipInput = null;
        try {
            gzipInput = new GZIPInputStream(input);
        } catch (Exception e) {
        }

        BufferedReader in;

        if ( gzipInput != null ) {
            in = new BufferedReader(new InputStreamReader(gzipInput, "UTF-8"));
        } else {
            in = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        }

        String inputLine;

        while ((inputLine = in.readLine()) != null)
            buffer.append(inputLine);

        in.close();
        return buffer.toString();
    }
}
