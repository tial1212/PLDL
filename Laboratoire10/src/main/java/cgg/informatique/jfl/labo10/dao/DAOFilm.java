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
import cgg.informatique.jfl.labo10.modeles.Film;

import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.inject.Inject;
import java.util.List;
import java.util.logging.Logger;

@Singleton
@Lock(LockType.READ)
public class DAOFilm {

    @Inject
    private DAO dao;
    
    private Logger LOGGER = Logger.getLogger(Demarrage.class.getName());
	

    public Film creer(String pTitre, String pAffiche, String pBandeAnnonce, String pResume) {
    	LOGGER.info("DAOFilm->creer("+pTitre+","+pAffiche+","+pBandeAnnonce+","+pResume+")");
    	Film film = new  Film(pTitre , pAffiche , pBandeAnnonce , pResume);
    	
        return dao.creer(film);
    }
    
    public List<Film> afficherListe(int pPremier, int pDernier) {
    	LOGGER.info("DAOFilm->afficherListe("+pPremier+","+pDernier+")");
        return dao.rechercheParRequete(Film.class, "film.list", pPremier, pDernier);
    }

    public Film rechercher(long pId) {
    	LOGGER.info("DAOFilm->rechercher("+pId+")");
        return dao.rechercher(Film.class, pId);
    }

    public void effacer(long pId) {
    	LOGGER.info("DAOFilm->effacer("+pId+")");
        dao.effacer(Film.class, pId);
    }

    public Film modifier(long pId, String pTitre, String pAffiche, String pBandeAnnonce, String pResume) {
    	LOGGER.info("DAOFilm->afficherListe("+pId+","+pTitre+","+pAffiche+","+pBandeAnnonce+","+pResume+")");
    	Film film = dao.rechercher(Film.class, pId);
        if (film == null) {
            throw new IllegalArgumentException("MAJ id " + pId + " n\'existe pas!");
        }
         
        film.setTitle(pTitre);
        film.setCoverArt(pAffiche);
        film.setTrailler(pBandeAnnonce);
        film.setResume(pResume);
        return dao.modifier(film);
    }
}
