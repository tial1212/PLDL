package cgg.informatique.jfl.labo10.services;

import cgg.informatique.jfl.labo10.dao.DAO;
import cgg.informatique.jfl.labo10.dao.DAOUtilisateur;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.Token;
import cgg.informatique.jfl.labo10.modeles.Utilisateur;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.logging.Logger;


@Path("/service/utilisateur")
@Produces("application/json")
public class ServiceUtilisateur {
  private Logger LOGGER = Logger.getLogger(Demarrage.class.getName());

  @Path("/creerUtilisateur")
  @PUT
  public Token creerUtilisateur(@QueryParam("alias") 	  String alias,
                                @QueryParam("motDePasse") String motDePasse,
                                @QueryParam("courriel")   String courriel,
                                @QueryParam("avatar")     int    idAvatar) {
    LOGGER.info("ServiceUtilisateur->createUser("+ alias + "," + motDePasse + "," + courriel + "," + idAvatar + ")" ); 

    // does't respect policy
    if (!Utilisateur.validerAlias(alias) || !Utilisateur.validerCourriel(courriel) || !Utilisateur.validerMotDePasse(motDePasse) ) {
      String action;
      action = (!Utilisateur.validerAlias(alias)        ? "Alias non compliant w/ policy! " :"");
      action = (!Utilisateur.validerMotDePasse(motDePasse) ? "Pswd non compliant w/ policy! "  :"");
      action = (!Utilisateur.validerCourriel(courriel) ? "EMail non compliant w/ policy! " :"");
      Token token = new Token(false, action);
      LOGGER.info("DAOUtilisateur->creerUtilisateur() ERROR : "+token.getAction() + " (Possible: Doesn't respect policy)");
      
      return token;
    }
    // courriel &&|| alias already used  &&|| Avatar.id non-existing
    boolean duplEmail   = (DAO.trouverParRequeteUnResultat(Utilisateur.class, "SELECT u FROM Utilisateur u WHERE u.eMaill = :courriel", "courriel", courriel) != null);
    boolean duplAlias   = (DAO.trouverParRequeteUnResultat(Utilisateur.class, "SELECT u FROM Utilisateur u WHERE u.alias = :alias", "alias", alias) != null);
    boolean AvatarExist = (DAO.trouverParRequeteUnResultat(Utilisateur.class, "SELECT a FROM Avatar a WHERE a.id = :avatar", "avatar", idAvatar) != null);
    if ( duplEmail || duplAlias || !AvatarExist) {
      Token token = new Token(false, (duplEmail ? "EMail already used!" : "")
                                   + (duplAlias ? "Alias already used!" : "")
                                   + (!AvatarExist ? "Avatar doesn't exist" : "") );
      LOGGER.info("DAOUtilisateur->creerUtilisateur() ERROR : " + token.getAction() + " (Possible: Email and/or Alias already used or Avatar non-existing)");
      
      return token;
    }

    return DAOUtilisateur.creerUtilisateur(alias, motDePasse, courriel, idAvatar);
  }
    
  @Path("/confirmerUtilisateur")
  @PUT
  public Token confirmerUtilisateur(@QueryParam("idToken")    int idToken,
                                    @QueryParam("captchaVal") String pCaptchaVal)
  {
    LOGGER.info("ServiceUtilisateur->confirmCreateUser(" + idToken + "," +pCaptchaVal+")" );
    
    return DAOUtilisateur.activerUtilisateur(idToken, pCaptchaVal);
  }

  @Path("/login")
  @PUT
  public Token login(@QueryParam("courriel")   String courriel,
                     @QueryParam("motDePasse") String motDePasse)
  {
    LOGGER.info("ServiceUtilisateur.login(" + courriel + "," + motDePasse + ")" );

    LOGGER.info("DAOUtilisateur.login(" + courriel + ", " + motDePasse + ")");
    String[] arrParams = new String[]{ "courriel", "password" };
    Object[] arrParamValues = new String[]{ courriel, motDePasse };
    Utilisateur utilisateur = DAO.trouverParRequeteUnResultat(Utilisateur.class,
                              "Select u FROM Utilisateur u WHERE u.eMaill = :courriel AND u.pasword = :password", arrParams, arrParamValues); 
    if (utilisateur != null) {
      if (utilisateur.isActive() ) {
        if (utilisateur.getMotDePasse().equals(motDePasse) ) {
          Token token = new Token(true, "login succeed");
          LOGGER.info("DAOUtilisateur.login() OK : "+token.getAction() );
          
          return token;
        }
        Token token = new Token(false, "login failed");
        LOGGER.info("DAOUtilisateur.login() ECHEC : user exist, wrong pswd" );
        
        return token;
      }
      Token token = new Token(false, "user not activated");
      LOGGER.info("DAOUtilisateur.login() ECHEC : user exist, but not activated" );
      
      return token;
    }
    Token token = new Token(false, "login failed");
    LOGGER.info("DAOUtilisateur.login() ECHEC : non existing user" );
    
    return token;
  }

