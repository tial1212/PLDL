package cgg.informatique.jfl.labo10.dao;

import java.util.logging.Logger;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;

import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.Musique;
import cgg.informatique.jfl.labo10.modeles.Token;



@Singleton
@Lock(LockType.READ)
public class DAOMusique {
	
	//TODO erase me when done
	// SONG ATTRIBUTE = 
	 // int owner,String title, String artist, String music, String coverArt,
	 // boolean isPublic , boolean isActive;
	 
	
	 @Inject
    private DAO dao;
	 
	 @Inject
	 private DAOToken daoToken;
	 
	 @Inject
	 private DAOMusique daoSong;
	 
	 
    
    Logger LOGGER = Logger.getLogger(Demarrage.class.getName());

	

	/**
	 * Find a song by ID
	 * @param pId The ID of the desired song
	 * @return musique The desired song OR null 
	 */
    public Musique find(int pId) {
    	LOGGER.info("DAOMusique->find("+pId+")");
    	return dao.find(Musique.class, pId);
    }
	 
	/**
     * Create a song
     * To be valid :<br>
     * <ul>
     * 	<li>Must be linked to an existing User</li>
     * <li>User must be activated</li>
     * <li>song's title must respect policy</li>
     * <li>song's artist must respect policy</li>
     * </ul>
	 * @param pIdOwner The ID of the owner.
	 * @param pTitle The title of the song.
	 * @param pArtist The song's artist.
	 * @param pMusic The song itself ( Base64 ).
	 * @param pCoverArt The cover art for the song ( Base64 ).
	 * @param pIsPublic If the song is available for all.
	 * @param pIsActive If the song is currently activated.
	 * @return token
	 */
    public Token create(int pIdOwner ,String pTitle ,  String pArtist , String pMusic ,String pCoverArt ,boolean pIsPublic ,boolean pIsActive) {
    	LOGGER.info("DAOMusique->create("+ pIdOwner+ "," + pTitle+ "," + pArtist+ "," + pMusic+ "," + pIsPublic+ "," + pIsActive+")" );
        
    	boolean userExist  = DAOUtilisateur.isUserExisting(pIdOwner);
        boolean userActivated = DAOUtilisateur.isUserActivated(pIdOwner);
        boolean okTitle  = Musique.validateTitle(pTitle);
        boolean okArtist  = Musique.validateArtist(pArtist);

        if ( !userExist || !userActivated || !okArtist ) {
        	 Token token = new Token(false, (userExist?"":"user doesn't exist! ")+
        			 						(userExist?"":"user not activated! ")+
        			 						(okTitle?"":"title not valid! ")+
        			 						(okArtist?"":"artist not valid! ") );
        	 LOGGER.info("DAOMusique->create() ERROR : "+token.getAction()  );
        	 return token;
		}
        Musique musique = dao.persist(new Musique(pIdOwner, pTitle, pArtist, pMusic, pCoverArt, pIsPublic, pIsActive) );
       
        //verify that created object (in DB) are correct
        // delete all if error 
        if ( !musique.getTitle().equals(pTitle) 	 || 
        	 !musique.getArtist().equals(pArtist)  	 || 
        	 !musique.getMusic().equals(pMusic)  	 ||
        	 !musique.getCoverArt().equals(pCoverArt)||
        	  musique.isPublic() != pIsPublic  		 ||
        	  musique.isActive() !=pIsActive         ) {
        	dao.remove(Musique.class , musique.getId() );
        	Token token2 = new Token(false, "erreur création musique dans DB");
        	LOGGER.info("DAOMusique->create() ERROR : "+token2.getAction()  );
        	return token2;
		}
        return new Token(true, "création musique ok");
    }
    
