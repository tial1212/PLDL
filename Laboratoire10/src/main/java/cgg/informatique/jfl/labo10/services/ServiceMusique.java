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
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.Musique;
import cgg.informatique.jfl.labo10.modeles.Token;

import javax.ejb.EJB;
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
    public Token createSong(@PathParam("idToken")     int	  pIdToken,
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
    		return daoMusique.create(pIdOwner, pTitle, pArtist, pMusic, pCoverArt, pIsPublic, pIsActive);
		}
    	return token;
    }
    
    @Path("/getPrivateSong")
	@PUT
	public Musique getPrivateSong(@PathParam("idToken")	int		pIdToken,
            					  @QueryParam("cle") 	String  pKey ,  
            					  @QueryParam("idSong") int		pIdSong ) {
		LOGGER.info("ServiceMusique->getPrivateSong(" + pIdToken + "," +pKey+ "," +pIdSong+")" );
    	if ( !daoToken.confirmCanDoAction(pIdToken, pKey ).getEtat()  ){
    		return null;
		}
    	return daoMusique.getPrivateSong(pIdToken,  pIdSong);
	}
    
    @Path("/getPublicSong")
	@PUT
	public Musique getPublicSong(@PathParam("idToken")    int   pIdToken,
								 @QueryParam("cle") 	    String pKey ,  
            			   		 @QueryParam("idSong") 	int    pIdSong ) {
		LOGGER.info("ServiceMusique->getPublicSong("+pKey+ "," +pIdSong+")" );
    	if ( !daoToken.confirmCanDoAction(pIdToken, pKey ).getEtat()  ){
    		return null;
		}
    	return daoMusique.getPublicSong(pIdSong);
	}
    
    @Path("/modify")
	@PUT
	public Token modify(  @PathParam("idToken")		int	pIdToken,
            			  @QueryParam("cle")		String	pKey,  
            			  @QueryParam("idSong")		int		pIdSong, 
	 					  @QueryParam("idUtilisateur")int 	pIdOwner ,
	 					  @QueryParam("titre")		String	pTitle , 
	 					  @QueryParam("artiste")	String	pArtist , 
	 					  @QueryParam("vignette")	String	pCoverArt ,
	 					  @QueryParam("publique")	boolean	pIsPublic ,
	 					  @QueryParam("active")		boolean	pIsActive) {
		LOGGER.info("ServiceMusique->modify("+pIdToken+","+pKey+","+pIdSong+","+pIdOwner+","+pTitle+","+pArtist+","+pCoverArt+","+pIsPublic+","+pIsActive+")" );
		Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
    	if ( token.getEtat()  ){
    		return daoMusique.modify(pIdSong, pIdOwner, pTitle, pArtist, pCoverArt, pIsPublic, pIsActive);
		}
    	return token;
	}
    
    
    @Path("/setActive")
	@PUT
	public Token setActive(@PathParam("idToken")    int     pIdToken,
            			   @QueryParam("cle") 	    String  pKey ,  
            			   @QueryParam("idSong") 	int     pIdSong ,
            			   @QueryParam("active") 	boolean pActive  ) {
		LOGGER.info("ServiceMusique->setActive(" + pIdToken + "," +pKey+ "," +pIdSong+"," +pActive+")" );
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
		if ( token.getEtat() ){
			return daoMusique.setActive(pIdSong, pIdToken, pActive);
		}
    	return token;
	}
    
    @Path("/setPublic")
	@PUT
	public Token setPublic(@PathParam("idToken")    int     pIdToken,
            			   @QueryParam("cle") 	    String  pKey ,  
            			   @QueryParam("idSong") 	int     pIdSong ,
            			   @QueryParam("public") 	boolean pIsPublic   ) {
		LOGGER.info("ServiceMusique->setActive(" + pIdToken + "," +pKey+ "," +pIdSong+"," +pIsPublic +")" );
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
		if ( token.getEtat() ){
    		return daoMusique.setPublic(pIdSong, pIdToken, pIsPublic );
		}
    	return token;
	}
}
