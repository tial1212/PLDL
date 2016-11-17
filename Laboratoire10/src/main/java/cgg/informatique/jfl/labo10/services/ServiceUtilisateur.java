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
import cgg.informatique.jfl.labo10.modeles.Utilisateur;

import javax.ejb.EJB;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
    
    private Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
    
    
    @Path("/afficherListe")
    @GET
    public List<Utilisateur> afficherListe(@QueryParam("premier") @DefaultValue("0") int premier,
                           				   @QueryParam("dernier") @DefaultValue("20") int dernier) {
    	Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
    	LOGGER.info("UTILISATEUR AFFICHER LISTE");
    	List<Utilisateur> desUtilisateurs = daoUtil.afficherListe(premier, dernier);
    	
    	
        return desUtilisateurs;
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
