
package cgg.informatique.jfl.labo10.services;

import cgg.informatique.jfl.labo10.dao.DAOToken;
import cgg.informatique.jfl.labo10.dao.DAOUtilisateur;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.Token;

import javax.inject.Inject;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
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
	 
    
    Logger LOGGER = Logger.getLogger(Demarrage.class.getName());

    @Path("/connect")
	@PUT
	public Token connect(@QueryParam("courriel")   String pCourriel,
	                   	 @QueryParam("motDePasse") String pMotDePasse) {
    	LOGGER.info("ServiceToken->connect("+ pCourriel + "," + pMotDePasse+")" );
    	 
	    boolean ok = doaUtil.login(pCourriel, pMotDePasse);
	    Token token =  new Token();
	    token.setEtat(ok);
	    
	    return token;
	}

	@Path("/createUser")
    @PUT
    public Token createUser(@QueryParam("alias") 	  String pAalias,
                       		@QueryParam("motDePasse") String pMotDePasse,
                       		@QueryParam("courriel")   String pCourriel) {
    	LOGGER.info("ServiceToken->createUser("+ pAalias + "," + pMotDePasse + "," + pCourriel+")" );
    	 
    	return doaUtil.creerUtilisateur(pAalias, pMotDePasse, pCourriel);
    }
    
    @Path("/confirmCreateUser")
	@PUT
	public Token confirmCreateUser(@QueryParam("idToken")    Long idToken,
	                               @QueryParam("captchaVal") String captchaVal,
	                               @QueryParam("courriel")   String pCourriel) {
		LOGGER.info("ServiceToken->confirmCreateUser(" + idToken + "," +captchaVal+","+pCourriel+")" );
		
		boolean ok = daoToken.activerUser(idToken, captchaVal , pCourriel);
		
		Token tokenRetour = new Token();
		tokenRetour.setEtat(ok);
		tokenRetour.setAction("confirmCreerRetour");
		
	  return tokenRetour;
	}
    
    
    /**
	@Path("/afficherListe")
    @GET
    public List<Token> afficherListe(@QueryParam("premier") 
     								 @DefaultValue("0") int pPremier,
                                     @QueryParam("dernier") 
    								 @DefaultValue("20") int pDernier) {
    	LOGGER.info("afficherListe("+pPremier+","+pDernier+")"  );
    	
    	return daoToken.afficherListe(pPremier, pDernier);
    }
    
    @Path("/afficher")
    @GET
    public Token afficher(@QueryParam("id") long pId) {
    	LOGGER.info("afficher("+pId+")" );
        return daoToken.rechercher(pId);
    }**/
}
