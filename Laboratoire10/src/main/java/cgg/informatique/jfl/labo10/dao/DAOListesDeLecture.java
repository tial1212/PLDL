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
    	
    	ListesDeLecture playList = new ListesDeLecture(idOwner, pName, pIsPublic, pIsActive);
    	//verify that created object (in DB) are correct
        // delete all if error 
        if ( !playList.getName().equals(pName)	|| 
        	  playList.getOwner() != idOwner	|| 
        	  playList.isActive() != pIsActive	||
        	 !playList.isPublic() != pIsPublic	||
        	  playList.isPublic() != pIsPublic 	) {
        	dao.remove(ListesDeLecture.class , playList.getId() );
        	Token token5 = new Token(false, "erreur création playlist dans DB");
        	LOGGER.info("DAOListesDeLecture->create() ERROR : "+token5.getAction()  );
        	return token5;
		}
        return new Token(true, "création ok");
    }
    
    /**
     * 
     * @param pOwner
     * @param pName
     * @param pIsPublic
     * @param pIsActive
     * @return
     */
	public ListesDeLecture modifier(int pOwner, String pName, boolean pIsPublic, boolean pIsActive ) {
		LOGGER.info("DAOListesDeLecture->modifier("+pOwner+","+pName+","+ pIsPublic+","+ pIsActive+")");
	    
		ListesDeLecture playlist = dao.find(ListesDeLecture.class, pOwner);
	    if (playlist == null) {
	        throw new IllegalArgumentException("MAJ id " + pOwner + " n\'existe pas!");
	    }
	     
	    playlist.setOwner(pOwner);
	    playlist.setName(pName);
	    playlist.setPublic(pIsPublic);
	    playlist.setActive(pIsActive);
	    return dao.modifier(playlist);
	}

	/**
	 * Erase a playlist.
     * @param pId ID of playlist to errase
     */
    public void effacer(int pId) {
    	LOGGER.info("DAOListesDeLecture->effacer("+pId+")");
	    dao.remove(ListesDeLecture.class, pId);
    }

    
    /**
     * Get a public playlist
     * <ul>
     * 	<li>Playlist must exist</li>
     *  <li>Playlist must belong to user requesting it</li>
     * </ul>
	 * @param id ID of desired playlist
	 * @return playlist OR null
	 */
	public ListesDeLecture getPublicPlaylist(int pId) {
		LOGGER.info("DAOListesDeLecture->getPublicPlaylist("+pId+")");
	    return dao.find(ListesDeLecture.class, pId);
	}
	
	/**
	 * Get a public playlist
	 * 
	 * <ul>
     * 	<li>Playlist must exist</li>
     *  <li>Playlist must be public</li>
     * </ul>
	 * @param id ID of desired playlist
	 * @return playlist OR null
	 */
	public ListesDeLecture getPrivatePlaylist(int pIdToken, int pIdPlaylist) {
		LOGGER.info("DAOListesDeLecture->getPrivatePlaylist("+pIdToken+","+pIdPlaylist+")" );
		//TODO validation
	    return dao.find(ListesDeLecture.class, pIdPlaylist);
	}


	//TODO restaure in other class where needed
	/**
	 * Get a list of public playlist<br>
	 * allow to select an interval of result
	 * 
	 * @param pFirst First result interval
	 * @param pLast  Last result interval
	 * @return
	 */
	public List<ListesDeLecture> getPublicPlaylistList( int pFirst, int pLast ) {
		LOGGER.info("DAOListesDeLecture->afficherListe("+pFirst+","+pLast+")");
	    return dao.rechercheParRequete(ListesDeLecture.class, "utilisateur.list", pFirst, pLast);
	}
    
}
