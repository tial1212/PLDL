package cgg.informatique.jfl.labo10.modeles;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class act as a relation between the songs and the playlist.
 * It allows for multiple song to be in a playlist.
 * It allows for a song to be in multiple playlist.
 * 
 * @author alexandrearsenault
 *
 */
@Entity
@NamedQueries({
   @NamedQuery(name = "listesdelecturemusique.list", query = "select l from ListesDelectureMusiques l")
              })
@XmlRootElement(name = "listesdelecture")
public class ListesDelectureMusiques extends ModeleDate {
	
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
	     * DO NOT USE it is useless 
	     * "Unenhanced classes must have a public or protected no-args constructor"
	     */
	    public ListesDelectureMusiques(){
	    }
	    
	    
	    /**
	     * Use this constructor instead. 2 params
	     * 
	     * @param pPlaylist ID of the playlist
	     * @param pSong  ID of the song
	     */
	    public ListesDelectureMusiques(int pPlaylist ,  int pSong ){
			this.song = pSong ;
			this.playList    = pPlaylist;
		}
	    
	    /**
		 * Get the playlist.
		 * @return playList
		 */
	    public int getPlayList() {
			return playList;
		}
	    
	    /**
	     * Set the playlist
	     * @param pPlayList The song to be set.
	     */
		public void setPlayList(int pPlayList) {
			this.playList = pPlayList;
		}
		
		/**
		 * Get the song.
		 * @return song
		 */
		public int getSong() {
			return song;
		}
		/**
		 * Set the song.
		 * @param pSong The song to be set.
		 */
		public void setSong(int pSong) {
			this.song = pSong;
		}
		
		
		@Override
		public String toString() {
			return "listesdelecture/musiques linking song '"+this.song+"' and playList '"+this.playList+"'";
		}

}
