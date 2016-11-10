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

import cgg.informatique.jfl.labo10.dao.DAOUtilisateur;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
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
    private DAOUtilisateur dao;

    @Path("/creer")
    @PUT
    public Utilisateur creer(@QueryParam("nom") String nom,
                       @QueryParam("motDePasse") String motDePasse,
                       @QueryParam("courriel") String courriel) {
    	Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
    	LOGGER.info("UTILISATEUR CREER Nom:" + nom + " Password:" + motDePasse + " Courriel:" + courriel);
    	 
        return dao.creer(nom, motDePasse, courriel);
    }

    @Path("/afficherListe")
    @GET
    public List<Utilisateur> afficherListe(@QueryParam("premier") @DefaultValue("0") int premier,
                           @QueryParam("dernier") @DefaultValue("20") int dernier) {
    	Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
    	LOGGER.info("UTILISATEUR AFFICHER LISTE");
    	List<Utilisateur> desUtilisateurs = dao.afficherListe(premier, dernier);
    	
    	
        return desUtilisateurs;
    }

    @Path("/afficher/{id}")
    @GET
    public Utilisateur afficher(@PathParam("id") long id) {
        return dao.rechercher(id);
    }

    @Path("/effacer/{id}")
    @DELETE
    public void effacer(@PathParam("id") long id) {
        dao.effacer(id);
    }

    @Path("/modifier/{id}")
    @POST
    public Utilisateur modifier(
    		           @PathParam("id") Long id,
                       @QueryParam("nom") String nom,
                       @QueryParam("motDePasse") String motDePasse,
                       @QueryParam("courriel") String courriel) {
    	
    	
    	 
        return dao.modifier(id, nom, motDePasse, courriel);
    }
}