    /**
     * Create a song
     * To be valid :<br>
     * <ul>
     * 	<li>Must be linked to an existing User</li>
     * <li>User must be activated</li>
     * <li>Song must exist</li>
     * <li>song's title must respect policy</li>
     * <li>song's artist must respect policy</li>
     * <li>song must belong to user</li>
     * </ul>
     * 
     * @param pIdSong The ID of the song
     * @param pIdOwner The ID of the owner.
	 * @param pTitle The title of the song.
	 * @param pArtist The song's artist.
	 * @param pCoverArt The cover art for the song ( Base64 ).
	 * @param pIsPublic If the song is available for all.
	 * @param pIsActive If the song is currently activated.
     * @return token
     */
    public Token modify(int pIdSong , int pIdOwner ,String pTitle ,  String pArtist ,String pCoverArt ,boolean pIsPublic ,boolean pIsActive) {
    	LOGGER.info("DAOMusique->modify("+ pIdOwner+ "," + pTitle+ "," + pArtist+ "," + pIsPublic+ "," + pIsActive+")" );
        
    	boolean userExist  = DAOUtilisateur.isUserExisting(pIdOwner);
        boolean userActivated = DAOUtilisateur.isUserActivated(pIdOwner);
        Musique song  = daoSong.find(pIdSong);
        boolean songExist = song != null;
        boolean okTitle  = Musique.validateTitle(pTitle);
        boolean okArtist  = Musique.validateArtist(pArtist);

        if ( !userExist || !userActivated || !songExist || !okArtist ) {
        	 Token token = new Token(false, (userExist?"":"user doesn't exist! ")+
        			 						(userActivated?"":"user not activated! ")+
        			 						(songExist?"":"song doesn't exist! ")+
        			 						(okTitle?"":"title not valid! ")+
        			 						(okArtist?"":"artist not valid! ") );
        	 LOGGER.info("DAOMusique->modify() ERROR : "+token.getAction()  );
        	 return token;
		}
        
        song.setTitle(pTitle);
        song.setArtist(pArtist);
        song.setCoverArt(pCoverArt);
        song.setPublic(pIsPublic);
        song.setActive(pIsActive);
        Musique musiqueRetour = dao.persist(song );
       
        //verify that modified object (in DB) are correct
        if ( !musiqueRetour.getTitle().equals(pTitle) 	 || 
        	 !musiqueRetour.getArtist().equals(pArtist)  	 ||
        	 !musiqueRetour.getCoverArt().equals(pCoverArt)||
        	  musiqueRetour.isPublic() != pIsPublic  		 ||
        	  musiqueRetour.isActive() !=pIsActive         ) {
        	Token token2 = new Token(false, "erreur modification musique dans DB");
        	LOGGER.info("DAOMusique->modify() ERROR : "+token2.getAction()  );
        	return token2;
		}
        return new Token(true, "création musique ok");
    }
    
    /**
     * Activate/Desactivate song<br>
     * To change active state :
	 * <ul>
	 *  <li>Token must exist</li>
     *  <li>Token must be ActionToken : email is set </li> 
     * 	<li>Song must exist</li>
     *  <li>User must exist</li>
     *  <li>User must be active</li>
     * 	<li>Song must belong to the user</li>
     * </ul>
     * 
     * @param pIdSong The ID of the song
     * @param pIdToken The ID of the token that 
     * @param pIsActive If song is active
     * @return token
     */
    public Token setActive(int pIdSong , int pIdToken , boolean pIsActive ) {
    	LOGGER.info("DAOMusique->setActive("+pIdSong+","+ pIdToken+","+ pIsActive+")");
    	Token token = daoToken.rechercher(pIdToken);
    	boolean tokenExist		 = token != null; 
    	boolean tokenIsAction	 = tokenExist && token.getAction().equals( Token.txtActionToken );
    	int     idUser			 = DAOUtilisateur.getIdForUser(token.getEMail() ) ;
    	boolean userExist		 = tokenIsAction && DAOUtilisateur.rechercher(idUser) != null;
    	boolean userActive		 = tokenIsAction && DAOUtilisateur.rechercher(idUser) != null;
    	Musique song			 = find(pIdSong);
    	boolean songExist 	  	 = userExist && song != null ;
    	boolean songBelongToUser = songExist && song.getOwner() == idUser ;
    	
    	if (!tokenExist || !tokenIsAction) {
    		Token token2 = new Token(false, (tokenExist?"Token pas ActionToken":"Token inexistant") );
        	LOGGER.info("DAOMusique->setActive() ERROR : "+token2.getAction()  );
        	return token2;
		}
    	if (!userExist || !songExist ) {
    		Token token3 = new Token(false, (userExist?"Musique inexistante":"Utilisateur inexistant") );
        	LOGGER.info("DAOMusique->setActive() ERROR : "+token3.getAction()  );
        	return token3;
    	}
    	if (userActive || !songBelongToUser ) {
    		Token token4 = new Token(false, (userActive?"Utilisateur non active":"La musique n'apartient pas au demandeur") );
        	LOGGER.info("DAOMusique->setActive() ERROR : "+token4.getAction()  );
        	return token4;
    	}
    	
    	song.setActive(pIsActive);
    	dao.persist(song);
    	
    	return new Token(true , "musique "+(pIsActive?"": "des")+"activé : ");
    }
    
