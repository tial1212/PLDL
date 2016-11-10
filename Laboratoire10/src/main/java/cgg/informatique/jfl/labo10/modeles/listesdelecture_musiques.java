package cgg.informatique.jfl.labo10.modeles;

import java.util.Date;

import javax.persistence.Column;

public class listesdelecture_musiques extends Modele {
	
	
	
	

	   Publique     bln
	   Active`      bln
	   Date         datetime
	   
	   
	   
	   
	   /**
	     * The modification date
	     */
	    @Column(name = "DATE") 
	    private Date date; 
	   
	   
	   
	   /**
	     * DO NOT USE it is useless 
	     * "Unenhanced classes must have a public or protected no-args constructor"
	     */
	    public listesdelecture_musiques(){
	    }
	    
	    
	    /**
	     * Use this constructor instead. X params
	     * 
	     * @param pNom, A description for the avatar 
	     * @param pAvatar,  The avatar image in Base64
	     */
	    public listesdelecture_musiques(int pPlaylist ,  int pSong , boolean pIsPublic , boolean pIsActive ){
			if(true ){ // TODO real validation
				this.owner = pOwner ;
				this.name    = pName;
				this.isPublic = pIsPublic;
				this.isActive = pIsActive;
			}
			
		}

}
