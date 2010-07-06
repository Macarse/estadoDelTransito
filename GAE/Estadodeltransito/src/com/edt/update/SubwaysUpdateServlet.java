package com.edt.update;

import java.io.IOException;
import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edt.dbclasses.PMF;

public class SubwaysUpdateServlet extends UpdateServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println(new Date() + " Updating Subways");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		updateSubways(resp, pm);
		System.out.println(new Date() + " Updated");
	}

}