    /**
     * Set Public/Private song<br>
     * To change public state :
	 * <ul>
	 *  <li>Token must exist</li>
     *  <li>Token must be ActionToken : email is set</li> 
     * 	<li>Song must exist</li>
     *  <li>User must be active</li>
     * 	<li>Song must belong to the user</li>
     * </ul>
     * 
     * @param pIdSong The ID of the song
     * @param pIdToken The ID of the token that requested action
     * @param pIsPublic If song is public
     * @return token
     */
    public Token setPublic(int pIdSong , int pIdToken , boolean pIsPublic ) {
    	LOGGER.info("DAOMusique->setPublic("+pIdSong+","+ pIdToken+","+ pIsPublic+")");
    	Token token = daoToken.rechercher(pIdToken);
    	boolean tokenExist		 = token != null; 
    	boolean tokenIsAction	 = tokenExist && token.getAction().equals( Token.txtActionToken );
    	int     idUser			 = DAOUtilisateur.getIdForUser(token.getEMail() ) ;
    	boolean userExist		 = tokenIsAction && DAOUtilisateur.rechercher(idUser) != null;
    	boolean userActive		 = tokenIsAction && DAOUtilisateur.rechercher(idUser) != null;
    	Musique song			 = find(pIdSong);
    	boolean songExist 	  	 = userExist && song != null ;
    	boolean songBelongToUser = songExist && song.getOwner() == idUser ;
    	
    	if (!tokenExist || !tokenIsAction) {
    		Token token2 = new Token(false, (tokenExist?"Token pas ActionToken":"Token inexistant") );
        	LOGGER.info("DAOMusique->setActive() ERROR : "+token2.getAction()  );
        	return token2;
		}
    	if (!userExist || !songExist ) {
    		Token token3 = new Token(false, (userExist?"Musique inexistante":"Utilisateur inexistant") );
        	LOGGER.info("DAOMusique->setActive() ERROR : "+token3.getAction()  );
        	return token3;
    	}
    	if (userActive || !songBelongToUser ) {
    		Token token4 = new Token(false, (userActive?"Utilisateur non active":"La musique n'apartient pas au demandeur") );
        	LOGGER.info("DAOMusique->setActive() ERROR : "+token4.getAction()  );
        	return token4;
    	}
    	
    	song.setPublic(pIsPublic);
    	dao.persist(pIsPublic);
    	
    	return new Token(true , "musique maintenant"+(pIsPublic?"publique": "privé") );
    }
    /**
     * Get a public song
     * <ul>
     * 	<li>Song must exist</li>
     *  <li>Song must be public</li>
     * </ul>
     * 
     * @param pIdSong ID of asked song
     * @return musique The song OR null
     */
	public Musique getPublicSong(int pIdSong) {
		LOGGER.info("DAOMusique->getPublicSong("+pIdSong+")" );
		Musique song = daoSong.find(pIdSong);
		boolean songExist = song != null;
		boolean songPublic = songExist && song.isPublic();
		
		if (songExist && songPublic ) {
			return song;
		}
		LOGGER.info("DAOMusique->getPublicSong() ERROR : "+ (songExist? (songPublic?"song public":"song private") :"song doesn't exist") );
		return null;
	}
	/**
	 * Get a private song.
	 * 
     * <ul>
	 *  <li>Token must exist</li>
     *  <li>Token must be ActionToken : email is set</li> 
     * 	<li>Song must exist</li>
     *  <li>Song must belong to User</li>
     * </ul>
     * 
	 * @param pIdToken ID of the token demanding action
	 * @param pIdSong ID of asked song
	 * @return musique The song OR null
	 */
	public Musique getPrivateSong(int pIdToken, int pIdSong) {
		LOGGER.info("DAOMusique->getPrivateSong("+pIdToken+","+pIdSong+")" );
		Token token = daoToken.rechercher(pIdToken);
		Musique song = daoSong.find(pIdSong);
		boolean tokenExist		 = token != null; 
    	boolean tokenIsAction	 = tokenExist && token.getAction().equals( Token.txtActionToken );
    	boolean tokenValid = tokenExist	&& tokenIsAction;
    	
    	if (!tokenExist || !tokenIsAction) {
        	LOGGER.info("DAOMusique->setActive() ERROR : "+ (tokenExist?"Token pas ActionToken":"Token inexistant") );
        	return null;
		}
    	
    	int     idUser = DAOUtilisateur.getIdForUser(token.getEMail() ) ;
		boolean userExist  = tokenValid && DAOUtilisateur.isUserExisting(idUser);
        boolean userActivated = tokenValid && DAOUtilisateur.isUserActivated(idUser);
        
    	if (!userExist || !userActivated ) {
        	LOGGER.info("DAOMusique->setActive() ERROR : "+(userExist?"Utilisateur non actif":"Utilisateur inexistant")   );
        	return null;
    	}
    	
    	boolean songExist = song != null;
        boolean songBelongToUser = songExist && false;
		
    	if ( !songExist || !songBelongToUser ) {
        	LOGGER.info("DAOMusique->setActive() ERROR : "+ (songBelongToUser?"Utilisateur non active":"La musique n'apartient pas au demandeur")  );
        	return null;
    	}
		return song;
	}
}
