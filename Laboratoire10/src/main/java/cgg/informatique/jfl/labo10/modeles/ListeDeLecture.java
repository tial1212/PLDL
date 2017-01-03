package cgg.informatique.jfl.labo10.modeles;

import cgg.informatique.jfl.labo10.dao.DAOUtilisateur;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;

import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A playlist 
 * (no song inside )
 * @see ListesDelectureMusiques
 * @author alexandrearsenault
 */
@Entity
@NamedQueries({@NamedQuery(name = "listedelecture.list", query = "select l from ListeDeLecture l")})
@XmlRootElement(name = "listesdelecture")
public class ListeDeLecture extends ModeleDate{
  private static final Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
  
  /**
   * The id of the owner of this song.
   * @see Utilisateur#id
   */
  @Column(name = "Proprietaire")
  private int idProprietaire;

  /**
   * The playList's name.
   * @see Utilisateur#id
   */
  @Column(name = "Nom")
  private String nom;

  /**
   * If the PlayList is available for all.
   * (private playlist VS. public plailist )
   */
  @Column(name = "Publique", columnDefinition = "TINYINT(1)" )
  private boolean estPublique;

  /**
   * If the PlayList is curently activated
   */
  @Column(name = "Active", columnDefinition = "TINYINT(1)" )
  private boolean estActive;

  /**
  * DO NOT USE it is useless 
  * "Unenhanced classes must have a public or protected no-args constructor"
  */
  public ListeDeLecture(){}

  /**
   * Use this constructor instead. X params
   * 
   * @param pIDOwner ID of the owner
   * @param pName The name
   * @param pIsPublic If the playlist is public
   * @param pIsActive If the playlist is active.
   */
  public ListeDeLecture(int idProprietaire, String nom, boolean estPublique, boolean estActive){
    if (validerNom(nom)) { 
      this.idProprietaire = idProprietaire;
      this.nom = nom;
      this.estPublique = estPublique;
      this.estActive = estActive;
    } else
      LOGGER.warning("ListesDeLecture.constructor(" + idProprietaire + ", " + nom + ", " + estPublique + ", " + estActive + ") -> INVALIDE");
  }

  /**
   * Set the owner of the playlist
   * @return owner
   * @see Utilisateur#id
   */
  public int getIdProprietaire() {
    return idProprietaire;
  }

  /**
   * Set the owner of the playlist
   * @see Utilisateur#id
   */
  public void setIdProprietaire(int idProprietaire) {
    this.idProprietaire = idProprietaire;
  }

  /**
   * Get the name of the playlist.
   * @return name The name of the playlist.
   */
  public String getNom() {
    return nom;
  }

  /**
   * Set the name of the playlist.
   * @param pName,   The name to be set
   * @return ok if name has been changed.
   */
  public boolean setNom(String nom) {
    boolean ok = validerNom(nom);
    this.nom = ok ? nom : this.nom;
    
    return ok;
  }
  
  /**
   * Validate an name.<br>
   * <p>The policy for a playlist name is:</p>
   * <ul>
   *  <li><p>At least 4 chars</p></li>
   *  <li><p>No longer than 50 chars</p></li>
   *	</ul>
   * @param pName The name to validate.
   * @return ok
   */
  public static boolean validerNom(String nom) {
    boolean ok = nom.length() >= 4 && nom.length() <= 50;
    if (!ok)
      LOGGER.warning("ListesDeLecture.validateName(" + nom + ") ->INVALIDE");

    return ok;
  }

  /**
   * Get if the playlist is public
   * @return isPublic
   */
  public boolean estPublique() {
    return estPublique;
  }

  /**
   * Set the playlist public availibility
   * @param pIsPublic The public state to set
   */
  public void setPublique(boolean estPublique) {
    this.estPublique = estPublique;
  }

  /**
   * Get if the playlist is active
   * @return isActive
   */
  public boolean estActive() {
    return estActive;
  }

  /**
   * Activate/Desactivate the playlist
   * @param pIsActive The active state to set
   */
  public void setActive(boolean estActive) {
          this.estActive = estActive;
  }
  
  public Token validerModificationListeLecture(Token token) {
    int idProprietaire = Utilisateur.getIdUtilisateur(token.getCourriel());

    if (this == null) {
      token = new Token(false, "Liste de lecture inexistante");
      LOGGER.warning("ServiceListeLecture.validerModificationListeLecture() ERROR : " + token.getAction());

      return token;
    }

    if (this.getIdProprietaire() != idProprietaire) {
      token = new Token(false, "Liste de lecture n'appartient pas à l'utilisateur");
      LOGGER.warning("ServiceListeLecture.validerModificationListeLecture() ERROR : " + token.getAction());

      return token;
    }
    
    return token;
  }
  
  public Token validerAccesListeLecture(Token token) {
    int idProprietaire = Utilisateur.getIdUtilisateur(token.getCourriel());

    if (this == null) {
      token = new Token(false, "Liste de lecture inexistante");
      LOGGER.warning("ServiceListeLecture.validerAccesListeLecture() ERROR : " + token.getAction());

      return token;
    }
    
    if (!this.estPublique()) {
      if (this.getIdProprietaire() != idProprietaire) {
        token = new Token(false, "Liste de lecture privée et n'appartient pas à l'utilisateur");
        LOGGER.warning("ServiceListeLecture.validerAccesListeLecture() ERROR : " + token.getAction());

        return token;
      }
      
      LOGGER.info("ServiceListeLecture.validerAccesListeLecture() ERROR : Liste de lecture privée");

      return null;
    }
    
    return token;
  }
}



