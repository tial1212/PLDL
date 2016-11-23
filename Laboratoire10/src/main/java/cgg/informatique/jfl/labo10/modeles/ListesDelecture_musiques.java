package cgg.informatique.jfl.labo10.modeles;

import javax.persistence.Column;

/**
 * This class act as a relation between the songs and the playlist.
 * It allows for multiple song to be in a playlist.
 * It allows for a song to be in multiple playlist.
 * 
 * @author alexandrearsenault
 *
 */
public class Listesdelecture_musiques extends ModeleDate {
	
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
	    public Listesdelecture_musiques(){
	    }
	    
	    
	    /**
	     * Use this constructor instead. 2 params
	     * 
	     * @param pPlaylist ID of the playlist
	     * @param pSong  ID of the song
	     */
	    public Listesdelecture_musiques(int pPlaylist ,  int pSong ){
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
