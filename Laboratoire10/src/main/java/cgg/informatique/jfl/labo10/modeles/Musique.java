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
@NamedQueries({
@NamedQuery(name = "musique.list", query = "select m from Musique m")})
@XmlRootElement(name = "musique")
public class Musique extends ModeleDate  {
  private static final Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
  
  /**
   * The id of the idProprietaire of this song.
   * @see Utilisateur#id
   */
  @Column(name = "Proprietaire", length = 11 )
  private int idProprietaire;

  /**
   * The titre of the song.
   */
  @Column(name = "Titre")
  private String titre;

  /**
   * The song's artist.
   */
  @Column(name = "Artiste" )
  private String artiste;  

  /**
   * The song itself ( Base64 )
   */
  @Lob
  @Column(name = "Musique" )
  private String musique; 

  /**
   * The cover art for the song ( Base64 )
   * A square image, usually the album's Artwork.
   */
  @Lob
  @Column(name = "Vignette" )
  private String vignette;

  /**
   * If the song is available for all.
   * (private song VS. public song )
   */
  @Column(name = "Publique", columnDefinition = "TINYINT(1)")
  private boolean estPublique;

  /**
   * If the song is curently activated.
   */
  @Column(name = "Active", columnDefinition = "TINYINT(1)" )
  private boolean estActive;

  /**
   * DO NOT USE it is useless 
   *  "Unenhanced classes must have a public or protected no-args constructor"
   */
  public Musique() {}

  /**
   * Use this constructor instead. 7 params
   * 
   * @param idProprietaire The id of the idProprietaire of this song.
   * @param titre The titre of the song.
   * @param artiste The song'S artist
   * @param musique The song itself ( Base64 )
   * @param vignette The cover art data ( Base64 )
   * @param estPublique If the song is available for all
   * @param estActive If the song is curently activated
   */
  public Musique(int idProprietaire, String titre, String artiste, String musique, String vignette, boolean estPublique, boolean estActive) {
    if (validerTitre(titre) && validerArtiste(artiste)) {
      this.idProprietaire = idProprietaire;
      this.titre = titre;
      this.artiste = artiste;
      this.musique = musique;
      this.vignette = vignette;
      this.estPublique = estPublique;
      this.estActive = estActive;
    } else
      LOGGER.warning("Musique.constructor(" + idProprietaire + ", " + titre + ", " + artiste
                   + ", " + musique + ", " + vignette + ", " + estPublique + ", " + estActive + ") -> INVALIDE");
  }

  /**
   * Get the idProprietaire's ID for the song.
   * @return pIdProprietaire ,
   */
  public int getIdProprietaire() {
    return idProprietaire;
  }

  /**
   * Set the idProprietaire's ID for the song.
   * 
   * @param pIdProprietaire The idProprietaire to be set.
   */
  public void setIdProprietaire(int pIdProprietaire) {
    this.idProprietaire = pIdProprietaire;
  }

  /**
   * Get the song's titre.
   * @return titre
   */
  public String getTitre() {
    return titre;
  }

  /**
   * Set the song's titre.
   * @param titre The titre to be set.
    * @return ok If the titre has been change.
   */
  public boolean setTitre(String titre) {
    boolean ok = validerTitre(titre);
    this.titre = ok ? titre : this.titre;
    
    return ok;
  }


  /**
   * Validate a titre.<br>
   * <p>The policy for a song's titre is:</p>
   * <ul>
   *  <li><p>At least 5 chars</p></li>
   *  <li><p>No longer than 50 chars</p></li>
   *	</ul>
   *@param titre The titre to validate.
   * @return ok
   */
  public static boolean validerTitre(String titre) {
    int length = titre.length();
    boolean ok = length>=5 && length<=50;
    if (!ok) {
      System.err.println("Musique.validateTitre("+titre+") ->INVALIDE");
    }
    return ok;
  }

  /**
   * Get the song's artist.
   * @return artist
   */
  public String getArtiste() {
    return artiste;
  }

  /**
   * Set the song's artist.
   * @param artiste The artiste to be set.
    * @return ok If the artiste has been change.
   */
  public boolean setArtiste(String artiste) {
    boolean ok = validerArtiste(artiste);
    this.artiste = ok ? artiste : this.artiste;
    
    return ok;
  }

  /**
   * Validate an artist.<br>
   * <p>The policy for an artist's song is:</p>
   * <ul>
   *  <li><p>At least 5 chars</p></li>
   *  <li><p>No longer than 50 chars</p></li>
   *	</ul>
   *@param artiste The titre to validate.
   * @return ok
   */
  public static boolean validerArtiste(String artiste) {
    int length = artiste.length();
    boolean ok = length>=5 && length<=50;
    if (!ok) {
      System.err.println("Musique.validateArtist("+artiste+") ->INVALIDE");
    }
    return ok;
  }

  /**
   * Get the song data itself ( Base64 )
   * @return musique The song data itself.
   */
  public String getMusique() {
    return musique;
  }

  /**
   * Set the song data itself ( Base64 )
   * @param musique The song data itself.
   */
  public void setMusique(String musique) {
    this.musique = musique;
    //TODO validation
  }

  /**
   * Get the song's Vignette ( Base64 )
   * @return vignette
   */
  public String getVignette() {
    return vignette;
  }

  /**
   * Get the song's Vignette ( Base64 )
   * @param vignette The data of the Cover Art.
   */
  public void setVignette(String vignette) {
    this.vignette = vignette;
    //TODO validation
  }

  /**
   * Get if the song is public (for anyone )
   * @return estPublique If the song is public.
   */
  public boolean estPublique() {
    return estPublique;
  }

  /**
   * Set if everybody can see the song.
   * @param estPublique If the song should be public.
   */
  public void setPublique(boolean estPublique) {
    this.estPublique = estPublique;
  }

  /**
   * Get if the song is active.
   * @return estActive If the song is curently activated
   */
  public boolean estActive() {
    return estActive;
  }

  /**
   * Set if the song is active.
   * @param estActive If the song should be activate
   */
  public void setActive(boolean estActive) {
    this.estActive = estActive;
  }

  @Override
  public String toString() {
    return "Song named ':" + this.titre + "' by :" + this.artiste + "' owned by '" + this.idProprietaire + "'";
  }
  
  public Token validerModificationMusique(Token token) {
    int idProprietaire = Utilisateur.getIdUtilisateur(token.getCourriel());

    if (this == null) {
      token = new Token(false, "Musique inexistante");
      LOGGER.warning("ServiceMusique.validerModificationMusique() ERROR : " + token.getAction());

      return token;
    }

    if (this.getIdProprietaire() != idProprietaire) {
      token = new Token(false, "Musique n'appartient pas à l'utilisateur");
      LOGGER.warning("ServiceMusique.validerModificationMusique() ERROR : " + token.getAction());

      return token;
    }
    
    return token;
  }
  
  public Token validerAccesMusique(Token token) {
    int idProprietaire = Utilisateur.getIdUtilisateur(token.getCourriel());

    if (this == null) {
      token = new Token(false, "Musique inexistante");
      LOGGER.warning("ServiceMusique.validerAccesMusique() ERROR : " + token.getAction());

      return token;
    }
    
    if (this.estPublique()) {
      if (this.getIdProprietaire() != idProprietaire) {
        token = new Token(false, "Liste de lecture privée et n'appartient pas à l'utilisateur");
        LOGGER.warning("ServiceMusique.validerAccesMusique() ERROR : " + token.getAction());

        return token;
      }
      
      LOGGER.info("ServiceMusique.validerAccesMusique() ERROR : Playlist privée");

      return null;
    }
    
    return token;
  }
}
