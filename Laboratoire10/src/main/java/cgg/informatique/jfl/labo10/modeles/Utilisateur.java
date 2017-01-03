package cgg.informatique.jfl.labo10.modeles;

import cgg.informatique.jfl.labo10.dao.DAO;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({@NamedQuery(name = "utilisateur.list", query = "select u from Utilisateur u")})
@XmlRootElement(name = "utilisateur")
public class Utilisateur extends ModeleDate {
  private static final Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
  
  /**
   * The E-mail of the user. 
   */
  @Column(name = "Courriel", length = 50, nullable = false, unique=true )
  private String courriel;

  /**
   * The motDePasse (MD5 encrypted ) of the user.
   */
  @Column(name = "MotDePasse", columnDefinition = "text") 
  private String motDePasse;

  /**
   * The displayed name of the user (NickName)
   */
  @Column(name = "Alias", length = 50)
  private String alias;

  /**
   * The ID of the selected Avatar. 
   * @see Avatar#id
   */
  @Column(name = "Avatar", length = 11)
  private int avatar;

  /**
   * If the user has been activated 
   * (has valider captcha ) 
   */
  @Column(name = "Actif" , columnDefinition = "TINYINT(1)")
  private boolean actif;

  /**
   * DO NOT USE it is useless 
   * "Unenhanced classes must have a public or protected no-args constructor"
   */
  public Utilisateur() {}

  /**
   * Create an unactivated user.
   * 
   * @param pAlias The displayed name of the user (NickName)
   * @param courriel The E-mail of the user. 
   * @param motDePasse The motDePasse (MD5 encrypted ) of the user.
   * @param pAvatar The alias The ID of the selected Avatar. 
   */
  public Utilisateur(String pAlias, String courriel, String motDePasse, int avatar) {
    super();
    if (validerAlias(pAlias) && validerCourriel(courriel) && validerMotDePasse(motDePasse) ) {
      this.alias = pAlias;
      this.courriel = courriel;
      this.motDePasse = motDePasse;
      this.avatar = avatar;
      this.actif = false;
    }
    else
      LOGGER.warning("Utilisateur.constructor(" + pAlias + ", " + courriel + ", " + motDePasse + ", " + avatar + ") -> INVALIDE");
  }

  /**
   * Get the e-mail of the user.
   * @return courriel The E-mail of the user.
   */
  public String getCourriel() {
    return courriel;
  }

  /**
   * Set the e-mail of the user.
   * @param courriel,   The e-mail to be set
   * @return ok if email has been changed.
   */
  public boolean setCourriel(String courriel) {
    boolean ok = validerCourriel(courriel);
    this.courriel = (ok?courriel:this.courriel);
    
    return ok;
  }

  /**
   * Validate an e-mail adress.<br>
   * 
   * 
   * <p>The policy for an user's e-mail is:</p>
   * <ul>
   *  <li><p>At least 4 chars</p></li>
   *  <li><p>No longer than 50 chars</p></li>
   *	<li><p> Must be in a valid form ( name@compagny.domain ).</p></li>
   *	</ul>
   * @param courriel The e-mail to valider
   * @return ok
   */
  public static boolean validerCourriel(String courriel) {
    Pattern VALID_EMAIL_ADDRESS_REGEX =  Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(courriel);
    int length = courriel.length();
    boolean ok = matcher.find() && length>=4 && length<=50 ;
    if (!ok)
      LOGGER.warning("Utilisateur.validerCourriel(" + courriel + ") -> INVALIDE");
            
    return ok;
  }

  /**
   * Get the user's motDePasse.
   * @return motDePasse
   */
  public String getMotDePasse() {
    return motDePasse;
  }

  /**
   * 
   * Set the user's motDePasse.
   * @param motDePasse The motDePasse to be set.
   * @return ok if motDePasse has been changed.
   */
  public boolean setMotDePasse(String motDePasse) {
    boolean ok = validerMotDePasse(motDePasse);
    this.motDePasse = (ok?motDePasse:this.motDePasse);
    return ok;
  }

