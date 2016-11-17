package cgg.informatique.jfl.labo10.modeles;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({
   @NamedQuery(name = "listesdelecture.list", query = "select l from ListesDeLecture l")
              })
@XmlRootElement(name = "listesdelecture")
public class ListesDeLecture extends Modele{
	
	
	
	/**
	 * The id of the owner of this song.
     * @see Utilisateur#id
	 */
	@Column(name = "Proprietaire")
	private int owner;
	
	/**
	 * The playList's name.
     * @see Utilisateur#id
	 */
	@Column(name = "Nom")
	private String name;
	
	
	/**
	 * If the PlayList is available for all.
	 * (private playlist VS. public plailist )
	 */
	@Column(name = "Publique" , columnDefinition   ="TINYINT(1)" )
	private boolean isPublic;
	
	/**
	 * If the PlayList is curently activated
	 */
	@Column(name = "Active" , columnDefinition   ="TINYINT(1)" )
	private boolean isActive;
	
	/**
     * The modification date
     */
    @Column(name = "DATE") 
    private Date date; 
	
	
	/**
     * DO NOT USE it is useless 
     * "Unenhanced classes must have a public or protected no-args constructor"
     */
    public ListesDeLecture(){
    }
    
    
    /**
     * Use this constructor instead. X params
     * 
     * @param pNom, A description for the avatar 
     * @param pAvatar,  The avatar image in Base64
     */
    public ListesDeLecture(int pOwner ,  String pName , boolean pIsPublic , boolean pIsActive ){
		if( validateName(pName) ){ // TODO real validation
			this.owner = pOwner ;
			this.name    = pName;
			this.isPublic = pIsPublic;
			this.isActive = pIsActive;
		}
		else{
    		System.err.println("ListesDeLecture.constructor("+pOwner+""+pName+""+pIsPublic +""+pIsActive+") -> INVALIDE");
    	}
		
	}
    
    /**
     * Set the owner of the playlist
     * @return owner
     * @see Utilisateur#id
     */
	public int getOwner() {
		return owner;
	}
	
	/**
	 * Set the owner of the playlist
	 * @see Utilisateur#id
	 */
	public void setOwner(int pOwner) {
		this.owner = pOwner;
	}
	
	/**
     * Get the name of the playlist.
     * @return name The name of the playlist.
     */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name of the playlist.
	 * @param pName,   The name to be set
	 * @return ok if name has been changed.
	 */
	public boolean setName(String pName) {
		this.name = pName;
		boolean ok = validateName(pName);
		this.name = (ok?pName:this.name);
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
	public static boolean validateName(String pName) {
		int length = pName.length();
		boolean ok = length>=4 && length<=50 ;
		if (!ok) {
			System.err.println("ListesDeLecture.validateName("+pName+") ->INVALIDE");
		}
        return ok;
	}
	
	/**
	 * Get if the playlist is public
	 * @return isPublic
	 */
	public boolean isPublic() {
		return isPublic;
	}
	
	/**
	 * Set the playlist public availibility
	 * @param pIsPublic The public state to set
	 */
	public void setPublic(boolean pIsPublic) {
		this.isPublic = pIsPublic;
	}
	
	/**
	 * Get if the playlist is active
	 * @return isActive
	 */
	public boolean isActive() {
		return isActive;
	}
	
	/**
	 * Activate/Desactivate the playlist
	 * @param pIsActive The active state to set
	 */
	public void setActive(boolean pIsActive) {
		this.isActive = pIsActive;
	}
	
	/**
	 * Get the last modification date  date.
	 * @return date The last modification date
	 */
	public Date getDate() {
		return date;
	}
	
	/**
	 * Set the last modification date to the current date.
	 */
	public void setDate() {
		this.date = new Date();
	}


	/**
	 * Set the last modification date to the desired date.
	 * @param pDate The desired date.
	 */
	public void setDate(Date date) {
		this.date = date;
	}
    
    

}



