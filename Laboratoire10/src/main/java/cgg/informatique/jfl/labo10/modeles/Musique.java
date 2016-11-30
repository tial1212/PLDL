package cgg.informatique.jfl.labo10.modeles;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({
   @NamedQuery(name = "musique.list", query = "select m from Musique m")
              })
@XmlRootElement(name = "musique")
public class Musique extends ModeleDate  {
	
	
	/**
	 * The id of the owner of this song.
     * @see Utilisateur#id
	 */
	@Column(name = "Proprietaire", length=11 )
	private int owner;
	
	/**
	 * The title of the song.
	 */
	@Column(name = "Titre")
	private String title;
	
	/**
	 * The song's artist.
	 */
	@Column(name = "Artiste" )
	private String artist;  
	
	/**
	 * The song itself ( Base64 )
	 */
	@Lob
	@Column(name = "Musique" )
	private String music; 
	
	/**
	 * The cover art for the song ( Base64 )
	 * A square image, usually the album's Artwork.
	 */
	@Lob
	@Column(name = "Vignette" )
	private String coverArt;
	
	/**
	 * If the song is available for all.
	 * (private song VS. public song )
	 */
	@Column(name = "Publique" , columnDefinition   ="TINYINT(1)" )
	private boolean isPublic;
	
	/**
	 * If the song is curently activated.
	 */
	@Column(name = "Active" , columnDefinition   ="TINYINT(1)" )
	private boolean isActive;
	
	
	
	/**
     * DO NOT USE it is useless 
     *  "Unenhanced classes must have a public or protected no-args constructor"
     */
	public Musique(){ }
	
	
	/**
	 * Use this constructor instead. 7 params
     * 
	 * @param pIdOwner The id of the owner of this song.
	 * @param pTitle The title of the song.
	 * @param pArtist The song'S artist
	 * @param pMusic The song itself ( Base64 )
	 * @param pCoverArt The cover art data ( Base64 )
	 * @param pIsPublic If the song is available for all
	 * @param pIsActive If the song is curently activated
	 */
	public Musique( int pIdOwner ,String pTitle ,String pArtist ,String pMusic ,String pCoverArt ,boolean pIsPublic ,boolean pIsActive ){
		if(validateTitle(pTitle) && validateArtist(pArtist) ){
			this.owner = pIdOwner;
			this.title = pTitle;
			this.artist = pArtist;
			this.music = pMusic;
                        this.coverArt = pCoverArt;
			this.isPublic = pIsPublic;
			this.isActive = pIsActive;
		}
		else{
    		System.err.println("Musique.constructor("+pIdOwner+""+pTitle+""+pArtist +""+pMusic+""+pCoverArt +""+pIsPublic+""+pIsActive +") -> INVALIDE");
    	}
	
	}
	
	/**
	 * Get the owner's ID for the song.
	 * @return pOwner ,
	 */
	public int getOwner() {
		return owner;
	}
	
	/**
	 * Set the owner's ID for the song.
	 * 
	 * @param pOwner The owner to be set.
	 */
	public void setOwner(int pOwner) {
		this.owner = pOwner;
	}
	
	/**
	 * Get the song's title.
	 * @return title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Set the song's title.
	 * @param pTitle The title to be set.
	  * @return ok If the title has been change.
	 */
	public boolean setTitle(String pTitle) {
		boolean ok = validateTitle(pTitle);
		this.title = (ok? pTitle :this.title);
		return ok;
	}
	
	
	/**
	 * Validate a title.<br>
	 * <p>The policy for a song's title is:</p>
	 * <ul>
	 *  <li><p>At least 5 chars</p></li>
	 *  <li><p>No longer than 50 chars</p></li>
	 *	</ul>
	 *@param pTitle The title to validate.
	 * @return ok
	 */
	public static boolean validateTitle(String pTitle) {
		int length = pTitle.length();
		boolean ok = length>=5 && length<=50;
		if (!ok) {
			System.err.println("Musique.validateTitle("+pTitle+") ->INVALIDE");
		}
        return ok;
	}
	
	/**
	 * Get the song's artist.
	 * @return artist
	 */
	public String getArtist() {
		return artist;
	}
	
	/**
	 * Set the song's artist.
	 * @param pArtist The artist to be set.
	  * @return ok If the artist has been change.
	 */
	public boolean setArtist(String pArtist) {
		boolean ok = validateArtist(pArtist);
		this.artist = (ok? pArtist :this.artist);
		return ok;
	}
	
	/**
	 * Validate an artist.<br>
	 * <p>The policy for an artist's song is:</p>
	 * <ul>
	 *  <li><p>At least 5 chars</p></li>
	 *  <li><p>No longer than 50 chars</p></li>
	 *	</ul>
	 *@param pArtist The title to validate.
	 * @return ok
	 */
	public static boolean validateArtist(String pArtist) {
		int length = pArtist.length();
		boolean ok = length>=5 && length<=50;
		if (!ok) {
			System.err.println("Musique.validateArtist("+pArtist+") ->INVALIDE");
		}
        return ok;
	}


	/**
	 * Get the song data itself ( Base64 )
	 * @return music The song data itself.
	 */
	public String getMusic() {
		return music;
	}
	
	/**
	 * Set the song data itself ( Base64 )
	 * @param music The song data itself.
	 */
	public void setMusic(String music) {
		this.music = music;
		//TODO validation
	}
	
	/**
	 * Get the song's CoverArt ( Base64 )
	 * @return coverArt
	 */
	public String getCoverArt() {
		return coverArt;
	}
	
	/**
	 * Get the song's CoverArt ( Base64 )
	 * @param pCoverArt The data of the Cover Art.
	 */
	public void setCoverArt(String pCoverArt) {
		this.coverArt = pCoverArt;
		//TODO validation
	}
	
	/**
	 * Get if the song is public (for anyone )
	 * @return isPublic If the song is public.
	 */
	public boolean isPublic() {
		return isPublic;
	}
	
	/**
	 * Set if everybody can see the song.
	 * @param pIsPublic If the song should be public.
	 */
	public void setPublic(boolean pIsPublic) {
		this.isPublic = pIsPublic;
	}
	
	/**
	 * Get if the song is active.
	 * @return isActive If the song is curently activated
	 */
	public boolean isActive() {
		return isActive;
	}
	
	/**
	 * Set if the song is active.
	 * @param pIsActive If the song should be activate
	 */
	public void setActive(boolean pIsActive) {
		this.isActive = pIsActive;
	}
	
	@Override
	public String toString() {
		return "Song named ':"+this.title+"' by :"+this.artist+"' owned by '"+this.owner+"' ";
	}

}
