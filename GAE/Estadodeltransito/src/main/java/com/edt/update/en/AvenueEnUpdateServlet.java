package com.edt.update.en;

import java.io.IOException;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.edt.dbclasses.Access;
import com.edt.dbclasses.AccessEn;
import com.edt.dbclasses.PMF;
import com.google.api.translate.Language;
import com.google.api.translate.Translate;

@SuppressWarnings("serial")
public class AvenueEnUpdateServlet extends HttpServlet{

	@SuppressWarnings("unchecked")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		String dataQuery = "select from " + Access.class.getName() + " where type=='Avenue'";
		String deleteQuery = "select from " + AccessEn.class.getName() + " where type=='Avenue'";
		
		List<AccessEn> deleteAccessList = (List<AccessEn>)pm.newQuery(deleteQuery).execute();
		pm.deletePersistentAll(deleteAccessList);
		
		
		Translate.setHttpReferrer("http://no-url.com");
		
		List<Access> accessList = (List<Access>)pm.newQuery(dataQuery).execute();
		for (Access access : accessList) {
			try {
				AccessEn newAccess = new AccessEn();
				
				newAccess.setName(access.getName());
				newAccess.setType(access.getType());
				
				if(access.getDirectionTo() != null) {
					newAccess.setDirectionTo(
							access.getDirectionTo().replaceAll("Norte", "North").replaceAll("Provincia", "Province")
													.replaceAll("Hacia", "To").replaceAll("Centro", "Downtown")
													.replaceAll("Sur", "South"));
				}
				
				newAccess.setDirectionFrom(
						access.getDirectionFrom().replaceAll("Norte", "North").replaceAll("Provincia", "Province")
												.replaceAll("Hacia", "To").replaceAll("Centro", "Downtown")
												.replaceAll("Sur", "South"));
				
				if(access.getStatusTo() != null) {
					newAccess.setStatusTo(
							access.getStatusTo().replaceAll("Demora", "Delayed").replaceAll("Congestionado", "Congested")
												.replaceAll("Interrumpido", "Interrupted"));
				}
				
				newAccess.setStatusFrom(
						access.getStatusFrom().replaceAll("Demora", "Delayed").replaceAll("Congestionado", "Congested")
											.replaceAll("Interrumpido", "Interrupted"));
				
				if(access.getStatusMessageFrom() != null) {
					System.out.println(access.getStatusMessageFrom());
					newAccess.setStatusMessageFrom(Translate.execute(access.getStatusMessageFrom(), Language.SPANISH, Language.ENGLISH).replaceAll("Slaughter","Mataderos").replaceAll("calzada", "roadway"));
					System.out.println(newAccess.getStatusMessageFrom());
				}
				if(access.getStatusMessageTo() != null) {
					System.out.println(access.getStatusMessageTo());
					newAccess.setStatusMessageTo(Translate.execute(access.getStatusMessageTo(), Language.SPANISH, Language.ENGLISH).replaceAll("Slaughter","Mataderos").replaceAll("calzada", "roadway"));
					System.out.println(newAccess.getStatusMessageTo());
				}
				
				newAccess.setDelayFrom(access.getDelayFrom());
				newAccess.setDelayTo(access.getDelayTo());
				
				pm.makePersistent(newAccess);
			} catch (Exception e) {
				e.printStackTrace();
			}	
			
		}
	}
	
}
