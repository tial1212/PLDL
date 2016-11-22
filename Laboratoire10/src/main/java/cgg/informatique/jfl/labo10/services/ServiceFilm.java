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

import cgg.informatique.jfl.labo10.dao.DAOFilm;
import cgg.informatique.jfl.labo10.demarrage.Demarrage;
import cgg.informatique.jfl.labo10.modeles.Film;
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


@Path("/service/film")
@Produces({ "application/json","text/xml"})
public class ServiceFilm {

    @EJB
    private DAOFilm dao;
    
    //private Logger LOGGER = Logger.getLogger(Demarrage.class.getName());

    @Path("/creer") 
    @PUT
    public Film creer(
			   @QueryParam("titre") String pTitre,
               @QueryParam("bandeAnnonce") String pAffiche,
               @QueryParam("affiche") String pBandeAnnonce , 
               @QueryParam("resume") String pResume) {
    	
        return dao.creer(pTitre , pAffiche , pBandeAnnonce , pResume);
    }

    @Path("/afficherListe")
    @GET
    public List<Film> afficherListe(@QueryParam("premier") @DefaultValue("0") int premier,
                           @QueryParam("dernier") @DefaultValue("20") int dernier) {
    	Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
    	LOGGER.info("UTILISATEUR AFFICHER LISTE");
    	List<Film> desFilm = dao.afficherListe(premier, dernier);
    	
    	
        return desFilm;
    }

    @Path("/afficher/{id}")
    @GET
    public Film afficher(@PathParam("id") long id) {
        return dao.rechercher(id);
    }

    @Path("/effacer/{id}")
    @DELETE
    public void effacer(@PathParam("id") long id) {
        dao.effacer(id);
    }

    @Path("/modifier/{id}")
    @POST
    public Film modifier(
    		@PathParam("id") Long id,
    		@QueryParam("titre") String pTitre ,
            @QueryParam("bandeAnnonce") String pAffiche,
            @QueryParam("affiche") String pBandeAnnonce , 
            @QueryParam("resume") String pResume) {
    	
    	 
        return dao.modifier(id , pTitre , pAffiche , pBandeAnnonce , pResume);
    }
}
