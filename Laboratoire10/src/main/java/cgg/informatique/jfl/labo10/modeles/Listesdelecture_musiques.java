package cgg.informatique.jfl.labo10.modeles;

import java.util.Date;

import javax.persistence.Column;

/**
 * This class act as a relation between the songs and the playlist.
 * It allows for multiple song to be in a playlist.
 * It allows for a song to be in multiple playlist.
 * 
 * @author alexandrearsenault
 *
 */
public class Listesdelecture_musiques extends Modele {
	
    /**
     * The ID of the selected Avatar.
     * @see ListeDeLecture#id
     */
    @Column(name = "ListeDeLecture", length=11)
    private int playList;
	
	
	/**
     * The ID of the selected Avatar.
     * @see Musique#id
     */
    @Column(name = "Musique", length=11)
    private int song;
   
   
   /**
     * The last modification date.
     */
    @Column(name = "DATE") 
    private Date date; 
	   
	   
	   
	   /**
	     * DO NOT USE it is useless 
	     * "Unenhanced classes must have a public or protected no-args constructor"
	     */
	    public Listesdelecture_musiques(){
	    }
	    
	    
	    /**
	     * Use this constructor instead. X params
	     * 
	     * @param pNom, A description for the avatar 
	     * @param pAvatar,  The avatar image in Base64
	     */
	    public Listesdelecture_musiques(int pPlaylist ,  int pSong ){
			if( true ){ // TODO real validation
				this.song = pSong ;
				this.playList    = pPlaylist;
			}
			
		}
	    
	    
	    @Override
	    public String toString() {
	    	return "listesdelecture/musiques linking song '"+this.song+"' and playList '"+this.playList+"'";
	    }

}
