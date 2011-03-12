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

import com.edt.dbclasses.Access;
import com.edt.dbclasses.PMF;

public class HighwaysUpdateServlet extends UpdateServlet{

	private static final Pattern accessNamePattern = Pattern.compile("<div[^>]*+>\\s*<b[^>]*+>\\s*([^<]++)</b>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	private static final Pattern accessDirectionPattern = Pattern.compile("<div[^>]*class=\"[^\"]*transitoDescripcion[^\"]*+\"[^>]*+>\\s*([^<:]+)[^<]*<b[^>]*>\\s*([^<]++)(?:<font[^>]*>([^<]*)</font>)?\\s*</b>\\s*(?:<span[^>]*>\\s*<img[^>]*>\\s*<span[^>]*>\\s*([^<]++))?", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Pattern highwayGroupPattern = Pattern.compile("accesos\\s*a\\s*capital\\s*federal\\s*</div>(.*?)avenidas de", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	private static final Pattern avenueGroupPattern = Pattern.compile("avenidas\\s*de\\s*capital\\s*federal\\s*</div>(.*?)<div[^>]*id=\"tercera\"[^>]*>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	private static final Pattern accessGroupPattern = Pattern.compile("<div[^>]*class=\"itemAutos\"[^>]*>(.*?)(?=<div[^>]*class=\"itemAutos\"[^>]*>|$)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	
	public static final String avenuesURL = "http://servicios.lanacion.com.ar/informacion-general/transito";
	
	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		String deleteAccessQuery = "select from " + Access.class.getName();
		
		List<Access> accessList = (List<Access>)pm.newQuery(deleteAccessQuery).execute();
		if(accessList != null && !accessList.isEmpty()) {
			for (Access access : accessList) {
				pm.deletePersistentAll(access);
			}
		}

		super.doGet(req, resp);
	}
	
	@Override
	protected void persist(HttpServletResponse resp, Matcher m,
			PersistenceManager pm, String type){

		while (m.find()) {
			Access access = new Access();

			String accessGroup = m.group(1);
			Matcher matcher = accessNamePattern.matcher(accessGroup);
			if (matcher.find())
				access.setName(matcher.group(1));

			matcher = accessDirectionPattern.matcher(accessGroup);
			if (matcher.find()) {
				access.setDirectionFrom(matcher.group(1));
				access.setStatusFrom(matcher.group(2));
				access.setDelayFrom(matcher.group(3));
				access.setStatusMessageFrom(matcher.group(4));
				if (matcher.find()) {
					access.setDirectionTo(matcher.group(1));
					access.setStatusTo(matcher.group(2));
					access.setDelayTo(matcher.group(3));
					access.setStatusMessageTo(matcher.group(4));
				}
			}

			access.setType(type);
			pm.makePersistent(access);
		}
	}
	
	@Override
	protected String getUrl() {
		return avenuesURL;
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
		arrayList.add(highwayGroupPattern);
		arrayList.add(accessGroupPattern);
		arrayList.add(avenueGroupPattern);
		arrayList.add(accessGroupPattern);
		return arrayList;
	}

	@Override
	protected List<String> getEntitiesName() {
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add("Highway");
		arrayList.add("Avenue");
		return arrayList;
	}

}
