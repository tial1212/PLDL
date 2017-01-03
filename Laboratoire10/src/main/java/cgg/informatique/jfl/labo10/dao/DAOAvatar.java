package cgg.informatique.jfl.labo10.dao;

import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.Avatar;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@Singleton
@Lock(LockType.READ)
public class DAOAvatar extends DAO {
  private static Logger LOGGER = Logger.getLogger(Demarrage.class.getName());

  public static Avatar creerAvatar(String nom, String avatar) {
    LOGGER.info("DAOAvatar.creer(" + nom + ", " + avatar + ")");
    
    return persister(new Avatar(nom, avatar));
  }

  public static Avatar modifierAvatar(int id, String nom, String image) {
    LOGGER.info("DAOAvatar.modifier(" + id + ", " + nom + ", " + image + ")");
    Avatar avatarCree = trouverParId(Avatar.class, id);
    if (avatarCree != null) {
      avatarCree.setNom(nom);
      avatarCree.setImage(image);
    } else
      LOGGER.warning("DAOAvatar.modifier(...) Aucun avatar avec le \"id\" (" + id + ") trouvé");
    
    return modifier(avatarCree);
  }
}
