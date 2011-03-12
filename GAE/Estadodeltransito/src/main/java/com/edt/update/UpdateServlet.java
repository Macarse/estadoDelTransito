package com.edt.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edt.dbclasses.PMF;

abstract public class UpdateServlet extends HttpServlet{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String proxyURL = "http://edt.rocketzone.net/test.php?url=";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println(new Date() + " Updating "+getFormattedEntitiesName());
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		update(resp, pm);
		System.out.println(new Date() + " Updated");
	}

	private String getFormattedEntitiesName() {
		StringBuffer sb = new StringBuffer();
		for(String name:getEntitiesName())
			sb.append(name).append(" & ");
		return sb.toString();
	}

	protected void update(HttpServletResponse resp, PersistenceManager pm) throws UnsupportedEncodingException, IOException{
		resp.setContentType("text/plain");

		String urlContent = getUrlContent(getUrl(), getCharset(), useProxy());
		
		List<Pattern> patterns = getPatterns();
		if ( patterns.isEmpty() )
			return;

		Matcher matcher = patterns.get(0).matcher(urlContent);
		
		if ( patterns.size() == 1 )
			persist(resp, matcher, pm, getEntitiesName().get(0));
		else{
			for(int i=0; i < patterns.size(); ){
				matcher = patterns.get(i).matcher(urlContent);
				if (matcher.find()) {
					String groupString = matcher.group(1);
					Matcher groupMatcher = patterns.get(i+1).matcher(groupString.replaceAll("\u00A0", " "));
					persist(resp, groupMatcher, pm, getEntitiesName().get(i/2));
				}
				i+=2;
			}
		}

	}

	private String getUrlContent(String url, String charset, boolean useProxy) throws MalformedURLException, UnsupportedEncodingException,
			IOException {
		StringBuffer buffer = new StringBuffer();
		
		if ( charset == null )
			charset = "UTF8";
		
		if ( useProxy )
			url = proxyURL + url;
		
		URL pageUrl = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) pageUrl.openConnection();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), charset));

		String inputLine;

		while ((inputLine = in.readLine()) != null)
		    buffer.append(inputLine);

		in.close();
		return buffer.toString();
	}

	abstract protected void persist(HttpServletResponse resp, Matcher m, PersistenceManager pm, String type);

	abstract protected String getUrl();
	abstract protected String getCharset();
	abstract protected boolean useProxy();
	
	abstract protected List<Pattern> getPatterns();
	abstract protected List<String> getEntitiesName();
	
}
