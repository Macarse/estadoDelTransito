package com.edt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edt.dbclasses.PMF;
import com.edt.dbclasses.Subway;
import com.edt.dbclasses.SubwayEn;
import com.google.gson.Gson;

public class SubwaysServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String lang = req.getParameter("lang");
		
		if(lang != null && lang.equals("en")) {
			respondEs(resp);
		} else {
			respondEs(resp);
		}

	}
	@SuppressWarnings("unchecked")
	private void respondEs(HttpServletResponse resp) throws IOException {
		Collection<Subway> subwayCollection = new ArrayList<Subway>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "select from " + Subway.class.getName() + " order by name";
		System.out.println(query);
		List<Subway> subwayList = (List<Subway>)pm.newQuery(query).execute();
		if(subwayList != null && !subwayList.isEmpty()) {
			for (Subway access : subwayList) {
				subwayCollection.add(access);
			}
		}
		
		resp.getWriter().println(new Gson().toJson(subwayCollection));
	}
	
	@SuppressWarnings("unchecked")
	private void respondEn(HttpServletResponse resp) throws IOException {
		Collection<SubwayEn> subwayCollection = new ArrayList<SubwayEn>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "select from " + SubwayEn.class.getName() + " order by name";
		System.out.println(query);
		List<SubwayEn> subwayList = (List<SubwayEn>)pm.newQuery(query).execute();
		if(subwayList != null && !subwayList.isEmpty()) {
			for (SubwayEn access : subwayList) {
				subwayCollection.add(access);
			}
		}
		
		resp.getWriter().println(new Gson().toJson(subwayCollection));
	}

}
