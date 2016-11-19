
package cgg.informatique.jfl.labo10.services;

import cgg.informatique.jfl.labo10.dao.DAO;
import cgg.informatique.jfl.labo10.dao.DAOToken;
import cgg.informatique.jfl.labo10.dao.DAOUtilisateur;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.Token;
import cgg.informatique.jfl.labo10.modeles.Utilisateur;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.logging.Logger;


@Path("/service/token")
@Produces({ "application/json" , "text/xml",})
public class ServiceToken {

	@Inject
    private DAOToken daoToken;
    
	 @Inject
    private DAOUtilisateur doaUtil;
	
	 @Inject
	 private DAO dao;
	
	 
    Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
    
    
    
    @Path("/logoff")
	@PUT
	public Token logoff(@PathParam("idToken")      long   pIdToken,
            				@QueryParam("cle") 	      String pKey,
            				@QueryParam("courriel")   String pCourriel) {
    	LOGGER.info("ServiceToken->disconnect("+pIdToken+","+pKey+","+pCourriel+")" );
    	//TODO
	    return new Token();
	}
    
    
    
    
    @Path("/getActionToken")
	@PUT
	public Token getActionToken(@QueryParam("courriel")    String pEMail) {
		LOGGER.info("ServiceToken->getActionToken(" + pEMail +")" );
		
		//TODO querry user exist && isActive
		Utilisateur util = dao.querrySingle("SELECT u FROM Utilisateur u WHERE u.Courriel = "+pEMail );
		
		String message = "getActionToken";
		if (util == null || !util.isActive()) {
			message = ( !util.isActive() ?"Utilisateur non actif":"Utilisateur non existant");
			return new Token(false, message);
		}else {
			 Token token = new Token(true, message , Token.generateRdmSalt() );
			 return daoToken.persistToken(token);
		}
		
	}
}
