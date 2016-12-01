/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package cgg.informatique.jfl.labo10.dao;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.ListesDeLecture;
import cgg.informatique.jfl.labo10.modeles.Token;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;

import java.util.List;
import java.util.logging.Logger;

@Singleton
@Lock(LockType.READ)
public class DAOListesDeLecture {

    @Inject
    private DAO dao;
    
    @Inject
    private DAOToken daoToken;
    
    
    Logger LOGGER = Logger.getLogger(Demarrage.class.getName()); 
    
    
    /**
     * Create a playlist.
     *
     * @param pIdToken ID of token requesting creation.
     * @param pName Name of the playlist.
     * @param pIsPublic If playlist is public.
     * @param pIsActive If playlist is active.
     * 
     * @return token
     */
    public Token create(int pIdToken, String pName, boolean pIsPublic, boolean pIsActive) {
    	LOGGER.info("DAOListesDeLecture->create("+pIdToken+","+pName+","+pIsPublic+","+pIsActive+")");
    	
    	Token token =  daoToken.rechercher(pIdToken);
    	
    	boolean tokenExist		 = token != null; 
    	boolean tokenIsAction	 = tokenExist && token.getAction().equals( Token.txtActionToken );
    	boolean tokenValid = tokenExist && tokenIsAction;
    	
    	if (!tokenExist || !tokenIsAction ) {
			
		}
    	
    	if (!tokenExist || !tokenIsAction) {
    		Token token2 = new Token(false, (tokenExist?"Token pas ActionToken":"Token inexistant") );
        	LOGGER.info("DAOListesDeLecture->create() ERROR : "+ token2.getAction() );
        	return token2;
		}
    	
    	int     idOwner = DAOUtilisateur.getIdForUser(token.getEMail() ) ;
		boolean ownerExist  = tokenValid && DAOUtilisateur.isUserExisting(idOwner);
        boolean ownerActive = tokenValid && DAOUtilisateur.isUserActivated(idOwner);
        
    	if (!ownerExist || !ownerActive ) {
        	Token token3 = new Token(false, (ownerExist?"Propriétaire non actif":"Propriétaire inexistant") );
        	LOGGER.info("DAOListesDeLecture->create() ERROR : "+ token3.getAction() );
        	return token3;
    	}
    	
    	boolean okName = ListesDeLecture.validateName(pName);
    	if (!okName ) {
        	Token token4 = new Token(false, "Nom non valide" );
        	LOGGER.info("DAOListesDeLecture->create() ERROR : "+ token4.getAction() );
        	return token4;
    	}
    	
    	ListesDeLecture playList = dao.persist( new ListesDeLecture(idOwner, pName, pIsPublic, pIsActive) );
        
    	//verify that created object (in DB) are correct
        // delete all if error 
        if ( !playList.getName().equals(pName)	|| 
        	  playList.getOwner() != idOwner	|| 
        	  playList.isActive() != pIsActive	||
        	  playList.isPublic() != pIsPublic	||
        	  playList.isPublic() != pIsPublic 	) {
        	dao.remove(ListesDeLecture.class , playList.getId() );
        	Token token5 = new Token(false, "erreur création playlist dans DB");
        	LOGGER.info("DAOListesDeLecture->create() ERROR : "+token5.getAction()  );
        	return token5;
	}
        return new Token(true, "création ok");
    }
    
