
package cgg.informatique.jfl.labo10.services;

import cgg.informatique.jfl.labo10.dao.DAO;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.Token;
import cgg.informatique.jfl.labo10.modeles.Utilisateur;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.logging.Logger;


@Path("/service/token")
@Produces("application/json")
public class ServiceToken {
  Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
    
  @Inject
  private DAO dao;
  
  private EntityManager em;
    
  @Path("/getTokenAction")
  @PUT
  public Token getTokenAction(@QueryParam("courriel") String email) {
    LOGGER.info("ServiceToken.getTokenAction(" + email + ")");

    //TODO querry user exist && isActive
    Utilisateur util = (Utilisateur) em.createQuery("SELECT u FROM utilisateur u WHERE u.Courriel LIKE :email " )
            .setParameter("email", email).getResultList();


    String message = "getActionToken";
    if (util == null) {
      message = "Utilisateur non existant";
      return new Token(false, message);
    } else if (!util.isActive()) {
      message = ("Utilisateur non actif");
      return new Token(false, message);
    } else {
      Token token = Token.genererTokenAction(email);
      dao.persister(token);
      return token;
    }
  }
}
