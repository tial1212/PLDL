/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 *     contributor license agreements.  See the NOTICE file distributed with
 *     this work for additional information regarding copyright ownership.
 *     The ASF licenses this file to You under the Apache License, Version 2.0
 *     (the "License"); you may not use this file except in compliance with
 *     the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */
package cgg.informatique.jfl.labo10.services;

import cgg.informatique.jfl.labo10.dao.DAO;
import cgg.informatique.jfl.labo10.dao.DAOMusique;
import cgg.informatique.jfl.labo10.dao.DAOToken;
import cgg.informatique.jfl.labo10.dao.DAOUtilisateur;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.Musique;
import cgg.informatique.jfl.labo10.modeles.Token;
import cgg.informatique.jfl.labo10.modeles.Utilisateur;

import javax.ejb.EJB;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.logging.Logger;


@Path("/service/musique")
@Produces({"application/json" , "text/xml"})
public class ServiceMusique {

    @EJB
    private DAOMusique daoMusique;
    
    @EJB
    private DAOToken daoToken;
    
    @EJB
    private DAO dao;
    
    private Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
    
    
    
	@Path("/createSong")
    @PUT
    public Token createSong(@PathParam("idToken")    long    pIdToken,
				            @QueryParam("cle") 	      String  pKey,
				    		@QueryParam("idOwner") 	  int     pIdOwner,
                       		@QueryParam("titre")      String  pTitle,
                       		@QueryParam("artiste")    String  pArtist,
                       		@QueryParam("musique")    String  pMusic,
                       		@QueryParam("coverArt")   String  pCoverArt,
                       		@QueryParam("public")     boolean pIsPublic,
                       		@QueryParam("active")     boolean pIsActive) {
		
		//  int pIdOwner ,String pTitle ,  String pArtist , String pMusic ,String pCoverArt ,boolean pIsPublic ,boolean pIsActive
		
    	LOGGER.info("ServiceMusique->createSong("+ pIdToken + "," + pKey + "," + pIdOwner+ "," + pTitle+ "," + pArtist+ "," + pMusic+ "," + pIsPublic+ "," + pIsActive+")" ); 
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
    	if ( token.getEtat()  ){
    		return daoMusique.createSong(pIdOwner, pTitle, pArtist, pMusic, pCoverArt, pIsPublic, pIsActive);
		}
    	return token;
    }
    
    @Path("/getSong")
	@PUT
	public Musique getSong(@PathParam("idToken")    long   pIdToken,
            			   @QueryParam("cle") 	    String pKey ,  
            			   @QueryParam("idSong") 	int    pIdSong ) {
		LOGGER.info("ServiceMusique->getSong(" + pIdToken + "," +pKey+ "," +pIdSong+")" );
    	if ( !daoToken.confirmCanDoAction(pIdToken, pKey ).getEtat()  ){
    		return null;
		}
    	return daoMusique.find(pIdSong);
	}
    
    @Path("/modify")
	@PUT
	public Musique modify(@PathParam("idToken")    long   pIdToken,
            			   @QueryParam("cle") 	    String pKey ,  
            			   @QueryParam("idSong") 	int    pIdSong ) {
		LOGGER.info("ServiceMusique->getSong(" + pIdToken + "," +pKey+ "," +pIdSong+")" );
    	if ( !daoToken.confirmCanDoAction(pIdToken, pKey ).getEtat()  ){
    		return null;
		}
    	return daoMusique.find(pIdSong);
	}
    
    
    @Path("/setActive")
	@PUT
	public Token setActive(@PathParam("idToken")    int     pIdToken,
            			   @QueryParam("cle") 	    String  pKey ,  
            			   @QueryParam("idSong") 	int     pIdSong ,
            			   @QueryParam("active") 	boolean pActive  ) {
		LOGGER.info("ServiceMusique->setActive(" + pIdToken + "," +pKey+ "," +pIdSong+"," +pActive+")" );
    	if ( !daoToken.confirmCanDoAction(pIdToken, pKey ).getEtat()  ){
    		return null;  //TODO check what it create as json
		}
    	return daoMusique.setActive(pIdSong, pIdToken, pActive);
	}
}
