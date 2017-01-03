package cgg.informatique.jfl.labo10.modeles;

import cgg.informatique.jfl.labo10.demarrage.Demarrage;

import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({@NamedQuery(name = "avatar.list", query = "select a from Avatar a")})
@XmlRootElement(name = "avatar")
public class Avatar extends Modele {
  private static final Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
  
  /**
   * A description for the avatar 
   */
  @Column(name = "Nom", length=50 , nullable = false)
  private String nom;

  /**
   *  The avatar image in Base64
   */
  @Lob
  @Column(name = "Image")
  private String image;

  /**
   * DO NOT USE it is useless 
   * "Unenhanced classes must have a public or protected no-args constructor"
   */
  public Avatar() {}

  /**
   * Use this constructor instead. 2 params
   * 
   * @param pNom, A description for the avatar 
   * @param pAvatar The avatar image in Base64
   */
  public Avatar(String nom , String image){
    if (nom.length() <= 50) {
      this.nom = nom;
      this.image = image;
    }
  }

  /**
   * Get the avatar's name ( description ) 
   * @return name
   */
  public String getNom() {
    return nom;
  }

  /**
   * Set the name (description) of the avatar. 
   * @param pName The name to be set.
   * @return ok If the name has been change.
   */
  public boolean setNom(String nom) {
    boolean ok = validerNom(nom);
    this.nom = (ok ? nom : this.nom);
    
    return ok;
  }

  /**
   * Validate a name.<br>
   * <p>The policy for an avatar's name is:</p>
   * <ul>
   *  <li><p>At least 4 chars</p></li>
   *  <li><p>No longer than 20 chars</p></li>
   *	</ul>
   *@param pName The name to validate.
   * @return ok
   */
  public static boolean validerNom(String nom) {
    boolean ok = nom.length() >= 5 && nom.length() <= 50;
    if (!ok)
      LOGGER.warning("Avatar.validerNom(" + nom + ") -> INVALIDE");

    return ok;
  }

  /**
   * Get the avatar (image in Base64 )
   * @return avatar a description for the avatar.
   */
  public String getImage() {
    return image;
  }

  /**
   *  Set the avatar (image in Base64 )
   * @param pAvatar The avatar to be set
   */
  public void setImage(String image) {
    this.image = image;
  }

  @Override
  public String toString() {
    return "Avatar named ':" + this.nom + "'";
  }
}
