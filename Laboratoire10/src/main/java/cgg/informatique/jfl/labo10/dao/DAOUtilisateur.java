package cgg.informatique.jfl.labo10.dao;

import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.Token;
import cgg.informatique.jfl.labo10.modeles.Utilisateur;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

import java.util.logging.Logger;

@Singleton
@Lock(LockType.READ)
public class DAOUtilisateur extends DAO {
  private static Logger LOGGER = Logger.getLogger(Demarrage.class.getName()); 

  /**
   * Create an user
   * To be valid :<br>
   * <ul>
   * 	<li>Utilisateur's param must respect policy</li>
   * 	<li>Email must be available</li>
   * 	<li>Alias must be available</li>
   *  <li>Avatar must exist</li>
   * </ul>
   * 
   * @param alias The displayed name of the user (NickName)
   * @param pCourriel The E-mail of the user
   * @param motDePasse The pasword (MD5 encrypted ) of the user.
   * @param idAvatar The ID of the selected Avatar.
   * @return token
   */
  public static Token creerUtilisateur(String alias, String motDePasse, String pCourriel,  int idAvatar) {
    LOGGER.info("DAOUtilisateur->creerUtilisateur("+alias+","+ motDePasse+","+pCourriel+","+idAvatar+")");

    // create objects
    Token token = Token.genererTokenConfirmation(pCourriel );
    Utilisateur utilisateurCree = new Utilisateur(alias, pCourriel, motDePasse, idAvatar);
    
    //persist objects
    token = DAO.persister(token);
    utilisateurCree = DAO.persister(utilisateurCree);

    //verify that created object (in DB) are correct
    // delete all if error 
    if ( token.getEtat() != true
     || !utilisateurCree.getCourriel().equals(pCourriel)
     ||  utilisateurCree.getAvatar() != idAvatar
     || !utilisateurCree.getAlias().equals(alias)
     || !utilisateurCree.getMotDePasse().equals(motDePasse))
    {
      enlever(Utilisateur.class, utilisateurCree.getId());
      token = new Token(false, "erreur création user dans DB");
      LOGGER.info("DAOUtilisateur.creerUtilisateur() ERROR : " + token.getAction());
      
      return token;
    }

    return token;
  }

  /**
   * Activate an user 
   * <ul><li>Token and Utilisateur must exist</li>
   * <li>captcha must match.</li></ul>
   * @param idToken The ID of the token that requested action
   * @param captcha The value of the captcha.
   * @return token
   */
  public static Token activerUtilisateur(int idToken, String captcha ) {
    LOGGER.info("DAOUtilisateur.activerUtilisateur("+idToken+","+captcha+")");

    Token token = DAO.trouverParId(Token.class, idToken);

    if (token != null && token.getCaptchaStr().equals(captcha) ) {
      if (token.getCourriel() != null) {
        Utilisateur utilisateurModifie = DAO.trouverParRequeteUnResultat(Utilisateur.class, "SELECT u FROM Utilisateur u WHERE u.eMaill = :email", "email", token.getCourriel());
        if (utilisateurModifie != null) {
          utilisateurModifie.setActive(true);
          DAO.modifier(utilisateurModifie);
          DAO.enlever(Token.class, idToken);
          return new Token(true, "activation réussie");
        }
        token = new Token(false, "utilisateur demander non existant");
        LOGGER.info("DAOUtilisateur.activerUtilisateur() -> ECHEC : " + token.getAction());
        
        return token;
      }
      token = new Token(false, "token ne contient pas d'utilisateur ascocié");
      LOGGER.info("DAOUtilisateur.activerUtilisateur() -> ECHEC : " + token.getAction());
        
      return token;
    }
    token = new Token(false, "token demander non existant");
    LOGGER.info("DAOUtilisateur.activerUtilisateur() -> ECHEC : " + token.getAction());
    
    return token;
  }

  /**
   * Modify an user
   * <ul>
       *  <li>Utilisateur must exist</li>
   *  <li>Pswd must comply policy</li> 
   * 	<li>Alias must comply policy</li>
   *  <li>Avatar must exist</li>
   *  <li>Avatar must exist</li>
   * 	<li>Song must belong to the user</li>
   * </ul>
   * 
   * @param idUtilisateur ID of the user
   * @param motDePasse The new pasoerd
   * @param alias The new alias
   * @param idAvatar ID of the new avatar
   * @return token
   */
  public static Token modifier(int idUtilisateur, String alias, String motDePasse, String courriel, int idAvatar) {
    LOGGER.info("DAOUtilisateur->modifier(" + idUtilisateur + "," + alias + "," + motDePasse + "," + courriel + "," + idAvatar + ")");
    
    Utilisateur utilisateur = DAO.trouverParId(Utilisateur.class, idUtilisateur);
    
    // try modification
    utilisateur.setDate();
    utilisateur.setMotDePasse(motDePasse);
    utilisateur.setAlias(alias);
    utilisateur.setCourriel(courriel);
    utilisateur.setAvatar(idAvatar);
    Utilisateur utilRetour = DAO.modifier(utilisateur);
    LOGGER.info("DAOUtilisateur.modifier() SUCCESS");
    // test if modification ok
    if ( !utilRetour.getMotDePasse().equals(motDePasse)
      || !utilRetour.getAlias().equals(alias)
      || !utilRetour.getCourriel().equals(courriel)
      ||  utilRetour.getAvatar() != idAvatar)
    {
        Token token = new Token(false, "erreur modification utilisateur dans DB");
        LOGGER.info("DAOUtilisateur.modifier() ERROR : " + token.getAction());
        return token;
    }

    return new Token(true, "modifier OK");
  }
}
