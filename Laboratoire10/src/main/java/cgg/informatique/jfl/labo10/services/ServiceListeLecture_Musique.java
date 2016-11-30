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
import cgg.informatique.jfl.labo10.dao.DAOListesdelectureMusiques;
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

import java.util.List;
import java.util.logging.Logger;


@Path("/service/listeLecture_Musique")
@Produces("application/json")
public class ServiceListeLecture_Musique {

    @EJB // FIXME inject ???? 
    private DAOListesdelectureMusiques daoPlaylistMusic;
    
    @EJB
    private DAOToken daoToken;
    
    @EJB
    private DAO dao;
    
    private Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
    
    @Path("/addSongToPlaylist")
    @PUT
    public Token addSongToPlaylist(
				@QueryParam( "idToken")		int		pIdToken,
				@QueryParam("cle")			String	pKey,
                       		@QueryParam("idPlaylist") int	 pIdPlaylist,
                                @QueryParam("idSong")     int	 pIdSong) {
    	LOGGER.info("ServiceListeLecture_Musiques->addSongToPlaylist("+ pIdToken + "," + pKey+ "," + pIdPlaylist+ "," + pIdSong + ")" );
    	
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
    	if ( token.getEtat()  ){
            return daoPlaylistMusic.addSong(pIdPlaylist, pIdSong);
    	}
    	return token;
    }
    
    @Path("/removeSongToPlaylist")
    @DELETE
    public Token removeSongToPlaylist(
    				@QueryParam("idToken")	  int	 pIdToken,
    				@QueryParam("cle")        String pKey , 
    				@QueryParam("idPlaylist") int	 pIdPlaylist,
                                @QueryParam("idSong")     int	 pIdSong) {
    	LOGGER.info("ServiceListeLecture_Musiques->removeSongToPlaylist("+ pIdToken + "," + pKey+ "," + pIdPlaylist+ "," + pIdSong + ")" );
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
    	if ( token.getEtat()  ){
            return daoPlaylistMusic.removeSong(pIdPlaylist, pIdSong);
	}
    	return token;
    }
}