  /**
   * Validate a  motDePasse.<br>
   * <p>The policy for an user's password is:</p>
   * <ul>
   *  <li><p>At least 8 chars</p></li>
   *  <li><p>No longer than 50 chars</p></li>
   *	<li><p>Contains at least one digit</p></li>
   *	<li><p>Contains at least one lower alpha char and one upper alpha char</p></li>
   *	<li><p>Contains at least one char within a set of special chars (<code>@#%$^</code> etc.)</p></li>
   *	<li><p>Does not contain space, tab, etc.</p></li>
   *	</ul>
   * @param motDePasse The motDePasse to valider.
   * @return ok 
   */
  public static boolean validerMotDePasse(String motDePasse) {
    Pattern VALID_EMAIL_ADDRESS_REGEX =  Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$", Pattern.CASE_INSENSITIVE);
    Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(motDePasse);
    int length = motDePasse.length();
    boolean ok = matcher.find() && length<=50;
    if (!ok)
      System.err.println("Utilisateur.validerMotDePasse(" + motDePasse + ") -> INVALIDE");
    
    return ok;
  }

  /**
   * Get the user's alias.
   * @return alias The display name of the user 
   */
  public String getAlias() {
    return alias;
  }

  /**
   * Set the user's alias.
   * @param pAlias The new display name to be set.
   * @return ok if alias has been changed.
   */
  public boolean setAlias(String pAlias) {
    boolean ok = validerAlias(pAlias);
    this.alias = (ok?pAlias:this.alias);
    
    return ok;
  }

  /**
   * Validate an alias.<br>
   * <p>The policy for an user's alias is:</p>
   * <ul>
   *  <li><p>At least 4 chars</p></li>
   *  <li><p>No longer than 50 chars</p></li>
   *	</ul>
   * @param pAlias The alias to valider
   * @return ok
   */
  public static boolean validerAlias(String pAlias) {
    int length = pAlias.length();
    boolean ok = length>=4 && length<=50;
    if (!ok) {
      LOGGER.warning("Utilisateur.validerAlias(" +pAlias  +") -> INVALIDE");
    }
    
    return ok;
  }

  /**
   * If the user has been activated
   * @return actif
   */
  public boolean isActive() {
    return actif;
  }

  /**
   * Set if the user is activated
   * @param pActive If actif
   */
  public void setActive(boolean pActive) {
    this.actif = pActive;
  }

  /**
   * Get the avatar id for the user.
   * 
   * @return avatar
   * @see Avatar#id
   */
  public int getAvatar() {
    return avatar;
  }

  /**
   * Set the avatar id.
   * 
   * @param pIdAvatar ID of avatar to set.
   * @see Avatar#id
   */
  public void setAvatar(int pIdAvatar) {
    this.avatar = pIdAvatar;
  }

  /**
   * Check if user is existing from an User Email,
   * @param courriel The Email to verify
   * @return ok
   */
  public static boolean validerExistenceUtilisateur(String courriel){
    boolean ok = (DAO.trouverParRequete(Utilisateur.class,
                  "SELECT u FROM Utilisateur u WHERE u.courriel = :email", "email", courriel) != null);
    LOGGER.info("DAOUtilisateur->isUserExisting(" + courriel + ") : " + (ok ? "OUI" : "NON"));

    return ok;
  }

  /**
   * Check if user is activated from an User Email,
   * @param courriel The Email to verify
   * @return ok
   */
  public static boolean validerEtatActifUtilisateur(String courriel){
    boolean okExist = validerExistenceUtilisateur(courriel);
    if (!okExist) {
      LOGGER.warning("DAOUtilisateur.validerEtatActifUtilisateur(" + courriel + ") : user DOESN'T exist" );
      
      return false;
    }
    Utilisateur utilisateurRequested = DAO.trouverParRequeteUnResultat(Utilisateur.class, "SELECT u FROM Utilisateur u WHERE u.courriel = :email", "email", courriel);
    boolean okActivated = utilisateurRequested.isActive();
    LOGGER.info("DAOUtilisateur->isUserActivated("+courriel+") : "+(okActivated?"YES" : "NO" ) );
    return okActivated;
  }

  /**
   * Get the User.id for an EMail.
   * <br> !!!MIGTH!!!<br> return -1 if user doesn't exist
   * @param courriel The Email to verify
   * @return id OR -1
   */
  public static int getIdUtilisateur(String courriel){
    Utilisateur utilisateurRequested = DAO.trouverParRequeteUnResultat(Utilisateur.class, "SELECT u FROM Utilisateur u WHERE u.courriel = :email", "email", courriel);
    if (utilisateurRequested != null ) {
      utilisateurRequested.getId();
      
      return utilisateurRequested.getId();
    }
    return -1;
  }

  @Override
  public String toString() {
    return "User '" + this.courriel + "' goes by '" + this.alias + "'It has :" + (this.actif?" " : " not ") + " been activated";
  }
}