    /**
     * Modify a playlist
     * <ul>
     * 	<li>Playlist must exist</li>
     *  <li>Playlist's name must be public</li>
     *  <li>Playlist must belong to user</li>
     * </ul>
     * 
     * @param pIdToken ID of token requesting action
     * @param pIdPlaylist ID of playlist to modify
     * @param pName Name of the playlist.
     * @param pIsPublic If playlist is public.
     * @param pIsActive If playlist is active.
     * 
     * @return token
     */
	public Token modify(int pIdToken, int pIdPlaylist, String pName, Boolean pIsPublic, Boolean pIsActive ) {
		LOGGER.info("DAOListesDeLecture->modify("+pIdToken+","+pName+","+ pIsPublic+","+ pIsActive+")");
	    
		boolean nullName =  pName == null;
		boolean nullPublic = pIsPublic == null;
		boolean nullActive = pIsActive == null;
		//determine action
		boolean actionModify = !nullName && !nullPublic && !nullActive;
		boolean actionName = !nullName && nullPublic && nullActive;
		boolean actionPublic = nullName && !nullPublic && nullActive;
		boolean actionActive = nullName && nullPublic && !nullActive;
		if (actionModify || actionName || actionPublic || actionActive ) {
			
			
			
			// token exist & valid
			Token token = daoToken.rechercher(pIdToken);
                        boolean tokenExist		 = token != null; 
                        boolean tokenIsAction	 = tokenExist && token.getAction().equals( Token.txtActionToken );
                        if (!tokenExist || !tokenIsAction) {
                            Token token2 = new Token(false, (tokenExist?"Token pas ActionToken":"Token inexistant") );
                            LOGGER.info("DAOListesDeLecture->modify() ERROR : "+token2.getAction()  );
                            return token2;
			}
	    	//owner exist &&  active 
	    	boolean userExist	= DAOUtilisateur.getIdForUser(token.getEMail() ) != -1 ;
	    	int idUser			= DAOUtilisateur.getIdForUser(token.getEMail() );
	    	boolean userActive	= DAOUtilisateur.isUserActivated(idUser);
	    	if (!userExist || !userActive ) {
	    		Token token3 = new Token(false, (userExist?"Utilisateur non actif":"Utilisateur inexistant") );
	        	LOGGER.info("DAOListesDeLecture->modify() ERROR : "+token3.getAction()  );
	        	return token3;
	    	}
			//playlist exist belong to owner 
	    	ListesDeLecture playlist	= dao.find(ListesDeLecture.class, pIdPlaylist);
	    	boolean playlistExist		= playlist != null;
	    	boolean playlistBelongUser	= playlistExist && playlist.getOwner() == idUser;
	    	if (!playlistExist || !playlistBelongUser ) {
	    		Token token4 = new Token(false, (playlistExist?"Playlist inexistant":"Playlist n'appartient pas a l'utilisateur") );
	        	LOGGER.info("DAOListesDeLecture->modify() ERROR : "+token4.getAction()  );
	        	return token4;
	    	}
	    	//name ok if given
	    	boolean okName = (  actionModify ||actionName ?   ListesDeLecture.validateName(pName) : true );
	    	if (!okName) {
	    		Token token5 = new Token(false, "Nom incorrect" );
	        	LOGGER.info("DAOListesDeLecture->modify() ERROR : "+token5.getAction() );
	        	return token5;
	    	}
	    	//OK yay...
	    	playlist.setDate();
	    	if ( actionModify) { // modify
                    playlist.setName(pName);
                    playlist.setPublic(pIsPublic);
                    playlist.setActive(pIsActive);
                }
                else if(actionName ) { // setPlaylistName
                        playlist.setName(pName);
                }
                else if( actionPublic ) { // setPlaylistPublic
                        playlist.setPublic(pIsPublic);
                }
                else { // setPlaylistActive
                        playlist.setActive(pIsActive);
                }
                ListesDeLecture playlistRetour = dao.modifier(playlist);
                String nomRetour = playlistRetour.getName();
                boolean publicRetour = playlistRetour.isPublic();
                boolean activeRetour = playlistRetour.isActive();
                // test if modification ok
                if (!nomRetour.equals(  (actionModify || actionName   ? pName : nomRetour ) )	 || 
                        publicRetour != (actionModify || actionPublic ? pIsPublic : publicRetour ) ||
                        activeRetour != (actionModify || actionActive ? pIsActive : activeRetour )) {
                Token token6 = new Token(false, "erreur modification utilisateur dans DB");
                LOGGER.info("DAOListesDeLecture->modify() ERROR : "+token6.getAction()  );
                return token6;
                }
            }
            else {
                    return new Token(false, "modification impossible mauvais parametres");
            }
		
		return new Token(true, "Modifications à la playlist effectuées");
	}

