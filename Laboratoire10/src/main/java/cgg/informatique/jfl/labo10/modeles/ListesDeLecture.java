package cgg.informatique.jfl.labo10.modeles;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@NamedQueries({
   @NamedQuery(name = "ListesDeLecture.list", query = "select a from Avatar a")
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
		if(true ){ // TODO real validation
			this.owner = pOwner ;
			this.name    = pName;
			this.isPublic = pIsPublic;
			this.isActive = pIsActive;
		}
		
	}
    
    
    
    

}



