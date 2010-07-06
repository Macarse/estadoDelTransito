package com.edt.update.en;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edt.dbclasses.PMF;
import com.edt.dbclasses.Train;
import com.edt.update.UpdateServlet;

public class TrainsEnUpdateServlet extends UpdateServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		System.out.println(new Date() + " Updating Trains");
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		String deleteAccessQuery = "select from " + Train.class.getName();
		
		List<Train> trainList = (List<Train>)pm.newQuery(deleteAccessQuery).execute();
		if(trainList != null && !trainList.isEmpty()) {
			for (Train train : trainList) {
				pm.deletePersistentAll(train);
					
			}
		}
		
		updateTrains(resp, pm);
		System.out.println(new Date() + " Updated");
	}

}
