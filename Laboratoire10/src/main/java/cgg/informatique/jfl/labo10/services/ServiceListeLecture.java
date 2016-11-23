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
import cgg.informatique.jfl.labo10.dao.DAOListesDeLecture;
import cgg.informatique.jfl.labo10.dao.DAOToken;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.ListesDeLecture;
import cgg.informatique.jfl.labo10.modeles.Token;

import javax.ejb.EJB;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.bouncycastle.asn1.pkcs.Pfx;

import java.util.List;
import java.util.logging.Logger;


@Path("/service/listeLecture")
@Produces({"text/xml", "application/json"})
public class ServiceListeLecture {

    @EJB // FIXME inject ???? 
    private DAOListesDeLecture daoPlaylist;
    
    @EJB
    private DAOToken daoToken;
    
    @EJB
    private DAO dao;
    
    private Logger LOGGER = Logger.getLogger(Demarrage.class.getName());

	private DAOListesDeLecture daoUtil;
    
     // TOKEN =  owner name isPublic isActive  TODO remove when done
	
    
	@Path("/createPlaylist")
    @PUT
    public Token createPlaylist(
				    		@PathParam( "idToken")		int		pIdToken,
				            @QueryParam("cle")			String	pKey,
                       		@QueryParam("nom")			String	pName,
                       		@QueryParam("publique")		boolean	pIsPublic,
                       		@QueryParam("active")		boolean	pIsActive) {
    	LOGGER.info("ServiceListeLecture->createPlaylist("+ pIdToken + "," + pKey +"," + pName+"," + pIsPublic+"," + pIsActive+")" ); 
    	
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
    	if ( token.getEtat()  ){
    		return daoPlaylist.create(pIdToken ,pName, pIsPublic , pIsActive  );
    	}
    	return token;
    }
    
    @Path("/modify")
    @POST
    public Token modify(
    		           @PathParam( "idToken")		int		pIdToken,
    		           @QueryParam("cle")			String	pKey,
    		           @PathParam( "idPlaylist")	int		pIdPlaylist,
                       @QueryParam("nom")			String	pName,
                       @QueryParam("publique")		boolean	pIsPublic,
                       @QueryParam("active")		boolean		pIsActive ) {
    	LOGGER.info("ServiceListeLecture->modifier("+ pIdToken + ","+ pKey + ","+ pName + ","+ pIsPublic + ","+ pIsPublic +")" );
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
    	if ( token.getEtat()  ){
    		return daoPlaylist.modify(pIdToken,pIdPlaylist , pName, pIsPublic, pIsActive);
		}
    	return token;
    }
    
    
    @Path("/setPlaylistName")
    @POST
    public Token setPlaylistName(
    		           @PathParam( "idToken")		int		pIdToken,
                       @QueryParam("cle")			String	pKey,
                       @PathParam( "idPlaylist")	int		pIdPlaylist,
                       @QueryParam("nom")			String	pName ) {
    	LOGGER.info("ServiceListeLecture->modifier("+ pIdToken + ","+ pKey+","+pIdPlaylist+","+pName+")" );
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
    	if ( token.getEtat()  ){
    		return daoPlaylist.modify(pIdToken, pIdPlaylist , pName, null, null);
		}
    	return token;
    }
    
    
    @Path("/setPlaylistActive")
    @POST
    public Token setPlaylistActive(
    		           @PathParam( "idToken")		int		pIdToken,
                       @QueryParam("cle")			String	pKey,
                       @PathParam( "idPlaylist")	int		pIdPlaylist,
                       @QueryParam("active")		boolean		pIsActive ) {
    	LOGGER.info("ServiceListeLecture->setPlaylistActive("+ pIdToken +","+pKey +","+pIdPlaylist+","+pIsActive+")" );
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
    	if ( token.getEtat()  ){
    		return daoPlaylist.modify(pIdToken,pIdPlaylist,null,pIsActive,null);
		}
    	return token;
    }
    
    
    @Path("/setPlaylistPublic")
    @POST
    public Token setPlaylistPublic(
    		           @PathParam( "idToken")		int		pIdToken,
                       @QueryParam("cle")			String	pKey,
<<<<<<< Updated upstream
                       @QueryParam("publique")		String	pIsPublic) {
    	LOGGER.info("ServiceListeLecture->modifier("+ pIdToken + ","+ pKey + ","+ pIsPublic + ")" );
=======
                       @PathParam( "idPlaylist")	int		pIdPlaylist,
                       @QueryParam("publique")		boolean	pIsPublic) {
    	LOGGER.info("ServiceListeLecture->modifier("+ pIdToken+","+pKey+","+pIsPublic +","+pIsPublic+")" );
>>>>>>> Stashed changes
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
    	if ( token.getEtat()  ){
    		return daoPlaylist.modify(pIdToken,pIdPlaylist,null, null,pIsPublic);
		}
    	return token;
    }
    
