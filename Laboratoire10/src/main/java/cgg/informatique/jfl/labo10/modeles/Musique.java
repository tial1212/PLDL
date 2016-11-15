package cgg.informatique.jfl.labo10.modeles;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({
   @NamedQuery(name = "musique.list", query = "select m from Musique m")
              })
@XmlRootElement(name = "musique")
public class Musique extends Modele  {
	
	
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
	 * The song artist
	 */
	@Column(name = "Artiste" )
	private String artist;  
	
	/**
	 * The song itself ( Base64 )
	 */
	@Column(name = "Musique" )
	private String music; 
	
	/**
	 * The cover art for the song ( Base64 )
	 * A square image, usually the album's Artwork.
	 */
	@Column(name = "Vignette" )
	private String coverArt;
	
	/**
	 * If the song is available for all.
	 * (private song VS. public song )
	 */
	@Column(name = "Publique" , columnDefinition   ="TINYINT(1)" )
	private boolean isPublic;
	
	/**
	 * If the song is curently activated
	 */
	@Column(name = "Active" , columnDefinition   ="TINYINT(1)" )
	private boolean isActive;
	
	/**
	 *  The last modification date
	 */
	@Column(name = "Date") 
	private Date date;
	
	
	
	

	/**
     * DO NOT USE it is useless 
     *  "Unenhanced classes must have a public or protected no-args constructor"
     */
	public Musique(){ }
	
	
	/**
	 * Use this constructor instead. 7 params
     * 
	 * @param pOwner
	 * @param pTitle
	 * @param pArtist
	 * @param pMusic
	 * @param pCoverArt
	 * @param pPublic
	 * @param pActive
	 */
	public Musique( int pOwner ,String pTitle ,String pArtist ,String pMusic ,String pCoverArt ,boolean pIsPublic ,boolean pIsActive ){
		if(true){   // TODO real validation
			this.owner = pOwner;
			this.title = pTitle;
			this.artist = pArtist;
			this.music = pMusic;
			this.isPublic = pIsPublic;
			this.isActive = pIsActive;
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
	 * @param pTitle
	 */
	public void setTitle(String pTitle) {
		this.title = pTitle;
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
	 * @param artist
	 */
	public void setArtist(String artist) {
		this.artist = artist;
	}
	
	/**
	 * Get the song data itself ( Base64 )
	 * @return
	 */
	public String getMusic() {
		return music;
	}
	
	/**
	 * Set the song data itself ( Base64 )
	 * @param music
	 */
	public void setMusic(String music) {
		this.music = music;
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
	 * @param pCoverArt
	 */
	public void setCoverArt(String pCoverArt) {
		this.coverArt = pCoverArt;
	}
	
	/**
	 * Get if the song is public (for anyone )
	 * @return
	 */
	public boolean isPublic() {
		return isPublic;
	}
	
	/**
	 * Set if everybody can see the song.
	 * @param pIsPublic
	 */
	public void setPublic(boolean pIsPublic) {
		this.isPublic = pIsPublic;
	}
	
	/**
	 * Get if the song is active.
	 * @return
	 */
	public boolean isActive() {
		return isActive;
	}
	
	/**
	 * Set if the song is active.
	 * @param pIsActive
	 */
	public void setActive(boolean pIsActive) {
		this.isActive = pIsActive;
	}
	
	/**
	 * Get the last modification date.
	 * @return
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 *Get the last modification date.
	 * @param date
	 */
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public String toString() {
		return "Song named ':"+this.title+"' by :"+this.artist+"' owned by '"+this.owner+"' ";
	}

}
