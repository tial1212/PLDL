package cgg.informatique.jfl.labo10.services;

import cgg.informatique.jfl.labo10.dao.DAOListeLecture;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.ListeDeLecture;
import cgg.informatique.jfl.labo10.modeles.Token;
import cgg.informatique.jfl.labo10.modeles.Utilisateur;

import javax.ejb.EJB;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import java.util.List;
import java.util.logging.Logger;

@Path("/service/listeLecture")
@Produces("application/json")
public class ServiceListeLecture {
  private Logger LOGGER = Logger.getLogger(Demarrage.class.getName());

  @Path("/creerListeLecture")
  @PUT
  public Token creerListeLecture(@QueryParam("idToken")  int idToken,
                                 @QueryParam("cle")      String cle,
                                 @QueryParam("nom")      String nom,
                                 @QueryParam("publique") boolean estPublique,
                                 @QueryParam("active")   boolean estActive)
  {
    LOGGER.info("ServiceListeLecture.creerListeLecture(" + idToken + ", " + cle + ", "
              + nom + ", " + estPublique + ", " + estActive + ")"); 

    Token token = DAOListeLecture.trouverParId(Token.class, idToken).validerTokenAction(cle);

    if (!token.getEtat())
      return token;

    int idProprietaire = Utilisateur.getIdUtilisateur(token.getCourriel());

    if (!ListeDeLecture.validerNom(nom)) {
      token = new Token(false, "Nom non valide");
      LOGGER.warning("ServiceListeLecture.creerListeLecture() ERROR : " + token.getAction());

      return token;
    }
    
    return DAOListeLecture.creerListeLecture(idProprietaire, nom, estPublique , estActive);
  }
  
  @Path("/effacer")
  @DELETE
  public Token effacer(@QueryParam("idToken")    int idToken,
                       @QueryParam("cle")        String cle, 
                       @QueryParam("idListeLecture") int idListeLecture)
  {
    LOGGER.info("ServiceListeLecture.effacer(" + idToken + ", " + cle + ", " + idListeLecture + ")");
    
    Token token = DAOListeLecture.trouverParId(Token.class, idToken).validerTokenAction(cle);
    
    if (!token.getEtat())
      return token;
    
    token = DAOListeLecture.trouverParId(ListeDeLecture.class, idListeLecture).validerModificationListeLecture(token);
    
    if (!token.getEtat())
      return token;
      
    DAOListeLecture.enlever(ListeDeLecture.class, idListeLecture);
    
    return new Token(true, "Liste de lecture effacée");
  }

  @Path("/getListeLecture")
  @POST
  public ListeDeLecture getListeLecture(@QueryParam("idToken")        int idToken,
                                        @QueryParam("cle")            String cle,
                                        @QueryParam("idListeLecture") int idListeLecture)
  {
    LOGGER.info("ServiceListeLecture.getListeLecture(" + idToken + ", " + cle + ", " + idListeLecture + ")");
    
    Token token = DAOListeLecture.trouverParId(Token.class, idToken).validerTokenAction(cle);
    
    if (!token.getEtat())
      return null;
    
    // playlist exist 
    ListeDeLecture listeLecture = DAOListeLecture.trouverParId(ListeDeLecture.class, idListeLecture);
    
    token = DAOListeLecture.trouverParId(ListeDeLecture.class, idListeLecture).validerAccesListeLecture(token);
    
    if (!token.getEtat())
      return null;
    
    return listeLecture;
  }

  @Path("/getToutesListesLecture")
  @POST
  public List<ListeDeLecture> getToutesListesLecture(
    @QueryParam("idToken")  int idToken,
    @QueryParam("cle")      String cle,
    @QueryParam("publique") boolean inclusPublique,
    @QueryParam("privee")   boolean inclusPrivee,
    @QueryParam("premier")  int pFirst,
    @QueryParam("dernier")  int pLast)
  {
    LOGGER.info("ServiceListeLecture.getToutesListesLecture(" + idToken + ", " + cle + ", " + pFirst + ", " + pLast + ")");
    
    Token token = DAOListeLecture.trouverParId(Token.class, idToken).validerTokenAction(cle);
    
    if (!token.getEtat())
      return null;
    
    // playlist exist
    List<ListeDeLecture> toutesListesLecture = DAOListeLecture.trouverParRequete(ListeDeLecture.class,
      "SELECT l FROM ListeDeLecture l", pFirst, pLast);
    
    Token tokenListeLecturePriveeValide;
    for (int i = 0; i < toutesListesLecture.size(); i++) {
      if (inclusPrivee) {
        tokenListeLecturePriveeValide = toutesListesLecture.get(i).validerAccesListeLecture(token);
        if (!tokenListeLecturePriveeValide.getEtat())
          toutesListesLecture.remove(i);
      }
      
      if (!inclusPublique && toutesListesLecture.get(i).estPublique())
        toutesListesLecture.remove(i);
      else if (!inclusPrivee && !toutesListesLecture.get(i).estPublique())
        toutesListesLecture.remove(i);
    }
    
    return toutesListesLecture;
  }
  