    @Path("/getPublicPlaylist")
    @POST
    public Token getPublicPlaylist(
    		           @PathParam( "idToken")		int		pIdToken,
                       @QueryParam("cle")			String	pKey,
                       @QueryParam("idPlaylist")		int		pIdPlaylist ) {
    	LOGGER.info("ServiceListeLecture->modifier("+pIdToken+","+pKey+","+pIdPlaylist+")" );
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
    	if ( token.getEtat()  ){
    		daoPlaylist.getPublicPlaylist(pIdPlaylist);
		}
    	return token;
    }
    
    
    @Path("/getPrivatePlaylist")
    @POST
    public Token getPrivatePlaylist(
    		           @PathParam( "idToken")		int		pIdToken,
                       @QueryParam("cle")			String	pKey,
                       @QueryParam("idPlaylist")	int		pIdPlaylist ) {
    	LOGGER.info("ServiceListeLecture->modifier("+pIdToken+","+pKey+","+pIdPlaylist+")" );
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
    	if ( token.getEtat()  ){
    		daoPlaylist.getPrivatePlaylist(pIdToken , pIdPlaylist);
		}
    	return token;
    }
    
    @Path("/getPrivatePlaylist")
    @POST
    public List<ListesDeLecture> getMyPlaylists(
		    		@PathParam( "idToken")	int		pIdToken,
		            @QueryParam("cle")		String	pKey,
		            @QueryParam("premier")	int		pFirst,
		            @QueryParam("dernier")	int		pLast){
    	LOGGER.info("ServiceListeLecture->getMyPlaylists("+pIdToken+","+pKey+","+pFirst+","+pLast+")" );
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
    	if ( token.getEtat()  ){
    		daoPlaylist.getMyPlaylists(pFirst, pLast);
		}
    	return null;
    }
    
    @Path("/getPublicPlaylistList")
    @POST
    public List<ListesDeLecture> getPublicPlaylistList(
		    		@PathParam( "idToken")	int		pIdToken,
		            @QueryParam("cle")		String	pKey,
		            @QueryParam("premier")	int		pFirst,
		            @QueryParam("dernier")	int		pLast) {
    	LOGGER.info("ServiceListeLecture->getPublicPlaylistList("+pIdToken+","+pKey+","+pFirst+","+pLast+")" );
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
    	if ( token.getEtat()  ){
    		daoPlaylist.getPublicPlaylistList(pFirst, pLast);
		}
    	return null;
    }
    
    
    
    @Path("/effacer")
    @DELETE
    public Token effacer(
    				@PathParam("idToken")	  int	 pIdToken,
    				@QueryParam("cle")		  String pKey , 
    				@QueryParam("idPlaylist") int	 pIdPlaylist ) {
    	LOGGER.info("ServiceListeLecture->effacer("+ pIdToken + "," + pKey+ "," + pIdPlaylist+")" );
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
    	if ( token.getEtat()  ){
    		//TODO
		}
    	return token;
    }
}
