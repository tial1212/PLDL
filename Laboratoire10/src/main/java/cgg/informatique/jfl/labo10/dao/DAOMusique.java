package cgg.informatique.jfl.labo10.dao;

import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.Musique;
import cgg.informatique.jfl.labo10.modeles.Token;

import java.util.logging.Logger;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;

@Singleton
@Lock(LockType.READ)
public class DAOMusique extends DAO {
  private static Logger LOGGER = Logger.getLogger(Demarrage.class.getName());

  /**
   * Create a song
   * To be valid :<br>
   * <ul>
   * 	<li>Must be linked to an existing User</li>
   * <li>User must be activated</li>
   * <li>song's title must respect policy</li>
   * <li>song's artist must respect policy</li>
   * </ul>
   * @param idProprietaire The ID of the owner.
   * @param titre The title of the song.
   * @param artiste The song's artist.
   * @param musique The song itself ( Base64 ).
   * @param vignette The cover art for the song ( Base64 ).
   * @param estPublique If the song is available for all.
   * @param estActive If the song is currently activated.
   * @return token
   */
  public static Token creerMusique(int idProprietaire, String titre, String artiste, String musique, String vignette, boolean estPublique, boolean estActive) {
    LOGGER.info("DAOMusique.creerMusique("+ idProprietaire+ "," + titre+ "," + artiste+ "," + musique+ "," + estPublique+ "," + estActive+")" );

    Musique musiqueCree = new Musique(idProprietaire, titre, artiste, musique, vignette, estPublique, estActive);
    musiqueCree.setDate();
    persister(musiqueCree);

    //verify that created object (in DB) are correct
    // delete all if error 
    if (!musiqueCree.getTitre().equals(titre)         || musiqueCree.getIdProprietaire() != idProprietaire
      || musiqueCree.estPublique() != estPublique || musiqueCree.estActive() != estActive)
    {
      enlever(Musique.class, musiqueCree.getId());
      Token token = new Token(false, "erreur création musique dans DB");
      LOGGER.warning("DAOMusique.creerMusique() ERROR : " + token.getAction());
      
      return token;
    }
    return new Token(true, "création ok");
  }

  /**
   * Modify a song
   * To be valid :<br>
   * <ul>
   * 	<li>Must be linked to an existing User</li>
   * <li>User must be activated</li>
   * <li>Song must exist</li>
   * <li>song's title must respect policy</li>
   * <li>song's artist must respect policy</li>
   * <li>song must belong to user</li>
   * </ul>
   * 
   * @param pIdSong The ID of the song
   * @param idProprietaire The ID of the owner.
   * @param titre The title of the song.
   * @param artiste The song's artist.
   * @param vignette The cover art for the song ( Base64 ).
   * @param estPublique If the song is available for all.
   * @param estActive If the song is currently activated.
   * @return token
   */
  public static Token modifierMusique(int idMusique, String titre, String artiste, String musique, String vignette, Boolean estPublique, Boolean estActive) {
    LOGGER.info("DAOMusique->modify(" + idMusique + "," + titre+ "," + artiste+ "," + estPublique+ "," + estActive + ")");

    Musique musiqueModifiee = trouverParId(Musique.class, idMusique);
    musiqueModifiee.setDate();
    if (titre != null)
      musiqueModifiee.setTitre(titre);
    if (artiste != null)
      musiqueModifiee.setArtiste(artiste);
    if (musique != null)
      musiqueModifiee.setMusique(musique);
    if (vignette != null)
      musiqueModifiee.setVignette(vignette);
    if (estPublique != null)
      musiqueModifiee.setPublique(estPublique);
    if (estActive != null)
      musiqueModifiee.setActive(estActive);
    
    Musique musiqueRetour = modifier(musiqueModifiee);

    // test if modification ok
    if (titre != null && !musiqueRetour.getTitre().equals(titre)
     || artiste != null && !musiqueRetour.getArtiste().equals(artiste)
     || musique != null && !musiqueRetour.getMusique().equals(musique)
     || vignette != null && !musiqueRetour.getVignette().equals(vignette)
     || estPublique != null && musiqueRetour.estPublique() != estPublique
     || estActive != null && musiqueRetour.estActive() != estActive) {
      Token token = new Token(false, "erreur modification utilisateur dans DB");
      LOGGER.info("DAOMusique.modifierMusique() ERROR : " + token.getAction());

      return token;
    }

    return new Token(true, "Modifications à la musique effectuées");
  }
}