  @Path("/logoff")
  @PUT
  public Token logoff(@QueryParam( "idToken")   int idToken,
                      @QueryParam("cle") 	String cle,
                      @QueryParam("courriel")   String courriel)
  {
    LOGGER.info("ServiceUtilisateur->logoff("+idToken+","+cle+","+courriel+")" );
    //TODO delete all token w/ user courriel is referenced

    Token token = DAO.trouverParId(Token.class, idToken);
    token = token.validerTokenAction(cle);
    
    if (token.getEtat()) {
      LOGGER.info("DAOUtilisateur->logoff(" + courriel + ")");

      Utilisateur utilisateur = DAO.trouverParRequeteUnResultat(Utilisateur.class, "SELECT u FROM Utilisateur u WHERE u.eMaill = :courriel", "courriel", courriel);
      if (utilisateur != null) {
        if (utilisateur.isActive()) {
          DAO.executerRequete(Token.class, "DELETE FROM Token t WHERE t.eMail = :courriel", "courriel", courriel);
          token = new Token(true, "logoff success");
          LOGGER.info("DAOUtilisateur->logoff() Success" );
          
          return token;
        }
        token = new Token(false, "user not activated");
        LOGGER.info("DAOUtilisateur->logoff() ECHEC : user exist, but not activated" );
        
        return token;
      }
      token = new Token(false, "user does not exist");
      LOGGER.info("DAOUtilisateur->logoff() ECHEC : non existing user" );
      
      return token;
    }
    
    return token;
  }

  @Path("/modifier")
  @POST
  public Token modifier(@QueryParam("idToken")    int idToken,
                        @QueryParam("cle") 	  String cle,
                        @QueryParam("idUtil")     int idUtilisateur,
                        @QueryParam("courriel")   String courriel,
                        @QueryParam("motDePasse") String motDePasse,
                        @QueryParam("alias")      String alias,
                        @QueryParam("avatar")     int idAvatar)
  {
    LOGGER.info("ServiceToken->modifier(" + idToken + ", " + cle + ", " + idUtilisateur + ", " + courriel + ", " + motDePasse + ", " + alias + ", " + idAvatar + ")");
    
    Token token = DAO.trouverParId(Token.class, idToken);
    token = token.validerTokenAction(cle);
    
    if (!token.getEtat())
      return token;
    
    Utilisateur utilisateur = DAO.trouverParId(Utilisateur.class, idUtilisateur);
    boolean okUtilisateurExist = utilisateur != null;
    boolean okAlias = Utilisateur.validerAlias(alias);
    boolean okPswd  = Utilisateur.validerMotDePasse(motDePasse);
    boolean okEmail = Utilisateur.validerCourriel(courriel);
    boolean okAliasAvailable = DAO.trouverParRequete(Utilisateur.class, "SELECT u FROM Utilisateur u WHERE u.alias = :alias", "alias", alias) == null;
    boolean okAvatar = true;

    if (!okUtilisateurExist || !okPswd || !okAlias || !okAliasAvailable || !okEmail || !okAvatar) {
      String action = (okUtilisateurExist ? "" : "Utilisateur inexistant!")
                    + (okPswd ? "" : "Pswd non conforme!")
                    + (okAlias ? "" : "Alias non conforme!")
                    + (okAliasAvailable ? "" : "Alias déjà pris!")
                    + (okEmail ? "" : "Email non conforme!")
                    + (okAvatar ? "" : "Avatar non existant!");
      LOGGER.info("DAOUtilisateur->modifier() ECHEC (Validation of fields : " + action + ")");

      token = new Token(false, action);
    } else
      token = DAOUtilisateur.modifier (idUtilisateur, alias, motDePasse, courriel, idAvatar);
      
    return token;
  }

  @Path("/effacer")
  @DELETE
  public Token effacer(@QueryParam("idToken") int idToken,
                       @QueryParam("cle")     String cle,
                       @QueryParam("idUser")  int idUtilisateur)
  {
    LOGGER.info("ServiceToken->effacer(" + idToken + ", " + cle + ", " + idUtilisateur + ")");
    
    Token token = DAO.trouverParId(Token.class, idToken);
    token = token.validerTokenAction(cle);
    
    if (token.getEtat()) {
      Utilisateur utilisateurDemande = DAO.trouverParRequeteUnResultat(Utilisateur.class,
                                       "SELECT u FROM Utilisateur u WHERE u.id = :id", "id", idUtilisateur);
      if (utilisateurDemande != null) {
        DAO.enlever(Utilisateur.class, utilisateurDemande.getId());
        
        return new Token(true, "supression utilisateur réussie");
      }
      token = new Token(false, "utilisateur demander non existant");
      LOGGER.info("DAOUtilisateur->activerUser() -> ECHEC : " + token.getAction());
      
      return token;
    }
    
    return token;
  }
  
  @Path("/getUtilisateur")
  @POST
  public Utilisateur getUtilisateur(@QueryParam("idToken")       int idToken,
                                    @QueryParam("cle")           String cle,
                                    @QueryParam("idUtilisateur") int idUtilisateur)
  {
    LOGGER.info("ServiceListeLecture.getUtilisateur(" + idToken + ", " + cle + ", " + idUtilisateur + ")");
    
    Token token = DAOUtilisateur.trouverParId(Token.class, idToken).validerTokenAction(cle);
    
    if (!token.getEtat())
      return null;
    
    return DAOUtilisateur.trouverParId(Utilisateur.class, idUtilisateur);
  }
}
