package cgg.informatique.jfl.labo10.services;

import cgg.informatique.jfl.labo10.dao.DAOMusique;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.ListeDeLecture;
import cgg.informatique.jfl.labo10.modeles.Musique;
import cgg.informatique.jfl.labo10.modeles.Token;
import cgg.informatique.jfl.labo10.modeles.Utilisateur;
import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.logging.Logger;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;


@Path("/service/musique")
@Produces("application/json")
public class ServiceMusique {
  private Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
  
  @Path("/creerListeLecture")
  @PUT
  public Token creerMusique(@QueryParam("idToken")  int idToken,
                            @QueryParam("cle")      String cle,
                            @QueryParam("titre")    String titre,
                            @QueryParam("artiste")  String artiste,
                            @QueryParam("musique")  String musique,
                            @QueryParam("vignette") String vignette,
                            @QueryParam("publique") boolean estPublique,
                            @QueryParam("active")   boolean estActive)
  {
    LOGGER.info("ServiceListeLecture->createPlaylist(" + idToken + ", " + cle + ", "
              + titre + ", " + artiste + ", " + musique + ", " + vignette + ", "
              + estPublique + ", " + estActive + ")"); 

    Token token = DAOMusique.trouverParId(Token.class, idToken).validerTokenAction(cle);

    if (!token.getEtat())
      return token;

    int idProprietaire = Utilisateur.getIdUtilisateur(token.getCourriel());

    if (!Musique.validerTitre(titre)) {
      token = new Token(false, "Titre non valide");
      LOGGER.warning("ServiceMusique.creerMusique() ERROR : " + token.getAction());

      return token;
    }
    
    if (!Musique.validerArtiste(artiste)) {
      token = new Token(false, "Artiste non valide");
      LOGGER.warning("ServiceMusique.creerMusique() ERROR : " + token.getAction());

      return token;
    }
    
    return DAOMusique.creerMusique(idProprietaire, titre, artiste, musique, vignette, estPublique , estActive);
  }
  
  @Path("/effacer")
  @DELETE
  public Token effacer(@QueryParam("idToken")   int idToken,
                       @QueryParam("cle")       String cle, 
                       @QueryParam("idMusique") int idMusique)
  {
    LOGGER.info("ServiceMusique.effacer(" + idToken + ", " + cle + ", " + idMusique + ")");
    
    Token token = DAOMusique.trouverParId(Token.class, idToken).validerTokenAction(cle);
    
    if (!token.getEtat())
      return token;
    
    token = DAOMusique.trouverParId(Musique.class, idMusique).validerModificationMusique(token);
    
    if (!token.getEtat())
      return token;
      
    DAOMusique.enlever(ListeDeLecture.class, idMusique);
    
    return new Token(true, "Musique effacée");
  }

  @Path("/getMusique")
  @POST
  public Musique getMusique(@QueryParam("idToken")   int idToken,
                            @QueryParam("cle")       String cle,
                            @QueryParam("idMusique") int idMusique)
  {
    LOGGER.info("ServiceListeLecture->modifier(" + idToken + ", " + cle + ", " + idMusique + ")");
    
    Token token = DAOMusique.trouverParId(Token.class, idToken).validerTokenAction(cle);
    
    if (!token.getEtat())
      return null;
    
    //playlist exist 
    Musique musique = DAOMusique.trouverParId(Musique.class, idMusique);
    
    token = musique.validerAccesMusique(token);
    
    if (!token.getEtat())
      return null;
    
    return musique;
  }

  @Path("/getToutesMusiques")
  @POST
  public List<Musique> getToutesMusiques(
    @QueryParam("idToken")  int idToken,
    @QueryParam("cle")      String cle,
    @QueryParam("publique") boolean inclusPublique,
    @QueryParam("privee")   boolean inclusPrivee,
    @QueryParam("premier")  int pFirst,
    @QueryParam("dernier")  int pLast)
  {
    LOGGER.info("ServiceListeLecture.getPublicPlaylistList(" + idToken + ", " + cle + ", "
                + inclusPublique + ", " + inclusPrivee + ", " + pFirst + ", " + pLast + ")");
    
    Token token = DAOMusique.trouverParId(Token.class, idToken).validerTokenAction(cle);
    
    if (!token.getEtat())
      return null;
    
    // music exist 
    List<Musique> toutesMusiques = DAOMusique.trouverParRequete(Musique.class,
      "SELECT m FROM Musique m", pFirst, pLast);
    
    Token tokenMusiquePriveeValide;
    for (int i = 0; i < toutesMusiques.size(); i++) {
      if (inclusPrivee) {
        tokenMusiquePriveeValide = toutesMusiques.get(i).validerAccesMusique(token);
        if (!tokenMusiquePriveeValide.getEtat())
          toutesMusiques.remove(i);
      }
      
      if (!inclusPublique && toutesMusiques.get(i).estPublique())
        toutesMusiques.remove(i);
      else if (!inclusPrivee && !toutesMusiques.get(i).estPublique())
        toutesMusiques.remove(i);
    }
    
    return toutesMusiques;
  }
  
