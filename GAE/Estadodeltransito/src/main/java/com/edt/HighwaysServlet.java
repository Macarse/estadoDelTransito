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

import com.edt.dbclasses.Access;
import com.edt.dbclasses.AccessEn;
import com.edt.dbclasses.PMF;
import com.google.gson.Gson;

public class HighwaysServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String lang = req.getParameter("lang");
		
		if(lang != null && lang.equals("en")) {
			respondEn(resp);
		} else {
			respondEs(resp);
		}
		
	}
	
	@SuppressWarnings("unchecked")
	private void respondEs(HttpServletResponse resp) throws IOException {
		Collection<Access> accessCollection = new ArrayList<Access>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "select from " + Access.class.getName() + " where type=='Highway'" + " order by name";
		
		List<Access> accessList = (List<Access>)pm.newQuery(query).execute();
		if(accessList != null && !accessList.isEmpty()) {
			for (Access access : accessList) {
				accessCollection.add(access);
			}
		}
		
		resp.getWriter().println(new Gson().toJson(accessCollection));
	}
	
	@SuppressWarnings("unchecked")
	private void respondEn(HttpServletResponse resp) throws IOException {
		Collection<AccessEn> accessCollection = new ArrayList<AccessEn>();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String query = "select from " + AccessEn.class.getName() + " where type=='Highway'" + " order by name";
		
		List<AccessEn> accessList = (List<AccessEn>)pm.newQuery(query).execute();
		if(accessList != null && !accessList.isEmpty()) {
			for (AccessEn access : accessList) {
				accessCollection.add(access);
			}
		}
		
		resp.getWriter().println(new Gson().toJson(accessCollection));
	}

}
