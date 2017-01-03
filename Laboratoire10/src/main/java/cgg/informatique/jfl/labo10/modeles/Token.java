package cgg.informatique.jfl.labo10.modeles;
import cgg.informatique.jfl.labo10.dao.DAOUtilisateur;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.services.ServiceCaptcha;
import cgg.informatique.jfl.labo10.utilitaires.MD5Digest;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.RandomStringUtils;

@Entity
@NamedQueries({@NamedQuery(name = "token.list", query = "select t from Token t")})
@XmlRootElement(name = "token")
public class Token extends Modele {
  private static final Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
	
  /**
   * Describe an action token's action
   */
  public static final String TXT_ACTION_TOKEN =  "action token";
  
  /**
   * Describe a user confirmation token's action
   */
  public static final String TXT_CONFIRM_TOKEN = "confirmation creer";

  /**
   * The string value of the captcha 
   */
  private String captchaStr;

  /**
   * Detailed information about last transaction.<br>
   * Can also indicate last action completed or specific error message if there is.
   */
  private String action;

  /**
   * A salt used to secure the user's pasowrd <br>
   * The salt is save in the database in MD5
   */
  private String salt;

  /**
   * If transaction was succesful.
   */
  private Boolean etat;
    
  /**
   * The E-mail of the user requesting the action.
   */
  @Column(name = "Courriel", length=50 , nullable = false  )
  private String courriel;
    
  /**
   * DO NOT USE it is useless 
   *  "Unenhanced classes must have a public or protected no-args constructor"
   */
  public Token() {}

  /**
   * 2 param constructor. (Succes/Error Token) <br> 
   * Create a token with : etat , action
   * @param pEtat The succes state
   * @param pAction The description of the occuring action
   */
  public Token( boolean pEtat , String pAction) {
    if (validerAction(pAction)) {
      this.etat = pEtat;
      this.action = pAction;
    } else
      LOGGER.warning("Token.constructor("+pEtat+""+pAction+") -> INVALIDE");
  }

  /**
   * 3 param constructor. (Action Token)<br>
   * Create a token with : etat , action , salt
   * @param pEtat The succes state
   * @param pAction The action
   * @param pSalt The salt
   */
  public Token( boolean pEtat , String pAction, String pSalt) {
    if (validerAction(pAction) && validerSalt(pSalt)) {
      this.etat = pEtat;
      this.action = pAction;
      this.salt = pSalt;
    } else
      LOGGER.warning("Token.constructor("+pEtat+","+pAction+","+pSalt+") -> INVALIDE");
  }

  /**
   * Generate a random salt that comply with the folowing policy 
   * <p>The policy for a token's salt is:</p>
   * <ul>
   *  <li><p>At least 8 chars</p></li>
   *  <li><p>No longer than 50 chars</p></li>
   *	</ul>
   * @return salt
   */
  private static String genererSaltHazard() {
    int length = ThreadLocalRandom.current().nextInt(8, 50 + 1);

    return RandomStringUtils.randomAlphabetic(length);
  }

  /**
   *  Generate a confirmation token used for an user activation.
   * @param pCourriel The email of the requesting user
   * @return token
   */
  public static Token genererTokenConfirmation(String pCourriel) {
    Token token = new Token();
    token.setCaptchaStr(ServiceCaptcha.getRdmCaptchaStr() );
    token.setCourriel(pCourriel);
    token.setAction(TXT_CONFIRM_TOKEN);
    token.setEtat(true);
    
    return token;
  }

  /**
   *  Generate an action token. Used to confirm any action.
   * @param pCourriel The email of the requesting user
   * @return Token
   */
  public static Token genererTokenAction(String pCourriel) {
    Token token = new Token();
    token.setSalt( genererSaltHazard());
    token.setAction(TXT_ACTION_TOKEN);
    token.setCourriel(pCourriel);
    token.setEtat(true);

    return token;
  }
        
  /**
   * Get the e-mail of the user requesting the action.
   * @return courriel The E-mail of the user.
   */
  public String getCourriel() {
    return courriel;
  }

  /**
   * Set the e-mail of the user requesting the action.
   * @param  pCourriel   The e-mail to be set
   * @return ok If email has been changed.
   */
  public boolean setCourriel(String pCourriel) {
    boolean ok = Utilisateur.validerCourriel(pCourriel);
    this.courriel = (ok?pCourriel:this.courriel);

    return ok;
  }

  /**
   * Get the token salt
   * @return salt
   */
  public String getSalt() {
    return salt;
  }

  /**
   * Set salt with the default "longAssSalt".
   * @return ok If the salt has been change to the default.
   */
  public boolean setSalt() {
    String defaultSalt = "longAssSalt";
    boolean ok = validerSalt(defaultSalt);
    this.salt = (ok?MD5Digest.getMD5("salt") :this.salt);

    return ok;
  }