  @Path("/getToutesMusiquesUtilisateur")
  @POST
  public List<Musique> getToutesMusiquesUtilisateur(
    @QueryParam("idToken")       int idToken,
    @QueryParam("cle")           String cle,
    @QueryParam("idUtilisateur") int idUtilisateur)
  {
    LOGGER.info("ServiceListeLecture.getToutesMusiquesUtilisateur(" + idToken + ", " + cle + ", " + idUtilisateur + ")");
    
    Token token = DAOMusique.trouverParId(Token.class, idToken).validerTokenAction(cle);
    
    if (!token.getEtat())
      return null;
    
    // music exist 
    List<Musique> toutesMusiques = DAOMusique.trouverParRequete(Musique.class,
      "SELECT m FROM Musique m WHERE Proprietaire = :proprietaire", "proprietaire", idUtilisateur);
    
    return toutesMusiques;
  }
  
  @Path("/modifier")
  @POST
  public Token modifier(@QueryParam("idToken")   int idToken,
                        @QueryParam("cle")       String cle,
                        @QueryParam("idMusique") int idMusique,
                        @QueryParam("titre")     String titre,
                        @QueryParam("artiste")   String artiste,
                        @QueryParam("musique")   String musique,
                        @QueryParam("vignette")  String vignette,
                        @QueryParam("publique")  boolean estPublique,
                        @QueryParam("active")    boolean estActive)
  {
    LOGGER.info("ServiceListeLecture.modifier(" + idToken + ", " + cle + ", "
              + idMusique + ", " + titre + ", " + artiste + ", " + musique + ", " + vignette + ", "
              + estPublique + ", " + estPublique + ")");
    
    Token token = DAOMusique.trouverParId(Token.class, idToken).validerTokenAction(cle);
    
    if (!token.getEtat())
      return token;
    
    token = DAOMusique.trouverParId(Musique.class, idMusique).validerModificationMusique(token);
    
    if (!token.getEtat())
      return token;

    // name ok if given
    if (!Musique.validerTitre(titre)) {
      token = new Token(false, "Titre incorrect");
      LOGGER.info("ServiceMusique.modifier() ERROR : " + token.getAction());

      return token;
    }
    
    // name ok if given
    if (!Musique.validerArtiste(artiste)) {
      token = new Token(false, "Titre incorrect");
      LOGGER.info("ServiceMusique.modifier() ERROR : " + token.getAction());

      return token;
    }
      
    return DAOMusique.modifierMusique(idMusique, titre, artiste, musique, vignette,
                                      estPublique, estActive);
  }

  @Path("/setMusiqueActive")
  @POST
  public Token setMusiqueActive(@QueryParam("idToken")   int idToken,
                                @QueryParam("cle")       String cle,
                                @QueryParam("idMusique") int idMusique,
                                @QueryParam("active")    boolean estActive)
  {
    Token token = DAOMusique.trouverParId(Token.class, idToken).validerTokenAction(cle);
    
    if (!token.getEtat())
      return token;
    
    token = DAOMusique.trouverParId(Musique.class, idMusique).validerModificationMusique(token);
    
    if (!token.getEtat())
      return token;
      
    return DAOMusique.modifierMusique(idMusique, null, null, null, null, null, estActive);
  }
  
  @Path("/setMusiquePublique")
  @POST
  public Token setMusiquePublique(@QueryParam("idToken") int idToken,
                                @QueryParam("cle")       String cle,
                                @QueryParam("idMusique") int idMusique,
                                @QueryParam("publique")  boolean estPublique)
  {
    Token token = DAOMusique.trouverParId(Token.class, idToken).validerTokenAction(cle);
    
    if (!token.getEtat())
      return token;
    
    token = DAOMusique.trouverParId(Musique.class, idMusique).validerModificationMusique(token);
    
    if (!token.getEtat())
      return token;
      
    return DAOMusique.modifierMusique(idMusique, null, null, null, null, estPublique, null);
  }
}