  @Path("/getToutesListesLectureUtilisateur")
  @POST
  public List<ListeDeLecture> getToutesListesLectureUtilisateur(
    @QueryParam("idToken")       int idToken,
    @QueryParam("cle")           String cle,
    @QueryParam("idUtilisateur") int idUtilisateur)
  {
    LOGGER.info("ServiceListeLecture.getToutesMusiquesUtilisateur(" + idToken + ", " + cle + ", " + idUtilisateur + ")");
    
    Token token = DAOListeLecture.trouverParId(Token.class, idToken).validerTokenAction(cle);
    
    if (!token.getEtat())
      return null;
    
    // Liste de lecture existe 
    List<ListeDeLecture> toutesListesLecture = DAOListeLecture.trouverParRequete(ListeDeLecture.class,
      "SELECT l FROM ListeDeLecture m WHERE Proprietaire = :proprietaire", "proprietaire", idUtilisateur);
    
    return toutesListesLecture;
  }
  
  @Path("/modifier")
  @POST
  public Token modifier(@QueryParam("idToken")        int idToken,
                        @QueryParam("cle")            String cle,
                        @QueryParam("idListeLecture") int idListeLecture,
                        @QueryParam("nom")            String nom,
                        @QueryParam("publique")       boolean estPublique,
                        @QueryParam("active")         boolean estActive )
  {
    LOGGER.info("ServiceListeLecture.modifier(" + idToken + ", " + cle + ", "
              + idListeLecture+ ", " + nom + ", " + estPublique + ", " + estPublique + ")");
    
    Token token = DAOListeLecture.trouverParId(Token.class, idToken).validerTokenAction(cle);
    
    if (!token.getEtat())
      return token;
    
    token = DAOListeLecture.trouverParId(ListeDeLecture.class, idListeLecture).validerModificationListeLecture(token);
    
    if (!token.getEtat())
      return token;

    //name ok if given
    if (!ListeDeLecture.validerNom(nom)) {
      token = new Token(false, "Nom incorrect");
      LOGGER.info("ServiceListeLecture.modifier() ERROR : " + token.getAction());

      return token;
    }
      
    return DAOListeLecture.modifierListeLecture(idListeLecture, nom, estPublique, estActive);
  }

  @Path("/setListeLectureNom")
  @POST
  public Token setListeLectureNom(@QueryParam("idToken")        int idToken,
                                  @QueryParam("cle")            String cle,
                                  @QueryParam("idListeLecture") int idListeLecture,
                                  @QueryParam("nom")            String nom)
  {
    Token token = DAOListeLecture.trouverParId(Token.class, idToken).validerTokenAction(cle);
    
    if (!token.getEtat())
      return token;
    
    token = DAOListeLecture.trouverParId(ListeDeLecture.class, idListeLecture).validerModificationListeLecture(token);
    
    if (!token.getEtat())
      return token;

    //name ok if given
    if (!ListeDeLecture.validerNom(nom)) {
      token = new Token(false, "Nom incorrect");
      LOGGER.info("ServiceListeLecture.setListeLectureNom() ERROR : " + token.getAction());

      return token;
    }
      
    return DAOListeLecture.modifierListeLecture(idListeLecture, nom, null, null);
  }

  @Path("/setListeLectureActive")
  @POST
  public Token setListeLectureActive(@QueryParam("idToken")        int idToken,
                                     @QueryParam("cle")            String cle,
                                     @QueryParam("idListeLecture") int idListeLecture,
                                     @QueryParam("active")         boolean estActive)
  {
    Token token = DAOListeLecture.trouverParId(Token.class, idToken).validerTokenAction(cle);
    
    if (!token.getEtat())
      return token;
    
    token = DAOListeLecture.trouverParId(ListeDeLecture.class, idListeLecture).validerModificationListeLecture(token);
    
    if (!token.getEtat())
      return token;
      
    return DAOListeLecture.modifierListeLecture(idListeLecture, null, null, estActive);
  }


  @Path("/setListeLecturePublique")
  @POST
  public Token setListeLecturePublique(@QueryParam( "idToken")       int idToken,
                                       @QueryParam("cle")            String cle,
                                       @QueryParam("idListeLecture") int idListeLecture,
                                       @QueryParam("publique")       boolean estPublique)
  {
    Token token = DAOListeLecture.trouverParId(Token.class, idToken).validerTokenAction(cle);
    
    if (!token.getEtat())
      return token;
    
    token = DAOListeLecture.trouverParId(ListeDeLecture.class, idListeLecture).validerModificationListeLecture(token);
    
    if (!token.getEtat())
      return token;
      
    return DAOListeLecture.modifierListeLecture(idListeLecture, null, estPublique, null);
  }
}
