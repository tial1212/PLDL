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


@Path("/service/utilisateur")
@Produces("application/json")
public class ServiceUtilisateur {

    @EJB
    private DAOUtilisateur daoUtil;
    
    @EJB
    private DAOToken daoToken;
    
    @EJB
    private DAO dao;
    
    private Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
    
    
    
	@Path("/createUser")
    @PUT
    public Token createUser(@QueryParam("alias") 	  String pAalias,
                       		@QueryParam("motDePasse") String pMotDePasse,
                       		@QueryParam("courriel")   String pCourriel,
                       		@QueryParam("avatar")     int    pIdAvatar) {
    	LOGGER.info("ServiceUtilisateur->createUser("+ pAalias + "," + pMotDePasse + "," + pCourriel+")" ); 
    	return daoUtil.createUser(pAalias, pMotDePasse, pCourriel, pIdAvatar);
    }
    
    @Path("/confirmCreateUser")
	@PUT
	public Token confirmCreateUser(@QueryParam("idToken")    int pIdToken,
	                               @QueryParam("captchaVal") String pCaptchaVal) {
		LOGGER.info("ServiceUtilisateur->confirmCreateUser(" + pIdToken + "," +pCaptchaVal+")" );
		return daoUtil.activateUser(pIdToken, pCaptchaVal);
	}
    
    @Path("/login")
	@PUT
	public Token login(@QueryParam("courriel")   String pCourriel,
	                   	 @QueryParam("motDePasse") String pMotDePasse) {
	    	LOGGER.info("ServiceUtilisateur->login("+ pCourriel + "," + pMotDePasse+")" );
	    	Token token  = daoUtil.login(pCourriel, pMotDePasse);
	    return token;
	}
    
    
    @Path("/getUser")
	@PUT
	public Utilisateur getUser( @QueryParam("idToken")	int		pIdToken,
            				@QueryParam("cle") 		String  pKey ) {
		LOGGER.info("ServiceMusique->getPrivateSong(" + pIdToken + "," +pKey+")" );
    	if ( !daoToken.confirmCanDoAction(pIdToken, pKey ).getEtat()  ){
    		return null;
		}
    	return daoUtil.getUser(pIdToken);
	}
    
    @Path("/logoff")
	@PUT
	public Token logoff(@QueryParam( "idToken")      int   pIdToken,
            			@QueryParam("cle") 	      String pKey) {
    		LOGGER.info("ServiceUtilisateur->logoff("+pIdToken+","+pKey+")" );
    		
    		Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
        	if ( token.getEtat()  ){
        		String email = ""; //TODO find in BD
        		return daoUtil.logoff(email);
        		
        	}
	    return token;
	}
    
    @Path("/modify")
    @POST
    public Token modify(
    		           @QueryParam( "idToken")     int   pIdToken,
                       @QueryParam("cle") 	      String pKey,
                       @QueryParam("courriel") 	  String pEMaill,
                       @QueryParam("motDePasse")  String pPasword,
                       @QueryParam("alias")       String pAlias,
                       @QueryParam("avatar")      int    pIdAvatar) {
    	LOGGER.info("ServiceToken->modifier("+ pIdToken + ","+ pKey + ","+ pEMaill + ","+ pPasword + ","+ pAlias + ","+ pIdAvatar +")" );
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
    	if ( token.getEtat()  ){
    		int idUser = 3; //TODO find from token
    		daoUtil.modifier (idUser, pAlias, pPasword, pEMaill, pIdAvatar);
    		Utilisateur util = DAOUtilisateur.rechercher(idUser);
    		if (util.getId() == idUser && 
    			util.getAlias().equals(pAlias) &&
    			util.getAvatar() == pIdAvatar &&
    			util.getEMaill().equals(pEMaill) &&
    			util.getPasowrd().equals(pPasword) ) {
				return new Token(true, "Modification utilisateur ok");
			}
    		
		}
    	return token;
    }
    
    @Path("/effacer")
    @DELETE
    public Token effacer(
    				@QueryParam("idToken")     int   pIdToken,
    				@QueryParam("cle") 	      String pKey,
    				@QueryParam("idUser") 	  int pIdUser) {
    	LOGGER.info("ServiceToken->effacer("+ pIdToken + "," + pKey+ "," + pIdUser+")" );
    	Token token = daoToken.confirmCanDoAction(pIdToken, pKey );
    	if ( token.getEtat()  ){
    		Utilisateur utilisateurRequested = dao.querrySingle("SELECT u FROM Utilisateur u WHERE u.id = :id", "id", pIdUser);
            if (utilisateurRequested != null) {
            	daoUtil.effacer(utilisateurRequested.getId() );
            	return new Token(true, "supression utilisateur réussie");
    		}
            Token token2 = new Token(false, "utilisateur demander non existant");
            LOGGER.info("DAOUtilisateur->activerUser() -> ECHEC : "+token2.getAction() );
            return token2;
		}
    	return token;
    }
}