  /**
   * Set a specific salt.
   * @param pSalt The salt to be set.
   * @return ok If the salt has been change.
   */
  public boolean setSalt(String pSalt) {
    this.salt = pSalt;
    boolean ok = validerSalt(pSalt);
    this.salt = (ok?MD5Digest.getMD5("salt") :this.salt);

    return ok;
  }

  /**
   * Validate a salt.<br>
   * <p>The policy for a token's salt is:</p>
   * <ul>
   *  <li><p>At least 8 chars</p></li>
   *  <li><p>No longer than 50 chars</p></li>
   *	</ul>
   *@param pSalt The salt to valider.
   * @return ok
   */
  public boolean validerSalt(String pSalt){
    int length = pSalt.length();
    boolean ok = length>=8 && length<=50;
    if (!ok)
      LOGGER.warning("Token.validerSalt("+pSalt+") ->INVALIDE");
    
    return ok;
  }

  /**
   * Get the string captcha<br>
   * To get a Base 64 img call "serviceCaptcha"
   * @see ServiceCaptcha#getCaptcha64
   * @return captchaStr
   */
  public String getCaptchaStr() {
    return captchaStr;
  }

  /**
   * Set the string captcha 
   * 
   * @param pCaptchaStr The captcha to be set.
   * @return ok
   * @see ServiceCaptcha#getCaptcha64
   */
  public boolean setCaptchaStr(String pCaptchaStr) {
    boolean ok = validerCaptchaStr(pCaptchaStr);
    this.captchaStr = (ok? pCaptchaStr :this.captchaStr);

    return ok;
  }

  /**
   * Validate a captcha.<br>
   * <p>The policy for a token's captcha is:</p>
   * <ul>
   *  <li><p>At least 5 chars</p></li>
   *  <li><p>No longer than 15 chars</p></li>
   *	</ul>
   *@param pCaptchaStr The salt to valider.
   * @return ok
   */
  public boolean validerCaptchaStr(String pCaptchaStr){
    int length = pCaptchaStr.length();
    boolean ok = length>=5 && length<=15;
    if (!ok)
      LOGGER.warning("Token.validerCaptchaStr("+pCaptchaStr+") ->INVALIDE");
            
    return ok;
  }

  /**
   * Get the token's action.
   * @return action
   */
  public String getAction() {
    return action;
  }

   /**
    * Set the token's action.
    * 
    * @param pAction The action to be set.
    * @return ok If the action has been change.
    */
  public boolean setAction(String pAction) {
    boolean ok = validerAction(pAction);
    this.action = (ok? pAction :this.action);

    return ok;
  }

  /**
   * Validate an action/error message.<br>
   * <p>The policy for a token's action is:</p>
   * <ul>
   *  <li><p>At least 5 chars</p></li>
   *  <li><p>No longer than 50 chars</p></li>
   *	</ul>
   *@param pAction The action to valider.
   * @return ok
   */
  public boolean validerAction(String pAction){
    int length = pAction.length();
    boolean ok = length>=5 && length<=50;
    if (!ok)
      LOGGER.warning("Token.validerAction("+pAction+") ->INVALIDE");
            
    return ok;
  }

  /**
   * Get the token's action state (succes/faillure)
   * @return etat
   */
  public Boolean getEtat() {
    return etat;
  }

  /**
   * Set the token's action state (succes/faillure)
   * @param pEtat The token succes state
   */
  public void setEtat(Boolean pEtat) {
    this.etat = pEtat;
  }

  public Token validerTokenAction(String cle) {
    Token token = new Token(true, "Token est valide");

    if (this == null)
      token = new Token(false, "Token inexistant");
    else if (!this.getAction().equals(Token.TXT_ACTION_TOKEN))
      token = new Token(false, "Token pas ActionToken");
    else if (this.getSalt().equals(cle))
      token = new Token(false, "Token clé invalide");
    else {
      if (!Utilisateur.validerExistenceUtilisateur(this.getCourriel()))
        token = new Token(false, "Propriétaire inexistant");
      else if (!Utilisateur.validerEtatActifUtilisateur(this.getCourriel()))
        token = new Token(false, "Propriétaire non actif");
    }

    if (!token.getEtat())
      LOGGER.warning("DAOListesDeLecture.create() ERROR : " + token.getAction());

    return token;
  }

  @Override
  public String toString() {
    String captchaStr = (this.captchaStr != null?", captcha = "+this.captchaStr+"":"");
    String action = (this.action != null?", action = "+this.action+" ":"");
    String salt = (this.salt != null?", salt = "+this.salt+" ":"");
    String etat = (this.captchaStr != null?", etat = "+this.etat.toString()+" ":"");
    
    return "Token #" + this.id + captchaStr + action + salt + etat;
  }
}
