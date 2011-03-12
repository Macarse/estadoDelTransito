package com.edt.update;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServletResponse;

import com.edt.dbclasses.Subway;

public class SubwaysUpdateServlet extends UpdateServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	private static final Pattern subwayPattern = Pattern.compile("<b>([^:]*)\\s*:\\s*</b>\\s*&nbsp;\\s*([a-z‡Ž’—œ\\s]+)?(\\d+\\s+min\\.?\\s+\\d+\\s+seg\\.)?", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	
	public static final String subwayURL = "http://www.metrovias.com.ar/V2/InfoSubteSplash.asp";
	
	@SuppressWarnings("unchecked")
	@Override
	protected void persist(HttpServletResponse resp, Matcher m, PersistenceManager pm, String type) {
		if(m.find()) {
			String deleteSubwayQuery = "select from " + Subway.class.getName();
			
			List<Subway> subwayList = (List<Subway>)pm.newQuery(deleteSubwayQuery).execute();
			if(subwayList != null && !subwayList.isEmpty()) {
				for (Subway subway : subwayList) {
					pm.deletePersistentAll(subway);
				}
			}
			
			Subway subway = new Subway();
			
			subway.setName(m.group(1));
			
			if(m.group(2) == null || m.group(2).isEmpty()) {
				subway.setStatus("Estado: normal");
			} else {
				subway.setStatus(m.group(2));
			}
			
			if(m.group(3) == null || m.group(3).isEmpty()) {
				subway.setFrequency("Servicio interrumpido");
			} else {
				subway.setFrequency("Trenes cada " + m.group(3));
			}
			
			pm.makePersistent(subway);
			
		}
		
		while(m.find()) {
			Subway subway = new Subway();
			
			subway.setName(m.group(1));
			
			if(m.group(2) == null || m.group(2).isEmpty()) {
				subway.setStatus("Estado: normal");
			} else {
				subway.setStatus(m.group(2));
			}
			
			if(m.group(3) == null || m.group(3).isEmpty()) {
				subway.setFrequency("Servicio interrumpido");
			} else {
				subway.setFrequency("Trenes cada " + m.group(3));
			}
			
			pm.makePersistent(subway);
		}
	}

	@Override
	protected String getUrl() {
		return subwayURL;
	}

	@Override
	protected String getCharset() {
		return "iso-8859-1";
	}

	@Override
	protected boolean useProxy() {
		return false;
	}

	@Override
	protected List<Pattern> getPatterns() {
		ArrayList<Pattern> arrayList = new ArrayList<Pattern>();
		arrayList.add(subwayPattern);
		return arrayList;
	}

	@Override
	protected List<String> getEntitiesName() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("Subways");
		return arrayList;
	}
	
}
