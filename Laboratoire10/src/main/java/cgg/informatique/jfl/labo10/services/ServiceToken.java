
package cgg.informatique.jfl.labo10.services;

import cgg.informatique.jfl.labo10.dao.DAOToken;
import cgg.informatique.jfl.labo10.dao.DAOUtilisateur;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.Token;

import javax.inject.Inject;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;
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
	public Token connect(@QueryParam("courriel") String pCourriel,
	                   		@QueryParam("motDePasse") String pMotDePasse) {
		
		 
	    boolean ok = doaUtil.login(pCourriel, pMotDePasse);
	    
	    Token token =  new Token();
	    token.setEtat(ok);
	    
	    
	    return token;
	}

	@Path("/createUser")
    @PUT
    public Token createUser(@QueryParam("nom") String nom,
                       		@QueryParam("motDePasse") String motDePasse,
                       		@QueryParam("courriel") String courriel) {
    	LOGGER.info("TOKEN CREER Nom:" + nom + " Password:" + motDePasse + " Courriel:" + courriel);
    	 
        Token token =  doaUtil.creerUtilisateur(nom, motDePasse, courriel);
        Token tokenRetour = new Token();
        tokenRetour.setAction(token.getAction());
        tokenRetour.setEtat(token.getEtat() );
        tokenRetour.setId(token.getId() );
        
        
        return tokenRetour;
    }
    
    @Path("/confirmCreateUser")
	@PUT
	public Token confirmCreateUser(@QueryParam("idToken") Long idToken,
	                               @QueryParam("captchaVal") String captchaVal ) {
		LOGGER.info("UTILISATEUR CONFIRM CREER idToken:" + idToken + " captcha:" +captchaVal );
		
		boolean ok = daoToken.activerUser(idToken, captchaVal );
		
		Token tokenRetour = new Token();
		tokenRetour.setEtat(ok);
		tokenRetour.setAction("confirmCreerRetour");
		
	  return tokenRetour;
	}

	@Path("/afficherListe")
    @GET
    public List<Token> afficherListe(@QueryParam("premier") 
     								 @DefaultValue("0") int premier,
                                     @QueryParam("dernier") 
    								 @DefaultValue("20") int dernier) {
    	Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
    	LOGGER.info("UTILISATEUR AFFICHER LISTE");
    	List<Token> desToken = daoToken.afficherListe(premier, dernier);
    	
    	
        return desToken;
    }
    
    @Path("/afficher")
    @GET
    public Token afficher(@QueryParam("id") long id) {
    	LOGGER.info("message test");
        return daoToken.rechercher(id);
    }
}
