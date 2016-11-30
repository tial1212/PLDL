
package cgg.informatique.jfl.labo10.services;

import cgg.informatique.jfl.labo10.dao.DAO;
import cgg.informatique.jfl.labo10.dao.DAOToken;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.Token;
import cgg.informatique.jfl.labo10.modeles.Utilisateur;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.logging.Logger;


@Path("/service/token")
@Produces("application/json")
public class ServiceToken {

	@Inject
    private DAOToken daoToken;
    
	 @Inject
	 private DAO dao;
	
    Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
    
    
    @Path("/getActionToken")
	@PUT
	public Token getActionToken(@QueryParam("courriel")    String pEMail) {
		LOGGER.info("ServiceToken->getActionToken(" + pEMail +")" );
		
		//TODO querry user exist && isActive
		Utilisateur util = dao.querrySingle("SELECT u FROM Utilisateur u WHERE u.eMaill = :email", "email", pEMail );
		
		String message = "getActionToken";
		if (util == null || !util.isActive()) {
			message = ( !util.isActive() ?"Utilisateur non actif":"Utilisateur non existant");
			return new Token(false, message);
		} else {
			 Token token = Token.generateActionToken(pEMail);
			 daoToken.persistToken( token );
			 return token;
		}
	}
}
