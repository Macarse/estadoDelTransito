package com.edt.update;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edt.dbclasses.PMF;
import com.edt.dbclasses.Train;

public class TrainsUpdateServlet extends UpdateServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	private static final Pattern trainGroupPattern = Pattern.compile("<div[^>]*>\\s*<img[^>]*>\\s*TRENES[^<]*<[^>]*>\\s*<div[^>]*>(.*?)<div[^>]*class=\"infoTransito\"[^>]*>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	private static final Pattern trainPattern = Pattern.compile("<b>((?!L.nea)[^<]*)\\s*\\((?:TBA-?)?\\s*([^\\)]+)\\)</b>\\s*<[^>]*><b>[^<]*<[^>]*>[^<]*<span[^>]*>\\s*\\|\\s*</span>\\s*<span>\\s*Estado\\s*:\\s*</span[^>]*>\\s*<b>\\s*<font[^>]*>([^<]*)(?:</font>\\s*</b>\\s*<br>\\s*<span>[^<]*</span>\\s*<b>(.*?)<br>)?", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	
	private static final String trainsURL = HighwaysUpdateServlet.avenuesURL;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		String deleteAccessQuery = "select from " + Train.class.getName();
		
		List<Train> trainList = (List<Train>)pm.newQuery(deleteAccessQuery).execute();
		if(trainList != null && !trainList.isEmpty()) {
			for (Train train : trainList) {
				pm.deletePersistentAll(train);
					
			}
		}
		
		super.doGet(req, resp);
	}

	@Override
	protected void persist(HttpServletResponse resp, Matcher m, PersistenceManager pm, String type) {
		
		while(m.find()) {
			Train train = new Train();
			
			train.setName(m.group(1));
			train.setLine(m.group(2).replaceAll("\\.", "").replaceAll("Roca", "Gral Roca").replaceAll("Gral Gral", "Gral"));
			train.setStatus(m.group(3));

			if(m.group(4) != null) {
				train.setStatus(train.getStatus() + " " + m.group(4).replaceAll("<[^>]*>", "").replaceAll("\\s+", " "));						
			}
		
			pm.makePersistent(train);
		}
	}

	@Override
	protected String getUrl() {
		return trainsURL;
	}

	@Override
	protected String getCharset() {
		return "UTF8";
	}

	@Override
	protected boolean useProxy() {
		return true;
	}

	@Override
	protected List<Pattern> getPatterns() {
		ArrayList<Pattern> arrayList = new ArrayList<Pattern>();
		arrayList.add(trainGroupPattern);
		arrayList.add(trainPattern);
		return arrayList;
	}

	@Override
	protected List<String> getEntitiesName() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("Trains");
		return arrayList;
	}
	
}
