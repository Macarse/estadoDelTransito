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
import com.edt.dbclasses.Train;
import com.edt.dbclasses.TrainEn;
import com.google.gson.Gson;

public class TrainsServlet extends HttpServlet{

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
		Collection<Train> trainCollection = new ArrayList<Train>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "select from " + Train.class.getName() + " order by line";
		System.out.println(query);
		List<Train> trainList = (List<Train>)pm.newQuery(query).execute();
		if(trainList != null && !trainList.isEmpty()) {
			for (Train access : trainList) {
				trainCollection.add(access);
			}
		}
		
		resp.getWriter().println(new Gson().toJson(trainCollection));		
	}
	
	@SuppressWarnings("unchecked")
	private void respondEn(HttpServletResponse resp) throws IOException {
		Collection<TrainEn> trainCollection = new ArrayList<TrainEn>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "select from " + TrainEn.class.getName() + " order by line";
		System.out.println(query);
		List<TrainEn> trainList = (List<TrainEn>)pm.newQuery(query).execute();
		if(trainList != null && !trainList.isEmpty()) {
			for (TrainEn access : trainList) {
				trainCollection.add(access);
			}
		}
		
		resp.getWriter().println(new Gson().toJson(trainCollection));	
	}
	
}
