package cgg.informatique.jfl.labo10.services;

import cgg.informatique.jfl.labo10.dao.DAOListeLectureMusique;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.ListeDeLecture;
import cgg.informatique.jfl.labo10.modeles.Musique;
import cgg.informatique.jfl.labo10.modeles.Token;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.logging.Logger;
import javax.ws.rs.POST;

@Path("/service/listeLecture_Musique")
@Produces("application/json")
public class ServiceListeLectureMusique {
  private Logger LOGGER = Logger.getLogger(Demarrage.class.getName());

  @Path("/ajouterMusique")
  @PUT
  public Token ajouterMusique(@QueryParam("idToken")    int idToken,
                              @QueryParam("cle")        String cle,
                              @QueryParam("idPlaylist") int idListeLecture,
                              @QueryParam("idSong")     int idMusique)
  {
      LOGGER.info("ServiceListeLecture_Musiques,addSongToPlaylist(" + idToken + ", " + cle + ", "
                + idListeLecture+ ", " + idMusique + ")");

      Token token = DAOListeLectureMusique.trouverParId(Token.class, idToken).validerTokenAction(cle);
    
      if (!token.getEtat())
        return token;

      token = DAOListeLectureMusique.trouverParId(ListeDeLecture.class, idListeLecture).validerModificationListeLecture(token);

      if (!token.getEtat())
        return token;
      
      token = DAOListeLectureMusique.trouverParId(Musique.class, idMusique).validerAccesMusique(token);

      if (!token.getEtat())
        return token;

      return DAOListeLectureMusique.ajouterMusique(idMusique, idListeLecture);
  }

  @Path("/enleverMusique")
  @DELETE
  public Token enleverMusiquet(@QueryParam("idToken")    int idToken,
                               @QueryParam("cle")        String cle , 
                               @QueryParam("idPlaylist") int idListeLecture,
                               @QueryParam("idSong")     int idMusique)
  {
      LOGGER.info("ServiceListeLecture_Musiques.enleverMusique(" + idToken + ", " + cle + ", "
                + idListeLecture+ ", " + idMusique + ")");
      
      Token token = DAOListeLectureMusique.trouverParId(Token.class, idToken).validerTokenAction(cle);
    
      if (!token.getEtat())
        return token;

      token = DAOListeLectureMusique.trouverParId(ListeDeLecture.class, idListeLecture).validerModificationListeLecture(token);

      if (!token.getEtat())
        return token;

      return DAOListeLectureMusique.enleverMusique(idMusique, idListeLecture);
  }
  
  @Path("/getToutesMusiquesListeLecture")
  @POST
  public List<Musique> getToutesMusiquesListeLecture(
    @QueryParam("idToken")  int idToken,
    @QueryParam("cle")      String cle,
    @QueryParam("publique") int idListeLecture,
    @QueryParam("premier")  int pFirst,
    @QueryParam("dernier")  int pLast)
  {
    LOGGER.info("ServiceListeLecture.getPublicPlaylistList(" + idToken + ", " + cle + ", " + pFirst + ", " + pLast + ")");
    
    Token token = DAOListeLectureMusique.trouverParId(Token.class, idToken).validerTokenAction(cle);

    if (!token.getEtat())
      return null;

    token = DAOListeLectureMusique.trouverParId(ListeDeLecture.class, idListeLecture).validerAccesListeLecture(token);

    if (!token.getEtat())
      return null;
    
    // music exist 
    List<Musique> toutesMusiquesId = DAOListeLectureMusique.trouverParRequete(Musique.class,
      "SELECT l FROM ListeLecture_Musique l WHERE ListeDeLecture = :listeLecture", "listeLecture", idListeLecture);
    
    List<Musique> toutesMusiques = new ArrayList<Musique>();
    for (int i = 0; i < toutesMusiquesId.size(); i++) {
      toutesMusiques.add(DAOListeLectureMusique.trouverParRequeteUnResultat(Musique.class,
                         "SELECT m FROM Musique WHERE Id = :id", "id", toutesMusiquesId.get(i).getMusique()));
    }
    
    return toutesMusiques;
  }
}
