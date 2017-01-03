package cgg.informatique.jfl.labo10.dao;

import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.ListeDeLecture;
import cgg.informatique.jfl.labo10.modeles.ListeDeLecture_Musique;
import cgg.informatique.jfl.labo10.modeles.Token;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import java.util.logging.Logger;

@Singleton
@Lock(LockType.READ)
public class DAOListeLectureMusique extends DAO {
  Logger LOGGER = Logger.getLogger(Demarrage.class.getName()); 

  //TODO lots of methods
  // find song in playlist

  public static Token ajouterMusique(int idMusique, int idListeDeLecture) {
    persister(new ListeDeLecture_Musique(idMusique, idListeDeLecture));
    
    return new Token(true, "Ajout ok");
  }

  public static Token enleverMusique(int idMusique, int idListeDeLecture) {
    String[] parametres = new String[] { "liste", "musique" };
    Object[] valeursParametres = new Object[] { idListeDeLecture, idMusique};
    if (executerRequete(ListeDeLecture_Musique.class, "DELETE FROM ListesDelectureMusiques l WHERE l.playList = :liste AND l.song = :musique", parametres, valeursParametres) == 0)
      return new Token(false, "Suppression problème");
    else
      return new Token(true, "Suppression ok");
  }
}
