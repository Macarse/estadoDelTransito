package com.edt.update;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edt.dbclasses.Access;
import com.edt.dbclasses.PMF;

public class HighwaysUpdateServlet extends UpdateServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
		System.out.println(new Date() + " Updating Highways");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		String deleteAccessQuery = "select from " + Access.class.getName();
		
		List<Access> accessList = (List<Access>)pm.newQuery(deleteAccessQuery).execute();
		if(accessList != null && !accessList.isEmpty()) {
			for (Access access : accessList) {
				pm.deletePersistentAll(access);
			}
		}
		
		updateAccessAndAvenues(resp, pm);
		System.out.println(new Date() + " Updated");
	}	
}