	/**
	 * Erase a playlist.
     * @param pIdPlaylist ID of playlist to errase
     */
    public void errase(int pIdPlaylist) {
    	// TODO return token w/ validation
    	LOGGER.info("DAOListesDeLecture->effacer("+pIdPlaylist+")");
	    dao.remove(ListesDeLecture.class, pIdPlaylist);
    }

    
    /**
     * Get a public playlist
     * <ul>
     * 	<li>Playlist must exist</li>
     *  <li>Playlist must be public</li>
     * </ul>
	 * @param pIdPlaylist ID of desired playlist
	 * @return playlist OR null
	 */
	public ListesDeLecture getPublicPlaylist(int pIdPlaylist) {
            LOGGER.info("DAOListesDeLecture->getPublicPlaylist("+pIdPlaylist+")");

            //playlist exist 
            ListesDeLecture playlist	= dao.find(ListesDeLecture.class, pIdPlaylist);
            boolean playlistExist		= playlist != null;
            if (!playlist.isPublic()) {
                LOGGER.info("DAOListesDeLecture->getPrivatePlaylist() ERROR : Playlist privée");
                return null;
            }
            if (!playlistExist ) {
                LOGGER.info("DAOListesDeLecture->getPrivatePlaylist() ERROR : Playlist inexistante");
            }
            return playlist;
	}
	
	/**
	 * Get a private playlist
	 * 
	 * <ul>
     * 	<li>Playlist must exist</li>
     *  <li>Related user exist</li>
     *  <li>Related user activated</li>
     *  <li>Playlist must belong to user requesting it</li>
     * </ul>
     * 
	 * @param pIdToken ID of token requesting action
	 * @param pIdPlaylist ID of playlist requested
	 * @return playlist OR null
	 */
	public ListesDeLecture getPrivatePlaylist(int pIdToken, int pIdPlaylist) {
		LOGGER.info("DAOListesDeLecture->getPrivatePlaylist("+pIdToken+","+pIdPlaylist+")" );
		
		Token token = daoToken.rechercher(pIdToken);
		boolean tokenExist		 = token != null; 
    	boolean tokenIsAction	 = tokenExist && token.getAction().equals( Token.txtActionToken );
    	if (!tokenExist || !tokenIsAction) {
    		LOGGER.info("DAOListesDeLecture->getPrivatePlaylist() ERROR : "+ (tokenExist?"Token pas ActionToken":"Token inexistant") );
        	return null;
		}
    	
    	//owner exist &&  active 
    	boolean userExist	= DAOUtilisateur.getIdForUser(token.getEMail() ) != -1 ;
    	int idUser			= DAOUtilisateur.getIdForUser(token.getEMail() );
    	boolean userActive	= DAOUtilisateur.isUserActivated(idUser);
    	if (!userExist || !userActive ) {
        	LOGGER.info("DAOListesDeLecture->getPrivatePlaylist() ERROR : "+(userExist?"Utilisateur non actif":"Utilisateur inexistant") );
        	return null;
    	}
		
    	//playlist exist belong to owner 
    	ListesDeLecture playlist	= dao.find(ListesDeLecture.class, pIdPlaylist);
    	boolean playlistExist		= playlist != null;
    	boolean playlistBelongUser	= playlistExist && playlist.getOwner() == idUser;
    	if (!playlistExist || !playlistBelongUser ) {
        	LOGGER.info("DAOListesDeLecture->getPrivatePlaylist() ERROR : "+ (playlistExist?"Playlist inexistant":"Playlist n'appartient pas a l'utilisateur") );
    	}
    	
		//ok 
		return dao.find(ListesDeLecture.class, pIdPlaylist);
	}
	
	/**
	 * Get a list of public playlist<br>
	 * from a select an interval of result
	 * 
	 * @param pFirst First result interval
	 * @param pLast  Last result interval
	 * @return ListOfPlaylist OR null
	 */
	public List<ListesDeLecture> getPublicPlaylistList(int pFirst, int pLast ) {
		LOGGER.info("DAOListesDeLecture->getPublicPlaylistList("+pFirst+","+pLast+")");
		//TODO 
	    return dao.rechercheParRequete(ListesDeLecture.class, "listesdelecture.list", pFirst, pLast);
	}
	
	/**
	 * Get a list of public playlist<br>
	 * from a select an interval of result
	 * 
	 * @param pFirst First result interval
	 * @param pLast  Last result interval
	 * @return ListOfPlaylist
	 */
	public List<ListesDeLecture> getMyPlaylists(int pIdToken , int pFirst, int pLast ) {
		LOGGER.info("DAOListesDeLecture->getMyPlaylists("+pFirst+","+pLast+")");
		//TODO 
		return dao.rechercheParRequete(ListesDeLecture.class, "listesdelecture.list", pFirst, pLast);
	}
    
}
