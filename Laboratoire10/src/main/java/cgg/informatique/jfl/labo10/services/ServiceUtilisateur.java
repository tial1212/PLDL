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

import cgg.informatique.jfl.labo10.dao.DAOToken;
import cgg.informatique.jfl.labo10.dao.DAOUtilisateur;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.Token;
import cgg.informatique.jfl.labo10.modeles.Utilisateur;

import javax.ejb.EJB;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.List;
import java.util.logging.Logger;


@Path("/service/utilisateur")
@Produces({"text/xml", "application/json"})
public class ServiceUtilisateur {

    @EJB
    private DAOUtilisateur daoUtil;
    
    @EJB
    private DAOToken daoToken;
    
    @EJB
	private DAOUtilisateur doaUtil;
    private Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
    
    
    
	@Path("/createUser")
    @PUT
    public Token createUser(@QueryParam("alias") 	  String pAalias,
                       		@QueryParam("motDePasse") String pMotDePasse,
                       		@QueryParam("courriel")   String pCourriel,
                       		@QueryParam("avatar")     int    pIdAvatar) {
    	LOGGER.info("ServiceToken->createUser("+ pAalias + "," + pMotDePasse + "," + pCourriel+")" );
    	 
    	return doaUtil.creerUtilisateur(pAalias, pMotDePasse, pCourriel, pIdAvatar);
    }
    
    @Path("/confirmCreateUser")
	@PUT
	public Token confirmCreateUser(@QueryParam("idToken")    Long idToken,
	                               @QueryParam("captchaVal") String captchaVal,
	                               @QueryParam("courriel")   String pCourriel) {
		LOGGER.info("ServiceToken->confirmCreateUser(" + idToken + "," +captchaVal+","+pCourriel+")" );
		
		boolean ok = daoToken.activerUser(idToken, captchaVal);
		
		Token tokenRetour = new Token();
		tokenRetour.setEtat(ok);
		tokenRetour.setAction("confirmCreerRetour");
		
	  return tokenRetour;
	}
    
    @Path("/login")
	@PUT
	public Token login(@QueryParam("courriel")   String pCourriel,
	                   	 @QueryParam("motDePasse") String pMotDePasse) {
    	LOGGER.info("ServiceToken->connect("+ pCourriel + "," + pMotDePasse+")" );
    	 
	    boolean ok = doaUtil.login(pCourriel, pMotDePasse);
	    Token token =  new Token();
	    token.setEtat(ok);
	    
	    return token;
	}

    @Path("/afficher/{id}")
    @GET
    public Utilisateur afficher(@PathParam("id") long id) {
        return daoUtil.rechercher(id);
    }
    
    
    @Path("/modifier/{id}")
    @POST
    public Utilisateur modifier(
    		           @PathParam("idToken")      long   pIdToken,
                       @QueryParam("cle") 	      String pKey,
                       @PathParam("idUtil")       Long   pIdUser,
                       @QueryParam("courriel") 	  String pEMaill,
                       @QueryParam("motDePasse")  String pPasword,
                       @QueryParam("alias")       String pAlias,
                       @QueryParam("avatar")      int    pAvatar) {
    
    	if (daoToken.confirmCanDoAction(pIdToken, pKey )  ){
    		return daoUtil.modifier(pIdUser, pEMaill, pPasword, pAlias, pAvatar);
		}
    	//FIXME
        return new Utilisateur();
    }
    
    @Path("/effacer/{id}")
    @DELETE
    public void effacer(@PathParam("id") long id) {
        daoUtil.effacer(id);
    }
}
