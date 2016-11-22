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
	 
    
    Logger LOGGER = Logger.getLogger(Demarrage.class.getName()); 

	/**
	 * Find a song by ID
	 * @param pId
	 * @return
	 */
    public Musique find(long pId) {
    	LOGGER.info("DAOMusique->rechercher("+pId+")");
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
	 * @param pIdOwner
	 * @param pTitle
	 * @param pArtist
	 * @param pMusic
	 * @param pCoverArt
	 * @param pIsPublic
	 * @param pIsActive
	 * @return
	 */
    public Token createSong(int pIdOwner ,String pTitle ,  String pArtist , String pMusic ,String pCoverArt ,boolean pIsPublic ,boolean pIsActive) {
    	LOGGER.info("DAOMusique->createMusique("+ pIdOwner+ "," + pTitle+ "," + pArtist+ "," + pMusic+ "," + pIsPublic+ "," + pIsActive+")" );
        
    	boolean userExist  = DAOUtilisateur.isUserExisting(pIdOwner);
        boolean userActivated = DAOUtilisateur.isUserActivated(pIdOwner);
        boolean okTitle  = Musique.validateTitle(pTitle);
        boolean okArtist  = Musique.validateArtist(pArtist);

        if ( !userExist || !userActivated || !okArtist ) {
        	 Token token = new Token(false, (userExist?"":"user doesn't exist! ")+
        			 						(userExist?"":"user not activated! ")+
        			 						(okTitle?"":"title not valid! ")+
        			 						(okArtist?"":"artist not valid! ") );
        	 LOGGER.info("DAOMusique->createMusique() ERROR : "+token.getAction()  );
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
        	Token token2 = new Token(false, "erreur création musique dans DB");
        	LOGGER.info("DAOMusique->createMusique() ERROR : "+token2.getAction()  );
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
     * <li>song's title must respect policy</li>
     * <li>song's artist must respect policy</li>
     * </ul>
	  */
    public Token modifySong(int pIdSong , int pIdOwner ,String pTitle ,  String pArtist , String pMusic ,String pCoverArt ,boolean pIsPublic ,boolean pIsActive) {
    	LOGGER.info("DAOMusique->createMusique("+ pIdOwner+ "," + pTitle+ "," + pArtist+ "," + pMusic+ "," + pIsPublic+ "," + pIsActive+")" );
        // user exist and is activated ??
        // artist respect policy  ??
        boolean userExist  = DAOUtilisateur.isUserExisting(pIdOwner);
        boolean userActivated = DAOUtilisateur.isUserActivated(pIdOwner);
        boolean okTitle  = Musique.validateTitle(pTitle);
        boolean okArtist  = Musique.validateArtist(pArtist);

        if ( !userExist || !userActivated || !okArtist ) {
        	 Token token = new Token(false, (userExist?"":"user doesn't exist! ")+
        			 						(userExist?"":"user not activated! ")+
        			 						(okTitle?"":"title not valid! ")+
        			 						(okArtist?"":"artist not valid! ") );
        	 LOGGER.info("DAOMusique->createMusique() ERROR : "+token.getAction()  );
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
        	Token token2 = new Token(false, "erreur création musique dans DB");
        	LOGGER.info("DAOMusique->createMusique() ERROR : "+token2.getAction()  );
        	return token2;
		}
        return new Token(true, "création musique ok");
    }
    
    /**
     * Activate/Desactivate song<br>
     * To change active state :
	 * <ul>
	 *  <li>Token must exist</li>
     *  <li>Token must be ActionToken ->have email setted</li> 
     * 	<li>Song must exist</li>
     *  <li>User must exist</li>
     *  <li>User must be active</li>
     * 	<li>Song must belong to the user</li>
     * </ul>
     * 
     * @param pIdSong
     * @param pIdUser
     * @return token
     */
    public Token setActive(long pIdSong , int pIdToken , boolean pActive ) {
    	LOGGER.info("DAOMusique->setActive("+pIdSong+","+ pIdToken+","+ pActive+")");
    	Token token = daoToken.rechercher(pIdToken);
    	boolean tokenExist		 = token != null; 
    	boolean tokenIsAction	 = tokenExist && token.getAction().equals( Token.txtActionToken );
    	int     idUser			 =    DAOUtilisateur.getIdForUser(token.getEMail() ) ;
    	boolean userExist		 = tokenIsAction && DAOUtilisateur.rechercher(idUser) != null;
    	Musique song			 = find(pIdSong);
    	boolean songExist 	  	 = userExist && song != null ;
    	boolean songBelongToUser = songExist && song.getOwner() == idUser ;
    	boolean okFinal 		 = tokenExist && tokenIsAction && userExist && songExist && songBelongToUser ;
    	
    	if (!tokenExist || !tokenIsAction) {
    		return new Token(false , "Error "+(pActive?"":"des")+"activating song : ");
    		Token token2 = new Token(false, "erreur création musique dans DB");
        	LOGGER.info("DAOMusique->creerUtilisateur() ERROR : "+token2.getAction()  );
        	return token2;
		}
    	
    	//TODO fix error message precision
    	return new Token(okFinal , "Error "+(pActive?"":"des")+"activating song : ");
    }
    
    /**
     * Set Public/Private song<br>
     * To change public state :
	 * <ul>
	 *  <li>Token must exist</li>
     *  <li>Token must be ActionToken ->have email setted</li> 
     * 	<li>Song must exist</li>
     *  <li>User must exist</li>
     *  <li>User must be active</li>
     * 	<li>Song must belong to the user</li>
     * </ul>
     * 
     * @param pIdSong
     * @param pIdToken
     * @param pActive
     * @return
     */
    public Token setPublic(long pIdSong , int pIdToken , boolean pActive ) {
    	
    }
}
