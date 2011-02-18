package com.edt.update;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edt.dbclasses.Access;
import com.edt.dbclasses.Subway;
import com.edt.dbclasses.Train;

public class UpdateServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final Pattern highwayGroupPattern = Pattern.compile("accesos\\s*a\\s*capital\\s*federal\\s*</div>(.*?)avenidas de", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	private static final Pattern avenueGroupPattern = Pattern.compile("avenidas\\s*de\\s*capital\\s*federal\\s*</div>(.*?)<div[^>]*id=\"tercera\"[^>]*>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	private static final Pattern accessGroupPattern = Pattern.compile("<div[^>]*class=\"itemAutos\"[^>]*>(.*?)(?=<div[^>]*class=\"itemAutos\"[^>]*>|$)", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	private static final Pattern accessNamePattern = Pattern.compile("<div[^>]*+>\\s*<b[^>]*+>\\s*([^<]++)</b>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	private static final Pattern accessDirectionPattern = Pattern.compile("<div[^>]*class=\"[^\"]*transitoDescripcion[^\"]*+\"[^>]*+>\\s*([^<:]+)[^<]*<b[^>]*>\\s*([^<]++)(?:<font[^>]*>([^<]*)</font>)?\\s*</b>\\s*(?:<span[^>]*>\\s*<img[^>]*>\\s*<span[^>]*>\\s*([^<]++))?", Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
	private static final Pattern subwayPattern = Pattern.compile("<b>([^:]*)\\s*:\\s*</b>\\s*&nbsp;\\s*([a-z‡Ž’—œ\\s]+)?(\\d+\\s+min\\.?\\s+\\d+\\s+seg\\.)?", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	private static final Pattern trainGroupPattern = Pattern.compile("<div[^>]*>\\s*<img[^>]*>\\s*TRENES[^<]*<[^>]*>\\s*<div[^>]*>(.*?)<div[^>]*class=\"infoTransito\"[^>]*>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	private static final Pattern trainPattern = Pattern.compile("class=\"itemTrenes\">\\s*?<b>(?!L.nea)\\s*?([^\\<]+)\\((?:TBA-?)?\\s*([^\\)]++)\\).*?Estado:\\s*?</span>\\s*?<b>\\s*?<font[^>]*>([^<]+)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
	}

	/*
	 * Subway update
	 */
	protected void updateSubways(HttpServletResponse resp, PersistenceManager pm) throws UnsupportedEncodingException, IOException {
		StringBuffer buffer = new StringBuffer();
		resp.setContentType("text/plain");

		URL lanacion = new URL("http://www.metrovias.com.ar/V2/InfoSubteSplash.asp");
		BufferedReader in = new BufferedReader(new InputStreamReader(lanacion.openStream(), "iso-8859-1"));

		String inputLine;

		while ((inputLine = in.readLine()) != null)
		    buffer.append(inputLine);

		in.close();
		Matcher subwayMatcher = subwayPattern.matcher(buffer.toString());
		persistSubway(resp, subwayMatcher, pm);
		
	}

	@SuppressWarnings("unchecked")
	private void persistSubway(HttpServletResponse resp, Matcher m,
			PersistenceManager pm) {
		
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

	/*
	 * Update Highways
	 */
	protected void updateAccessAndAvenues(HttpServletResponse resp, PersistenceManager pm)
			throws MalformedURLException, UnsupportedEncodingException,
			IOException {
		StringBuffer buffer = new StringBuffer();
		resp.setContentType("text/plain");

		URL lanacion = new URL("http://servicios.lanacion.com.ar/informacion-general/transito");
		BufferedReader in = new BufferedReader(new InputStreamReader(lanacion.openStream(), "iso-8859-1"));

		String inputLine;

		while ((inputLine = in.readLine()) != null)
		    buffer.append(inputLine);

		in.close();
		
		Matcher highwayGroupMatcher = highwayGroupPattern.matcher(buffer.toString());
		if (highwayGroupMatcher.find()) {
			String highwaysString = highwayGroupMatcher.group(1);
			Matcher accessGroupMatcher = accessGroupPattern.matcher(highwaysString.replaceAll("\u00A0", " "));
			persistAccess(resp, accessGroupMatcher, pm, "Highway");
		}
		
		Matcher avenueGroupMatcher = avenueGroupPattern.matcher(buffer.toString());
		if (avenueGroupMatcher.find()) {
			String avenueString = avenueGroupMatcher.group(1);
			Matcher accessGroupMatcher = accessGroupPattern.matcher(avenueString.replaceAll("\u00A0", " "));
			persistAccess(resp, accessGroupMatcher, pm, "Avenue");
		}
	}

	private void persistAccess(HttpServletResponse resp, Matcher m, PersistenceManager pm, String type)
			throws IOException {
		
		
		while(m.find()) {
			Access access = new Access();
			
			String accessGroup = m.group(1);
			Matcher matcher = accessNamePattern.matcher(accessGroup);
			if ( matcher.find() )
				access.setName(matcher.group(1));
			
			matcher = accessDirectionPattern.matcher(accessGroup);
			if ( matcher.find() ){
				access.setDirectionFrom(matcher.group(1));
				access.setStatusFrom(matcher.group(2));
				access.setDelayFrom(matcher.group(3));
				access.setStatusMessageFrom(matcher.group(4));
				if ( matcher.find() ){
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

	/*
	 * Train update
	 */
	protected void updateTrains(HttpServletResponse resp, PersistenceManager pm) throws UnsupportedEncodingException, IOException {
		StringBuffer buffer = new StringBuffer();
		resp.setContentType("text/plain");

		URL lanacion = new URL("http://www.lanacion.com.ar/informacion-general/transito/index.asp");
		BufferedReader in = new BufferedReader(new InputStreamReader(lanacion.openStream(), "iso-8859-1"));

		String inputLine;

		while ((inputLine = in.readLine()) != null)
		    buffer.append(inputLine);

		in.close();
		
		Matcher trainGroupMatcher = trainGroupPattern.matcher(buffer.toString());
		if (trainGroupMatcher.find()) {
			String highwaysString = trainGroupMatcher.group(1);
			Matcher trainMatcher = trainPattern.matcher(highwaysString.replaceAll("\u00A0", " "));
			persistTrain(resp, trainMatcher, pm);
		}
	}

	private void persistTrain(HttpServletResponse resp, Matcher m, PersistenceManager pm)
			throws IOException {
		
		while(m.find()) {
			Train train = new Train();
			
			train.setName(m.group(1));
			train.setLine(m.group(2).replaceAll("\\.", "").replaceAll("Roca", "Gral Roca").replaceAll("Gral Gral", "Gral"));
			train.setStatus(m.group(3));
			
			if(m.group(4) != null) {
				train.setStatus(train.getStatus() + " " + m.group(4).replaceAll("<[^>]*>", "").replaceAll("\\s+", " "));						
			}
		
			System.out.println(train);
			pm.makePersistent(train);
		}
	}
}
